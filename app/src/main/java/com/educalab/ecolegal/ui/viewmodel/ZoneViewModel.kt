package com.educalab.ecolegal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ecolegal.data.repository.EcoLegalRepository
import com.educalab.ecolegal.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ScenarioWithChallenges(val scenario: ScenarioInfo, val challenges: List<ChallengeInfo>)

data class ZoneUiState(
    val loading: Boolean = true,
    val zone: ZoneInfo? = null,
    val progress: ZoneProgress? = null,
    val scenarios: List<ScenarioWithChallenges> = emptyList(),
    val restorationMissions: List<RestorationMissionInfo> = emptyList(),
    val authorizationActivities: List<AuthorizationActivityInfo> = emptyList(),
    val completedChallengeIds: Set<Long> = emptySet()
)

/** Alimenta la pantalla "hub" de una zona: sus escenas, retos, reparación y autorización. */
class ZoneViewModel(
    private val repository: EcoLegalRepository,
    private val userId: Long,
    private val zoneId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(ZoneUiState())
    val uiState: StateFlow<ZoneUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            val zone = repository.getZone(zoneId)
            val overview = repository.getZonesOverview(userId).firstOrNull { it.zone.id == zoneId }
            val scenarios = repository.getScenariosForZone(zoneId).map { sc ->
                ScenarioWithChallenges(sc, repository.getChallengesForScenario(sc.id))
            }
            val restoration = repository.getRestorationMissionsForZone(zoneId)
            val authorization = repository.getAuthorizationActivitiesForZone(zoneId)

            _uiState.value = ZoneUiState(
                loading = false,
                zone = zone,
                progress = overview?.progress,
                scenarios = scenarios,
                restorationMissions = restoration,
                authorizationActivities = authorization
            )
        }
    }
}
