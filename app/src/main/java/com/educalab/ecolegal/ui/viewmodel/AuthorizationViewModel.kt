package com.educalab.ecolegal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ecolegal.data.local.entity.AuthorizationChoice
import com.educalab.ecolegal.data.repository.EcoLegalRepository
import com.educalab.ecolegal.domain.model.AuthorizationActivityInfo
import com.educalab.ecolegal.domain.model.AuthorizationOutcome
import com.educalab.ecolegal.domain.model.MeasureInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthorizationUiState(
    val loading: Boolean = true,
    val activity: AuthorizationActivityInfo? = null,
    val selectedMeasureIds: Set<Long> = emptySet(),
    val outcome: AuthorizationOutcome? = null
)

/** Modo "Ruta de la autorización": revisar impactos, elegir medidas y decidir. */
class AuthorizationViewModel(
    private val repository: EcoLegalRepository,
    private val userId: Long,
    private val activityId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthorizationUiState())
    val uiState: StateFlow<AuthorizationUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val activity = repository.getAuthorizationActivity(activityId)
            _uiState.value = AuthorizationUiState(loading = false, activity = activity)
        }
    }

    fun toggleMeasure(measure: MeasureInfo) {
        val current = _uiState.value.selectedMeasureIds
        _uiState.value = _uiState.value.copy(
            selectedMeasureIds = if (measure.id in current) current - measure.id else current + measure.id
        )
    }

    fun decide(choice: AuthorizationChoice) {
        val activity = _uiState.value.activity ?: return
        val selected = activity.measures.filter { it.id in _uiState.value.selectedMeasureIds }
        viewModelScope.launch {
            val outcome = repository.submitAuthorizationDecision(userId, activity, choice, selected)
            _uiState.value = _uiState.value.copy(outcome = outcome)
        }
    }
}
