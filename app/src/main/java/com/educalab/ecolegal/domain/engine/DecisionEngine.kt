package com.educalab.ecolegal.domain.engine

import com.educalab.ecolegal.domain.model.ConsequenceInfo
import com.educalab.ecolegal.domain.model.DecisionEvaluation
import com.educalab.ecolegal.domain.model.DecisionOption
import com.educalab.ecolegal.domain.model.OutcomeTier

/**
 * Evalúa las decisiones que toma el niño ante una situación ambiental.
 * Las decisiones NO siempre son binarias correcto/incorrecto: existe un nivel
 * PARTIAL (parcialmente correcta) que otorga la mitad del XP y una explicación
 * distinta, como pide la especificación.
 */
class DecisionEngine {

    fun evaluate(
        option: DecisionOption,
        challengeXp: Int,
        explanationText: String,
        consequence: ConsequenceInfo?,
        priorAttemptsForChallenge: Int
    ): DecisionEvaluation {
        val tier = when {
            option.isCorrect -> OutcomeTier.CORRECT
            option.isPartial -> OutcomeTier.PARTIAL
            else -> OutcomeTier.INCORRECT
        }

        val xp = when (tier) {
            OutcomeTier.CORRECT -> challengeXp
            OutcomeTier.PARTIAL -> (challengeXp / 2).coerceAtLeast(1)
            OutcomeTier.INCORRECT -> 0
        }

        // Tras dos intentos fallidos en el mismo reto, se sugiere repaso (sección 20).
        val offerReview = tier == OutcomeTier.INCORRECT && priorAttemptsForChallenge >= 1

        return DecisionEvaluation(
            tier = tier,
            xpAwarded = xp,
            explanation = explanationText,
            consequence = if (tier != OutcomeTier.CORRECT) consequence else null,
            shouldOfferReview = offerReview
        )
    }

    /** Determina si el conjunto de opciones de un reto es válido (regla de datos, no UI). */
    fun isValidOptionSet(options: List<DecisionOption>): Boolean {
        if (options.size < 2) return false
        val correctCount = options.count { it.isCorrect }
        return correctCount == 1
    }
}
