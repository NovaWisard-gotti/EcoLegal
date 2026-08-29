package com.educalab.ecolegal.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.educalab.ecolegal.data.local.entity.ChallengeType
import com.educalab.ecolegal.data.repository.EcoLegalRepository
import com.educalab.ecolegal.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class SemaforoColor { VERDE, AMARILLO, ROJO }

data class ChallengeUiState(
    val loading: Boolean = true,
    val challenge: ChallengeInfo? = null,
    val options: List<DecisionOption> = emptyList(),
    val issues: List<IssueInfo> = emptyList(),
    val foundIssueIds: Set<Long> = emptySet(),
    val semaforoAssignments: Map<Long, SemaforoColor> = emptyMap(),
    val orderedIds: List<Long> = emptyList(),
    val result: ChallengeResult? = null
)

data class ChallengeResult(
    val success: Boolean,
    val partial: Boolean,
    val explanation: String,
    val xpAwarded: Int,
    val consequence: ConsequenceInfo?
)

class ChallengeViewModel(
    private val repository: EcoLegalRepository,
    private val userId: Long,
    private val challengeId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChallengeUiState())
    val uiState: StateFlow<ChallengeUiState> = _uiState.asStateFlow()

    init { load() }

    private fun load() {
        viewModelScope.launch {
            val challenge = repository.getChallenge(challengeId) ?: return@launch
            val options = repository.getDecisionsForChallenge(challengeId)
            val issues = if (challenge.type == ChallengeType.DETECTIVE_FIND) {
                repository.getIssuesForScenario(challenge.scenarioId)
            } else emptyList()

            _uiState.value = ChallengeUiState(
                loading = false,
                challenge = challenge,
                options = options,
                issues = repository.sortIssuesBySeverity(issues),
                orderedIds = options.shuffled().map { it.id }
            )
        }
    }

    /** DECISION: el niño elige una única alternativa. */
    fun submitSingleDecision(chosen: DecisionOption) {
        val challenge = _uiState.value.challenge ?: return
        viewModelScope.launch {
            val evaluation = repository.submitDecision(userId, challenge, chosen)
            _uiState.value = _uiState.value.copy(
                result = ChallengeResult(
                    success = evaluation.tier == OutcomeTier.CORRECT,
                    partial = evaluation.tier == OutcomeTier.PARTIAL,
                    explanation = evaluation.explanation,
                    xpAwarded = evaluation.xpAwarded,
                    consequence = evaluation.consequence
                )
            )
        }
    }

    /** DETECTIVE_FIND: el niño toca un elemento de la escena. */
    fun markIssueFound(issueId: Long) {
        val state = _uiState.value
        val newFound = state.foundIssueIds + issueId
        _uiState.value = state.copy(foundIssueIds = newFound)
        if (repository.discoveryProgress(state.issues, newFound) >= 1f) {
            finishNonDecisionChallenge(success = true, partial = false)
        }
    }

    /** SEMAFORO: el niño clasifica cada acción como verde/amarillo/rojo. */
    fun assignSemaforo(optionId: Long, color: SemaforoColor) {
        val state = _uiState.value
        val newMap = state.semaforoAssignments + (optionId to color)
        _uiState.value = state.copy(semaforoAssignments = newMap)
        if (newMap.size == state.options.size) {
            val correctCount = state.options.count { opt ->
                val expected = when {
                    opt.isCorrect -> SemaforoColor.VERDE
                    opt.isPartial -> SemaforoColor.AMARILLO
                    else -> SemaforoColor.ROJO
                }
                newMap[opt.id] == expected
            }
            val allCorrect = correctCount == state.options.size
            val someCorrect = correctCount > 0
            finishNonDecisionChallenge(success = allCorrect, partial = !allCorrect && someCorrect)
        }
    }

    /** ORDER_STEPS: el niño reordena los pasos con moveStep, luego confirma. */
    fun moveStep(fromIndex: Int, toIndex: Int) {
        val current = _uiState.value.orderedIds.toMutableList()
        if (fromIndex !in current.indices || toIndex !in current.indices) return
        val item = current.removeAt(fromIndex)
        current.add(toIndex, item)
        _uiState.value = _uiState.value.copy(orderedIds = current)
    }

    fun confirmOrder() {
        val state = _uiState.value
        val correctOrder = state.options.sortedBy { it.order }.map { it.id }
        val success = state.orderedIds == correctOrder
        // Parcial si al menos la mitad de las posiciones coinciden.
        val matchingPositions = state.orderedIds.indices.count { i -> state.orderedIds[i] == correctOrder.getOrNull(i) }
        val partial = !success && matchingPositions >= correctOrder.size / 2
        finishNonDecisionChallenge(success = success, partial = partial)
    }

    private fun finishNonDecisionChallenge(success: Boolean, partial: Boolean) {
        val challenge = _uiState.value.challenge ?: return
        val xp = when {
            success -> challenge.xpReward
            partial -> (challenge.xpReward / 2).coerceAtLeast(1)
            else -> 0
        }
        val explanation = when {
            success -> "¡Excelente trabajo! Completaste este reto correctamente."
            partial -> "¡Buen intento! Algunas partes estuvieron bien, otras necesitan un poco más de cuidado."
            else -> "No te preocupes, cada intento nos ayuda a aprender. ¿Quieres intentarlo de nuevo?"
        }
        viewModelScope.launch {
            repository.registerChallengeCompletion(userId, challenge, success, partial, xp)
            _uiState.value = _uiState.value.copy(
                result = ChallengeResult(success, partial, explanation, xp, null)
            )
        }
    }

    fun retry() {
        _uiState.value = _uiState.value.copy(
            result = null,
            foundIssueIds = emptySet(),
            semaforoAssignments = emptyMap(),
            orderedIds = _uiState.value.options.shuffled().map { it.id }
        )
    }
}
