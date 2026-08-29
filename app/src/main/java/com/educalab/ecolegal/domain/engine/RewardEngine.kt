package com.educalab.ecolegal.domain.engine

import com.educalab.ecolegal.domain.model.BadgeInfo
import com.educalab.ecolegal.domain.model.RewardContext

/**
 * Determina qué insignias corresponden a un contexto de progreso real (RewardContext).
 *
 * Convención de criteriaKey (ver seed data / BASE_DE_DATOS.md):
 *   "CHALLENGES>=n"        -> n retos completados en total
 *   "RESTORATIONS>=n"      -> n misiones de reparación completadas
 *   "AUTHORIZATIONS>=n"    -> n autorizaciones correctas
 *   "ISSUES>=n"            -> n problemas descubiertos (Detective Verde)
 *   "ZONE_MASTERED:CODE"   -> zona con código CODE dominada
 *   "ALL_ZONES_UNLOCKED"   -> todas las zonas desbloqueadas
 *   "ALL_ZONES_MASTERED"   -> todas las zonas dominadas (insignia final)
 */
class RewardEngine {

    fun evaluateNewBadges(
        context: RewardContext,
        catalog: List<BadgeInfo>,
        alreadyEarnedCodes: Set<String>
    ): List<BadgeInfo> = catalog
        .filterNot { it.code in alreadyEarnedCodes }
        .filter { meetsCriteria(it.criteriaKey, context) }

    private fun meetsCriteria(criteriaKey: String, ctx: RewardContext): Boolean {
        return when {
            criteriaKey.startsWith("CHALLENGES>=") ->
                ctx.totalChallengesCompleted >= criteriaKey.substringAfter(">=").toInt()

            criteriaKey.startsWith("RESTORATIONS>=") ->
                ctx.totalRestorationsCompleted >= criteriaKey.substringAfter(">=").toInt()

            criteriaKey.startsWith("AUTHORIZATIONS>=") ->
                ctx.totalAuthorizationsCorrect >= criteriaKey.substringAfter(">=").toInt()

            criteriaKey.startsWith("ISSUES>=") ->
                ctx.issuesDiscovered >= criteriaKey.substringAfter(">=").toInt()

            criteriaKey.startsWith("ZONE_MASTERED:") ->
                criteriaKey.substringAfter(":") in ctx.zonesMasteredCodes

            criteriaKey == "ALL_ZONES_UNLOCKED" ->
                ctx.zonesUnlockedCount >= ctx.totalZones

            criteriaKey == "ALL_ZONES_MASTERED" ->
                ctx.zonesMasteredCodes.size >= ctx.totalZones

            else -> false
        }
    }
}
