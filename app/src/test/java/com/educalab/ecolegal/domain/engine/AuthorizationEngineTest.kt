package com.educalab.ecolegal.domain.engine

import com.educalab.ecolegal.data.local.entity.AuthorizationChoice
import com.educalab.ecolegal.data.local.entity.SeverityLevel
import com.educalab.ecolegal.data.local.seed.AuthorizationSeed
import com.educalab.ecolegal.domain.model.ImpactInfo
import com.educalab.ecolegal.domain.model.MeasureInfo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthorizationEngineTest {

    private lateinit var engine: AuthorizationEngine

    @Before
    fun setUp() { engine = AuthorizationEngine() }

    private val recommendedMeasure = MeasureInfo(1, "medida recomendada", isRecommended = true)
    private val nonRecommendedMeasure = MeasureInfo(2, "medida no recomendada", isRecommended = false)

    @Test
    fun `high impact with no protective measures should not be authorized`() {
        val recommendation = engine.computeRecommendation(listOf(ImpactInfo("daño grave", SeverityLevel.HIGH)), emptyList())
        assertEquals(AuthorizationChoice.NO_AUTORIZAR, recommendation)
    }

    @Test
    fun `high impact with a protective measure asks for changes`() {
        val recommendation = engine.computeRecommendation(listOf(ImpactInfo("daño grave", SeverityLevel.HIGH)), listOf(recommendedMeasure))
        assertEquals(AuthorizationChoice.SOLICITAR_CAMBIOS, recommendation)
    }

    @Test
    fun `medium impact with no measures asks for changes`() {
        val recommendation = engine.computeRecommendation(listOf(ImpactInfo("impacto moderado", SeverityLevel.MEDIUM)), emptyList())
        assertEquals(AuthorizationChoice.SOLICITAR_CAMBIOS, recommendation)
    }

    @Test
    fun `medium impact with a recommended measure can be authorized`() {
        val recommendation = engine.computeRecommendation(listOf(ImpactInfo("impacto moderado", SeverityLevel.MEDIUM)), listOf(recommendedMeasure))
        assertEquals(AuthorizationChoice.AUTORIZAR, recommendation)
    }

    @Test
    fun `only low impacts are authorized regardless of measures`() {
        val recommendation = engine.computeRecommendation(listOf(ImpactInfo("impacto bajo", SeverityLevel.LOW)), emptyList())
        assertEquals(AuthorizationChoice.AUTORIZAR, recommendation)
    }

    @Test
    fun `a non-recommended measure does not count toward mitigation`() {
        val recommendation = engine.computeRecommendation(listOf(ImpactInfo("daño grave", SeverityLevel.HIGH)), listOf(nonRecommendedMeasure))
        assertEquals(AuthorizationChoice.NO_AUTORIZAR, recommendation)
    }

    @Test
    fun `user choice matching the recommendation is marked correct`() {
        val impacts = listOf(ImpactInfo("impacto bajo", SeverityLevel.LOW))
        val outcome = engine.evaluateUserChoice(AuthorizationChoice.AUTORIZAR, impacts, emptyList())
        assertTrue(outcome.userChoiceIsCorrect)
    }

    @Test
    fun `user choice not matching the recommendation is marked incorrect`() {
        val impacts = listOf(ImpactInfo("daño grave", SeverityLevel.HIGH))
        val outcome = engine.evaluateUserChoice(AuthorizationChoice.AUTORIZAR, impacts, emptyList())
        assertFalse(outcome.userChoiceIsCorrect)
        assertEquals(AuthorizationChoice.NO_AUTORIZAR, outcome.recommendedChoice)
    }

    @Test
    fun `explanation is never blank`() {
        val impacts = listOf(ImpactInfo("impacto bajo", SeverityLevel.LOW))
        val outcome = engine.evaluateUserChoice(AuthorizationChoice.AUTORIZAR, impacts, emptyList())
        assertTrue(outcome.explanation.isNotBlank())
    }

    @Test
    fun `every seeded authorization activity has at least one recommended measure`() {
        for (activity in AuthorizationSeed.activities) {
            val measures = AuthorizationSeed.measures.filter { it.authorizationActivityId == activity.id }
            assertTrue("Actividad ${activity.id} debe tener al menos una medida recomendada", measures.any { it.isRecommended })
        }
    }

    @Test
    fun `every seeded authorization activity has at least one impact`() {
        for (activity in AuthorizationSeed.activities) {
            val impacts = AuthorizationSeed.impacts.filter { it.authorizationActivityId == activity.id }
            assertTrue("Actividad ${activity.id} debe tener al menos un impacto descrito", impacts.isNotEmpty())
        }
    }
}
