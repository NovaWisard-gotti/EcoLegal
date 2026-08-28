package com.educalab.verdelegal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.verdelegal.data.repository.VerdeLegalRepository
import com.educalab.verdelegal.domain.model.RestorationMissionInfo
import com.educalab.verdelegal.domain.model.RestorationStepInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RestorationUiState(
    val loading: Boolean = true,
    val mission: RestorationMissionInfo? = null,
    val completedStepIds: Set<Long> = emptySet(),
    val lastPlacementCorrect: Boolean? = null,
    val missionComplete: Boolean = false
)

/** Modo "Reparar el entorno": arrastrar y soltar elementos para restaurar una zona. */
class RestorationViewModel(
    private val repository: VerdeLegalRepository,
    private val userId: Long,
    private val missionId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(RestorationUiState())
    val uiState: StateFlow<RestorationUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val mission = repository.getRestorationMission(missionId)
            _uiState.value = RestorationUiState(loading = false, mission = mission)
        }
    }

    /** El niño soltó item [droppedItemKey] sobre el destino [targetSlotKey] para [step]. */
    fun attemptPlacement(step: RestorationStepInfo, droppedItemKey: String, targetSlotKey: String) {
        val mission = _uiState.value.mission ?: return
        val correct = repository.validateStepPlacement(step, droppedItemKey, targetSlotKey)
        val newCompleted = if (correct) _uiState.value.completedStepIds + step.id else _uiState.value.completedStepIds

        _uiState.value = _uiState.value.copy(completedStepIds = newCompleted, lastPlacementCorrect = correct)

        if (correct && repository.isMissionComplete(mission, newCompleted)) {
            viewModelScope.launch {
                repository.completeRestorationMission(userId, mission)
                _uiState.value = _uiState.value.copy(missionComplete = true)
            }
        }
    }

    fun clearPlacementFeedback() {
        _uiState.value = _uiState.value.copy(lastPlacementCorrect = null)
    }
}
