package com.educalab.ecolegal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ecolegal.data.local.entity.UserProfile
import com.educalab.ecolegal.data.repository.EcoLegalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface AppStartState {
    data object Loading : AppStartState
    data object NeedsProfile : AppStartState
    data class Ready(val profile: UserProfile) : AppStartState
}

/** Estado global: perfil actual, onboarding, y sembrado inicial de datos. */
class AppViewModel(private val repository: EcoLegalRepository) : ViewModel() {

    private val _startState = MutableStateFlow<AppStartState>(AppStartState.Loading)
    val startState: StateFlow<AppStartState> = _startState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
            val profile = repository.getProfile()
            _startState.value = if (profile == null) AppStartState.NeedsProfile else AppStartState.Ready(profile)
        }
    }

    fun createProfile(alias: String, avatarKey: String) {
        viewModelScope.launch {
            val profile = repository.createProfile(alias.ifBlank { "Guardián" }, avatarKey)
            _startState.value = AppStartState.Ready(profile)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            val current = _startState.value
            if (current is AppStartState.Ready) {
                repository.completeOnboarding(current.profile.id)
                _startState.value = AppStartState.Ready(current.profile.copy(onboardingCompleted = true))
            }
        }
    }

    fun updatePreferences(soundEnabled: Boolean, hapticEnabled: Boolean) {
        viewModelScope.launch {
            repository.updatePreferences(soundEnabled, hapticEnabled)
            val current = _startState.value
            if (current is AppStartState.Ready) {
                _startState.value = AppStartState.Ready(current.profile.copy(soundEnabled = soundEnabled, hapticEnabled = hapticEnabled))
            }
        }
    }
}
