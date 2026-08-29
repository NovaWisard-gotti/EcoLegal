package com.educalab.ecolegal.domain.engine

import com.educalab.ecolegal.domain.model.IssueInfo
import com.educalab.ecolegal.domain.model.ScenarioInfo
import com.educalab.ecolegal.data.local.entity.SeverityLevel

/**
 * Controla zonas, escenarios, situaciones y problemas ambientales (Detective Verde).
 * No depende de Android ni de Room: solo modelos de dominio.
 */
class EnvironmentalScenarioEngine {

    /** Devuelve el siguiente escenario no completado, o null si todos están completos. */
    fun nextScenario(scenarios: List<ScenarioInfo>, completedIds: Set<Long>): ScenarioInfo? =
        scenarios.sortedBy { it.sceneOrder }.firstOrNull { it.id !in completedIds }

    /** Problemas de una escena ordenados por severidad descendente, para el modo Detective Verde. */
    fun issuesBySeverity(issues: List<IssueInfo>): List<IssueInfo> =
        issues.sortedByDescending { severityWeight(it.severity) }

    /** Marca cuántos de los problemas de la escena ya fueron encontrados por el niño. */
    fun discoveryProgress(issues: List<IssueInfo>, foundIds: Set<Long>): Float {
        if (issues.isEmpty()) return 0f
        return foundIds.count { id -> issues.any { it.id == id } }.toFloat() / issues.size
    }

    fun isSceneFullyExplored(issues: List<IssueInfo>, foundIds: Set<Long>): Boolean =
        issues.isNotEmpty() && issues.all { it.id in foundIds }

    private fun severityWeight(level: SeverityLevel): Int = when (level) {
        SeverityLevel.HIGH -> 3
        SeverityLevel.MEDIUM -> 2
        SeverityLevel.LOW -> 1
    }
}
