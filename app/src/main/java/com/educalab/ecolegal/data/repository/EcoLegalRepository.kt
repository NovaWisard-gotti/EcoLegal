package com.educalab.ecolegal.data.repository

import com.educalab.ecolegal.data.local.AppDatabase
import com.educalab.ecolegal.data.local.entity.*
import com.educalab.ecolegal.data.local.seed.*
import com.educalab.ecolegal.domain.engine.*
import com.educalab.ecolegal.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Punto único de acceso a los datos de EcoLegal. Combina las consultas Room
 * con los motores de dominio (domain/engine) para producir resultados listos
 * para la UI. Ningún cálculo educativo vive dentro de un Composable.
 */
class EcoLegalRepository(private val db: AppDatabase) {

    private val scenarioEngine = EnvironmentalScenarioEngine()
    private val decisionEngine = DecisionEngine()
    private val consequenceEngine = ConsequenceEngine()
    private val authorizationEngine = AuthorizationEngine()
    private val restorationEngine = RestorationEngine()
    private val progressEngine = ProgressEngine()
    private val rewardEngine = RewardEngine()

    // ---------------------------------------------------------------
    // SEEDING
    // ---------------------------------------------------------------

    /** Puebla la base de datos la primera vez que se instala la app. Idempotente. */
    suspend fun ensureSeeded() {
        if (db.zoneDao().count() > 0) return

        db.zoneDao().insertAll(ZoneSeed.zones)
        db.scenarioDao().insertAll(ScenarioSeed.scenarios)
        db.issueDao().insertAll(ScenarioSeed.issues)
        db.challengeDao().insertAll(ChallengeSeed.challenges)
        db.decisionDao().insertDecisions(ChallengeSeed.decisions)
        db.decisionDao().insertConsequences(ChallengeSeed.consequences)
        db.decisionDao().insertOutcomes(ChallengeSeed.decisionOutcomes)
        db.restorationDao().insertMissions(RestorationSeed.missions)
        db.restorationDao().insertSteps(RestorationSeed.steps)
        db.authorizationDao().insertActivities(AuthorizationSeed.activities)
        db.authorizationDao().insertImpacts(AuthorizationSeed.impacts)
        db.authorizationDao().insertMeasures(AuthorizationSeed.measures)
        db.badgeDao().insertAll(BadgeSeed.badges)
    }

    // ---------------------------------------------------------------
    // PERFIL
    // ---------------------------------------------------------------

    fun observeProfile(): Flow<UserProfile?> = db.userProfileDao().observeCurrentProfile()

    suspend fun getProfile(): UserProfile? = db.userProfileDao().getCurrentProfile()

    suspend fun createProfile(alias: String, avatarKey: String): UserProfile {
        val profile = UserProfile(alias = alias, avatarKey = avatarKey, createdAt = System.currentTimeMillis())
        val id = db.userProfileDao().insert(profile)
        initializeProgressForNewUser(id)
        return profile.copy(id = id)
    }

    suspend fun completeOnboarding(userId: Long) {
        db.userProfileDao().getCurrentProfile()?.let {
            db.userProfileDao().update(it.copy(onboardingCompleted = true))
        }
    }

    suspend fun updatePreferences(soundEnabled: Boolean, hapticEnabled: Boolean) {
        db.userProfileDao().getCurrentProfile()?.let {
            db.userProfileDao().update(it.copy(soundEnabled = soundEnabled, hapticEnabled = hapticEnabled))
        }
    }

    /** Crea el progreso inicial (todo en 0) para cada zona y desbloquea la primera zona. */
    private suspend fun initializeProgressForNewUser(userId: Long) {
        val zones = db.zoneDao().getAll()
        for (zone in zones) {
            val totalChallenges = db.challengeDao().getForZone(zone.id).size
            val totalRestorations = db.restorationDao().getForZone(zone.id).size
            val totalAuthorizations = db.authorizationDao().getForZone(zone.id).size
            db.progressDao().insert(
                Progress(
                    userId = userId, zoneId = zone.id,
                    totalChallenges = totalChallenges,
                    totalRestorationMissions = totalRestorations,
                    totalAuthorizations = totalAuthorizations,
                    status = ZoneStatus.LOCKED.name
                )
            )
        }
        unlockEligibleZones(userId)
    }

    // ---------------------------------------------------------------
    // MAPA / ZONAS
    // ---------------------------------------------------------------

    data class ZoneOverview(val zone: ZoneInfo, val progress: ZoneProgress)

    suspend fun getZonesOverview(userId: Long): List<ZoneOverview> {
        val zones = db.zoneDao().getAll()
        val unlockedIds = db.unlockedZoneDao().getForUser(userId).map { it.zoneId }.toSet()
        return zones.map { zone ->
            val p = db.progressDao().getForUserAndZone(userId, zone.id)
            val zp = toZoneProgress(p, zone.id)
            val status = progressEngine.computeStatus(zp, zone.id in unlockedIds)
            ZoneOverview(zone.toDomain(), zp.copy(status = status))
        }
    }

    suspend fun getZone(zoneId: Long): ZoneInfo? = db.zoneDao().getById(zoneId)?.toDomain()

    private fun toZoneProgress(p: Progress?, zoneId: Long): ZoneProgress = ZoneProgress(
        zoneId = zoneId,
        status = p?.status?.let { ZoneStatus.valueOf(it) } ?: ZoneStatus.LOCKED,
        challengesCompleted = p?.challengesCompleted ?: 0,
        totalChallenges = p?.totalChallenges ?: 0,
        restorationCompleted = p?.restorationCompleted ?: 0,
        totalRestorationMissions = p?.totalRestorationMissions ?: 0,
        authorizationsCorrect = p?.authorizationsCorrect ?: 0,
        totalAuthorizations = p?.totalAuthorizations ?: 0,
        xp = p?.xp ?: 0
    )

    private suspend fun unlockEligibleZones(userId: Long) {
        val zones = db.zoneDao().getAll()
        val badgeCount = db.badgeDao().countUserBadges(userId)
        val unlockedIds = db.unlockedZoneDao().getForUser(userId).map { it.zoneId }.toSet()
        for (zone in zones) {
            if (zone.id !in unlockedIds && progressEngine.canUnlockZone(zone.toDomain(), badgeCount)) {
                db.unlockedZoneDao().unlock(UnlockedZone(userId = userId, zoneId = zone.id, unlockedAt = System.currentTimeMillis()))
                val p = db.progressDao().getForUserAndZone(userId, zone.id)
                if (p != null && p.status == ZoneStatus.LOCKED.name) {
                    db.progressDao().update(p.copy(status = ZoneStatus.AVAILABLE.name))
                }
            }
        }
    }

    // ---------------------------------------------------------------
    // ESCENARIOS / DETECTIVE VERDE
    // ---------------------------------------------------------------

    suspend fun getScenariosForZone(zoneId: Long): List<ScenarioInfo> =
        db.scenarioDao().getForZone(zoneId).map { it.toDomain() }

    suspend fun getIssuesForScenario(scenarioId: Long): List<IssueInfo> =
        db.issueDao().getForScenario(scenarioId).map { it.toDomain() }

    fun sortIssuesBySeverity(issues: List<IssueInfo>) = scenarioEngine.issuesBySeverity(issues)

    fun discoveryProgress(issues: List<IssueInfo>, foundIds: Set<Long>) =
        scenarioEngine.discoveryProgress(issues, foundIds)

    // ---------------------------------------------------------------
    // RETOS (DECISION / SEMAFORO / ORDER_STEPS / DETECTIVE_FIND)
    // ---------------------------------------------------------------

    suspend fun getChallengesForScenario(scenarioId: Long): List<ChallengeInfo> =
        db.challengeDao().getForScenario(scenarioId).map { it.toDomain() }

    suspend fun getChallenge(id: Long): ChallengeInfo? = db.challengeDao().getById(id)?.toDomain()

    suspend fun getDecisionsForChallenge(challengeId: Long): List<DecisionOption> =
        db.decisionDao().getForChallenge(challengeId).map { it.toDomain() }

    /** Evalúa la decisión elegida por el niño y persiste el intento. */
    suspend fun submitDecision(userId: Long, challenge: ChallengeInfo, chosen: DecisionOption): DecisionEvaluation {
        val outcome = db.decisionDao().getOutcomeForDecision(chosen.id)
        val consequence = outcome?.consequenceId?.let { db.decisionDao().getConsequence(it)?.toDomain() }
        val priorAttempts = db.challengeDao().getAttempts(challenge.id, userId).size

        val evaluation = decisionEngine.evaluate(
            option = chosen,
            challengeXp = challenge.xpReward,
            explanationText = outcome?.explanationText ?: "¡Gracias por decidir con responsabilidad!",
            consequence = consequence,
            priorAttemptsForChallenge = priorAttempts
        )

        registerChallengeCompletion(
            userId = userId,
            challenge = challenge,
            success = evaluation.tier == OutcomeTier.CORRECT,
            partial = evaluation.tier == OutcomeTier.PARTIAL,
            xpAwarded = evaluation.xpAwarded
        )
        return evaluation
    }

    /** Registra el resultado de un reto (de cualquier tipo) y actualiza progreso + insignias. */
    suspend fun registerChallengeCompletion(userId: Long, challenge: ChallengeInfo, success: Boolean, partial: Boolean, xpAwarded: Int) {
        val attemptNumber = db.challengeDao().getAttempts(challenge.id, userId).size + 1
        db.challengeDao().insertAttempt(
            ChallengeAttempt(
                challengeId = challenge.id, userId = userId,
                success = success, partial = partial,
                attemptNumber = attemptNumber, timestamp = System.currentTimeMillis()
            )
        )

        if (success || partial) {
            val scenario = db.scenarioDao().getById(challenge.scenarioId)
            val zoneId = scenario?.zoneId
            if (zoneId != null) {
                val p = db.progressDao().getForUserAndZone(userId, zoneId)
                if (p != null) {
                    val alreadyCompleted = db.challengeDao().getAttempts(challenge.id, userId).any { it.success || it.partial }
                    val newCompleted = if (attemptNumber == 1 || !alreadyCompletedBefore(userId, challenge.id, attemptNumber)) {
                        (p.challengesCompleted + 1).coerceAtMost(p.totalChallenges)
                    } else p.challengesCompleted
                    db.progressDao().update(p.copy(challengesCompleted = newCompleted, xp = p.xp + xpAwarded))
                }
            }
        }
        evaluateAndAwardBadges(userId)
        unlockEligibleZones(userId)
    }

    private suspend fun alreadyCompletedBefore(userId: Long, challengeId: Long, currentAttemptNumber: Int): Boolean {
        val attempts = db.challengeDao().getAttempts(challengeId, userId)
        return attempts.any { it.attemptNumber < currentAttemptNumber && (it.success || it.partial) }
    }

    /** Retos fallidos recientes, para armar la actividad de repaso (sección 20). */
    suspend fun getReviewChallenges(userId: Long, limit: Int = 5): List<ChallengeInfo> {
        val failed = db.challengeDao().getRecentFailedAttempts(userId, limit)
        return failed.mapNotNull { db.challengeDao().getById(it.challengeId)?.toDomain() }.distinctBy { it.id }
    }

    // ---------------------------------------------------------------
    // REPARAR EL ENTORNO
    // ---------------------------------------------------------------

    suspend fun getRestorationMissionsForZone(zoneId: Long): List<RestorationMissionInfo> =
        db.restorationDao().getForZone(zoneId).map { m ->
            m.toDomain(db.restorationDao().getStepsForMission(m.id))
        }

    suspend fun getRestorationMission(id: Long): RestorationMissionInfo? =
        db.restorationDao().getById(id)?.let { m -> m.toDomain(db.restorationDao().getStepsForMission(m.id)) }

    fun validateStepPlacement(step: RestorationStepInfo, droppedItemKey: String, targetSlotKey: String) =
        restorationEngine.validatePlacement(step, droppedItemKey, targetSlotKey)

    fun isMissionComplete(mission: RestorationMissionInfo, completedStepIds: Set<Long>) =
        restorationEngine.isMissionComplete(mission, completedStepIds)

    suspend fun completeRestorationMission(userId: Long, mission: RestorationMissionInfo) {
        val p = db.progressDao().getForUserAndZone(userId, mission.zoneId)
        if (p != null) {
            db.progressDao().update(
                p.copy(
                    restorationCompleted = (p.restorationCompleted + 1).coerceAtMost(p.totalRestorationMissions),
                    xp = p.xp + mission.xpReward
                )
            )
        }
        mission.badgeIdOnComplete?.let { badgeId ->
            val badge = db.badgeDao().getAll().firstOrNull { it.id == badgeId }
            if (badge != null && db.badgeDao().hasBadge(userId, badgeId) == 0) {
                db.badgeDao().awardBadge(UserBadge(userId = userId, badgeId = badgeId, earnedAt = System.currentTimeMillis()))
            }
        }
        evaluateAndAwardBadges(userId)
        unlockEligibleZones(userId)
    }

    // ---------------------------------------------------------------
    // RUTA DE LA AUTORIZACIÓN
    // ---------------------------------------------------------------

    suspend fun getAuthorizationActivitiesForZone(zoneId: Long): List<AuthorizationActivityInfo> =
        db.authorizationDao().getForZone(zoneId).map { a ->
            a.toDomain(db.authorizationDao().getImpacts(a.id), db.authorizationDao().getMeasures(a.id))
        }

    suspend fun getAuthorizationActivity(id: Long): AuthorizationActivityInfo? =
        db.authorizationDao().getById(id)?.let { a ->
            a.toDomain(db.authorizationDao().getImpacts(a.id), db.authorizationDao().getMeasures(a.id))
        }

    suspend fun submitAuthorizationDecision(
        userId: Long,
        activity: AuthorizationActivityInfo,
        userChoice: AuthorizationChoice,
        selectedMeasures: List<MeasureInfo>
    ): AuthorizationOutcome {
        val outcome = authorizationEngine.evaluateUserChoice(userChoice, activity.impacts, selectedMeasures)

        db.authorizationDao().insertDecision(
            AuthorizationDecision(
                authorizationActivityId = activity.id, userId = userId,
                choice = userChoice.name, isCorrect = outcome.userChoiceIsCorrect,
                timestamp = System.currentTimeMillis()
            )
        )

        if (outcome.userChoiceIsCorrect) {
            val p = db.progressDao().getForUserAndZone(userId, activity.zoneId)
            if (p != null) {
                db.progressDao().update(p.copy(authorizationsCorrect = (p.authorizationsCorrect + 1).coerceAtMost(p.totalAuthorizations)))
            }
        }
        evaluateAndAwardBadges(userId)
        return outcome
    }

    // ---------------------------------------------------------------
    // INSIGNIAS Y RECOMPENSAS
    // ---------------------------------------------------------------

    data class BadgeOverview(val badge: BadgeInfo, val earned: Boolean, val earnedAt: Long?)

    suspend fun getBadgesOverview(userId: Long): List<BadgeOverview> {
        val all = db.badgeDao().getAll()
        val earned = db.badgeDao().getUserBadges(userId).associateBy { it.badgeId }
        return all.map { b -> BadgeOverview(b.toDomain(), b.id in earned, earned[b.id]?.earnedAt) }
    }

    suspend fun getTotalXp(userId: Long): Int = db.progressDao().getTotalXp(userId)

    private suspend fun evaluateAndAwardBadges(userId: Long) {
        val zones = db.zoneDao().getAll()
        val progressList = db.progressDao().getForUser(userId)
        val masteredCodes = zones.filter { z ->
            progressList.firstOrNull { it.zoneId == z.id }?.status == ZoneStatus.MASTERED.name
        }.map { it.code }.toSet()

        val totalChallenges = db.challengeDao().countDistinctChallengesCompleted(userId)
        val totalRestorations = progressList.sumOf { it.restorationCompleted }
        val totalAuthCorrect = progressList.sumOf { it.authorizationsCorrect }
        val unlockedCount = db.unlockedZoneDao().getForUser(userId).size

        val context = RewardContext(
            totalChallengesCompleted = totalChallenges,
            totalRestorationsCompleted = totalRestorations,
            totalAuthorizationsCorrect = totalAuthCorrect,
            zonesMasteredCodes = masteredCodes,
            zonesUnlockedCount = unlockedCount,
            totalZones = zones.size,
            issuesDiscovered = totalChallenges, // aproximación: retos completados incluyen los de Detective Verde
            currentBadgeCount = db.badgeDao().countUserBadges(userId)
        )

        val catalog = db.badgeDao().getAll().map { it.toDomain() }
        val alreadyEarned = db.badgeDao().getUserBadges(userId).mapNotNull { ub ->
            catalog.firstOrNull { it.id == ub.badgeId }?.code
        }.toSet()

        val newBadges = rewardEngine.evaluateNewBadges(context, catalog, alreadyEarned)
        for (badge in newBadges) {
            db.badgeDao().awardBadge(UserBadge(userId = userId, badgeId = badge.id, earnedAt = System.currentTimeMillis()))
        }

        // Tras recalcular el estado, refrescamos el status de cada zona (COMPLETED/MASTERED).
        refreshZoneStatuses(userId)
    }

    private suspend fun refreshZoneStatuses(userId: Long) {
        val zones = db.zoneDao().getAll()
        val unlockedIds = db.unlockedZoneDao().getForUser(userId).map { it.zoneId }.toSet()
        for (zone in zones) {
            val p = db.progressDao().getForUserAndZone(userId, zone.id) ?: continue
            val zp = toZoneProgress(p, zone.id)
            val status = progressEngine.computeStatus(zp, zone.id in unlockedIds)
            if (status.name != p.status) {
                db.progressDao().update(p.copy(status = status.name))
            }
        }
    }
}
