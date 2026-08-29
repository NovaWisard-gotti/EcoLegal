package com.educalab.ecolegal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ecolegal.data.repository.EcoLegalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class BadgesUiState(
    val loading: Boolean = true,
    val badges: List<EcoLegalRepository.BadgeOverview> = emptyList(),
    val totalXp: Int = 0
)

/** Alimenta la vitrina de insignias y el resumen de progreso del niño. */
class BadgesViewModel(private val repository: EcoLegalRepository, private val userId: Long) : ViewModel() {

    private val _uiState = MutableStateFlow(BadgesUiState())
    val uiState: StateFlow<BadgesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val badges = repository.getBadgesOverview(userId)
            val xp = repository.getTotalXp(userId)
            _uiState.value = BadgesUiState(loading = false, badges = badges, totalXp = xp)
        }
    }
}
