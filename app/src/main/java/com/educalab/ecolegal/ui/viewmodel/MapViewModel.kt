package com.educalab.ecolegal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ecolegal.data.repository.EcoLegalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MapUiState(
    val loading: Boolean = true,
    val zones: List<EcoLegalRepository.ZoneOverview> = emptyList(),
    val totalXp: Int = 0,
    val badgeCount: Int = 0
)

/** Alimenta el mapa principal del Valle Verde (pantalla de inicio / dashboard). */
class MapViewModel(private val repository: EcoLegalRepository, private val userId: Long) : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            val zones = repository.getZonesOverview(userId)
            val xp = repository.getTotalXp(userId)
            val badges = repository.getBadgesOverview(userId).count { it.earned }
            _uiState.value = MapUiState(loading = false, zones = zones, totalXp = xp, badgeCount = badges)
        }
    }
}
