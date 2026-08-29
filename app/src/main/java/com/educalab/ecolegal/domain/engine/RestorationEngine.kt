package com.educalab.ecolegal.domain.engine

import com.educalab.ecolegal.domain.model.RestorationMissionInfo
import com.educalab.ecolegal.domain.model.RestorationStepInfo

/**
 * Controla las misiones de reparación (arrastrar/colocar/reconstruir) y la
 * recuperación visual de las zonas.
 */
class RestorationEngine {

    /** Valida si un elemento arrastrado corresponde al slot correcto de un paso. */
    fun validatePlacement(step: RestorationStepInfo, droppedItemKey: String, targetSlotKey: String): Boolean =
        step.itemKey == droppedItemKey && step.targetSlotKey == targetSlotKey

    fun missionProgress(mission: RestorationMissionInfo, completedStepIds: Set<Long>): Float {
        if (mission.steps.isEmpty()) return 0f
        return completedStepIds.count { id -> mission.steps.any { it.id == id } }
            .toFloat() / mission.steps.size
    }

    fun isMissionComplete(mission: RestorationMissionInfo, completedStepIds: Set<Long>): Boolean =
        mission.steps.isNotEmpty() && mission.steps.all { it.id in completedStepIds }

    /** Siguiente paso pendiente, en orden, o null si la misión ya está completa. */
    fun nextPendingStep(mission: RestorationMissionInfo, completedStepIds: Set<Long>): RestorationStepInfo? =
        mission.steps.sortedBy { it.order }.firstOrNull { it.id !in completedStepIds }
}
