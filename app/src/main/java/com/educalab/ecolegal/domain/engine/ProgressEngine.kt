package com.educalab.ecolegal.domain.engine

import com.educalab.ecolegal.data.local.entity.ZoneStatus
import com.educalab.ecolegal.domain.model.ZoneInfo
import com.educalab.ecolegal.domain.model.ZoneProgress

/**
 * Deriva el estado visual y numérico del progreso SIEMPRE a partir de acciones
 * realizadas (retos completados, restauraciones, autorizaciones correctas),
 * nunca de un valor asignado arbitrariamente.
 */
class ProgressEngine {

    fun computeStatus(progress: ZoneProgress, isUnlocked: Boolean): ZoneStatus = when {
        !isUnlocked -> ZoneStatus.LOCKED
        progress.isMastered -> ZoneStatus.MASTERED
        progress.isFullyComplete -> ZoneStatus.COMPLETED
        progress.challengesCompleted > 0 || progress.restorationCompleted > 0 -> ZoneStatus.STARTED
        else -> ZoneStatus.AVAILABLE
    }

    fun canUnlockZone(zone: ZoneInfo, earnedBadgeCount: Int): Boolean =
        earnedBadgeCount >= zone.unlockRequiredBadges

    fun overallCompletionPercent(progressList: List<ZoneProgress>): Float {
        if (progressList.isEmpty()) return 0f
        val totalTasks = progressList.sumOf { it.totalChallenges + it.totalRestorationMissions }
        if (totalTasks == 0) return 0f
        val doneTasks = progressList.sumOf { it.challengesCompleted + it.restorationCompleted }
        return (doneTasks.toFloat() / totalTasks).coerceIn(0f, 1f)
    }

    fun totalXp(progressList: List<ZoneProgress>): Int = progressList.sumOf { it.xp }
}
