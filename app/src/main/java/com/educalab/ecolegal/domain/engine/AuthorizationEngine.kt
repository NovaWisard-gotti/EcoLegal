package com.educalab.ecolegal.domain.engine

import com.educalab.ecolegal.data.local.entity.AuthorizationChoice
import com.educalab.ecolegal.data.local.entity.SeverityLevel
import com.educalab.ecolegal.domain.model.AuthorizationOutcome
import com.educalab.ecolegal.domain.model.ImpactInfo
import com.educalab.ecolegal.domain.model.MeasureInfo

/**
 * "Autorización ambiental simplificada": el niño revisa impactos posibles y medidas
 * de cuidado de una actividad ficticia, y decide AUTORIZAR / SOLICITAR_CAMBIOS / NO_AUTORIZAR.
 *
 * La recomendación NO es un valor fijo leído de la base de datos: se calcula aquí,
 * a partir de los impactos y de las medidas de protección que el niño seleccionó
 * durante la revisión, para que exista lógica real y testeable (regla de la sección 38).
 */
class AuthorizationEngine {

    fun computeRecommendation(
        impacts: List<ImpactInfo>,
        selectedMeasures: List<MeasureInfo>
    ): AuthorizationChoice {
        val hasHighImpact = impacts.any { it.level == SeverityLevel.HIGH }
        val hasMediumImpact = impacts.any { it.level == SeverityLevel.MEDIUM }
        val recommendedMeasuresChosen = selectedMeasures.count { it.isRecommended }

        return when {
            hasHighImpact && recommendedMeasuresChosen == 0 -> AuthorizationChoice.NO_AUTORIZAR
            hasHighImpact && recommendedMeasuresChosen > 0 -> AuthorizationChoice.SOLICITAR_CAMBIOS
            hasMediumImpact && recommendedMeasuresChosen == 0 -> AuthorizationChoice.SOLICITAR_CAMBIOS
            else -> AuthorizationChoice.AUTORIZAR
        }
    }

    fun evaluateUserChoice(
        userChoice: AuthorizationChoice,
        impacts: List<ImpactInfo>,
        selectedMeasures: List<MeasureInfo>
    ): AuthorizationOutcome {
        val recommended = computeRecommendation(impacts, selectedMeasures)
        val isCorrect = userChoice == recommended
        val explanation = buildExplanation(recommended, impacts)
        return AuthorizationOutcome(recommended, isCorrect, explanation)
    }

    private fun buildExplanation(choice: AuthorizationChoice, impacts: List<ImpactInfo>): String {
        val worst = impacts.maxByOrNull {
            when (it.level) { SeverityLevel.HIGH -> 3; SeverityLevel.MEDIUM -> 2; SeverityLevel.LOW -> 1 }
        }
        return when (choice) {
            AuthorizationChoice.NO_AUTORIZAR ->
                "Esta actividad podría causar un daño importante" +
                    (worst?.let { ": ${it.text}" } ?: "") + ". Es mejor no autorizarla así."
            AuthorizationChoice.SOLICITAR_CAMBIOS ->
                "La actividad puede afectar el lugar, pero con algunos cambios de cuidado podría realizarse de forma más segura."
            AuthorizationChoice.AUTORIZAR ->
                "Los posibles impactos son bajos y se consideraron medidas de cuidado. La actividad puede autorizarse."
        }
    }
}
