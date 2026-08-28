package com.educalab.verdelegal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.verdelegal.data.repository.VerdeLegalRepository
import com.educalab.verdelegal.domain.model.ChallengeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReviewUiState(val loading: Boolean = true, val challenges: List<ChallengeInfo> = emptyList())

/** Sección 20: "Practicar otra vez", basada en retos donde el niño falló recientemente. */
class ReviewViewModel(private val repository: VerdeLegalRepository, private val userId: Long) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val challenges = repository.getReviewChallenges(userId)
            _uiState.value = ReviewUiState(loading = false, challenges = challenges)
        }
    }
}
