package com.educalab.verdelegal.domain.model

import com.educalab.verdelegal.data.local.entity.AuthorizationChoice
import com.educalab.verdelegal.data.local.entity.ChallengeType
import com.educalab.verdelegal.data.local.entity.SeverityLevel
import com.educalab.verdelegal.data.local.entity.ZoneStatus

/**
 * Modelos de dominio: independientes de Room, usados por los motores (engines)
 * para que la lógica educativa sea 100% testable en JVM puro sin Android.
 */

data class ZoneInfo(
    val id: Long,
    val code: String,
    val displayName: String,
    val shortDescription: String,
    val mapOrder: Int,
    val mapX: Float,
    val mapY: Float,
    val unlockRequiredBadges: Int,
    val iconKey: String
)

data class ScenarioInfo(
    val id: Long,
    val zoneId: Long,
    val title: String,
    val lumaIntro: String,
    val sceneOrder: Int,
    val backgroundKey: String
)

data class IssueInfo(
    val id: Long,
    val scenarioId: Long,
    val title: String,
    val description: String,
    val iconKey: String,
    val severity: SeverityLevel,
    val positionX: Float,
    val positionY: Float
)

data class ChallengeInfo(
    val id: Long,
    val scenarioId: Long,
    val type: ChallengeType,
    val title: String,
    val prompt: String,
    val difficulty: Int,
    val xpReward: Int,
    val order: Int
)

data class DecisionOption(
    val id: Long,
    val challengeId: Long,
    val text: String,
    val isCorrect: Boolean,
    val isPartial: Boolean,
    val order: Int
)

data class ConsequenceInfo(
    val id: Long,
    val description: String,
    val severity: SeverityLevel,
    val visualKey: String,
    val relatedRestorationMissionId: Long?
)

data class DecisionEvaluation(
    val tier: OutcomeTier,
    val xpAwarded: Int,
    val explanation: String,
    val consequence: ConsequenceInfo?,
    val shouldOfferReview: Boolean
)

enum class OutcomeTier { CORRECT, PARTIAL, INCORRECT }

data class RestorationStepInfo(
    val id: Long,
    val missionId: Long,
    val description: String,
    val order: Int,
    val itemKey: String,
    val targetSlotKey: String
)

data class RestorationMissionInfo(
    val id: Long,
    val zoneId: Long,
    val title: String,
    val description: String,
    val xpReward: Int,
    val badgeIdOnComplete: Long?,
    val steps: List<RestorationStepInfo>
)

data class ImpactInfo(val text: String, val level: SeverityLevel)
data class MeasureInfo(val id: Long, val text: String, val isRecommended: Boolean)

data class AuthorizationActivityInfo(
    val id: Long,
    val zoneId: Long,
    val activityName: String,
    val description: String,
    val iconKey: String,
    val authoredCorrectChoice: AuthorizationChoice,
    val impacts: List<ImpactInfo>,
    val measures: List<MeasureInfo>
)

data class AuthorizationOutcome(
    val recommendedChoice: AuthorizationChoice,
    val userChoiceIsCorrect: Boolean,
    val explanation: String
)

data class ZoneProgress(
    val zoneId: Long,
    val status: ZoneStatus,
    val challengesCompleted: Int,
    val totalChallenges: Int,
    val restorationCompleted: Int,
    val totalRestorationMissions: Int,
    val authorizationsCorrect: Int,
    val totalAuthorizations: Int,
    val xp: Int
) {
    val isFullyComplete: Boolean
        get() = totalChallenges > 0 && challengesCompleted >= totalChallenges &&
            restorationCompleted >= totalRestorationMissions

    val isMastered: Boolean
        get() = isFullyComplete && totalAuthorizations > 0 && authorizationsCorrect >= totalAuthorizations
}

data class BadgeInfo(
    val id: Long,
    val code: String,
    val name: String,
    val description: String,
    val iconKey: String,
    val criteriaKey: String
)

/** Agregados reales del jugador, usados por RewardEngine para evaluar insignias. */
data class RewardContext(
    val totalChallengesCompleted: Int,
    val totalRestorationsCompleted: Int,
    val totalAuthorizationsCorrect: Int,
    val zonesMasteredCodes: Set<String>,
    val zonesUnlockedCount: Int,
    val totalZones: Int,
    val issuesDiscovered: Int,
    val currentBadgeCount: Int
)
