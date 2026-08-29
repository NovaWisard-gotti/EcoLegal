package com.educalab.ecolegal.domain.engine

import com.educalab.ecolegal.data.local.entity.SeverityLevel
import com.educalab.ecolegal.domain.model.ConsequenceInfo

/**
 * Relaciona ACCIÓN -> CONSECUENCIA -> POSIBLE REPARACIÓN, y calcula el "riesgo"
 * acumulado de una zona a partir de las consecuencias observadas recientemente
 * (usado para decidir si el mapa debe mostrar la zona como "necesita ayuda").
 */
class ConsequenceEngine {

    /** Sugiere la misión de reparación asociada a una consecuencia, si existe. */
    fun suggestedRestorationMissionId(consequence: ConsequenceInfo): Long? =
        consequence.relatedRestorationMissionId

    /**
     * Calcula el nivel de riesgo de una zona a partir de las últimas consecuencias
     * observadas por decisiones incorrectas o parciales. Zona sin consecuencias
     * recientes = riesgo bajo.
     */
    fun zoneRiskLevel(recentConsequences: List<ConsequenceInfo>): SeverityLevel {
        if (recentConsequences.isEmpty()) return SeverityLevel.LOW
        val highCount = recentConsequences.count { it.severity == SeverityLevel.HIGH }
        val mediumCount = recentConsequences.count { it.severity == SeverityLevel.MEDIUM }
        return when {
            highCount >= 2 -> SeverityLevel.HIGH
            highCount == 1 || mediumCount >= 2 -> SeverityLevel.MEDIUM
            else -> SeverityLevel.LOW
        }
    }

    /** Texto corto y no alarmante para explicar la relación causa-efecto al niño. */
    fun causeEffectSummary(action: String, consequence: ConsequenceInfo): String =
        "$action → ${consequence.description}"
}
