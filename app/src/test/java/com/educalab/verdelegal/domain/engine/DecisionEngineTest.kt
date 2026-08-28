package com.educalab.verdelegal.domain.engine

import com.educalab.verdelegal.data.local.entity.SeverityLevel
import com.educalab.verdelegal.data.local.seed.ChallengeSeed
import com.educalab.verdelegal.domain.model.ConsequenceInfo
import com.educalab.verdelegal.domain.model.DecisionOption
import com.educalab.verdelegal.domain.model.OutcomeTier
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DecisionEngineTest {

    private lateinit var engine: DecisionEngine

    @Before
    fun setUp() {
        engine = DecisionEngine()
    }

    private val sampleConsequence = ConsequenceInfo(1, "Algo se ve afectado", SeverityLevel.MEDIUM, "visual", null)

    @Test
    fun `correct option yields CORRECT tier and full xp`() {
        val option = DecisionOption(1, 100, "texto", isCorrect = true, isPartial = false, order = 1)
        val result = engine.evaluate(option, challengeXp = 20, explanationText = "bien", consequence = sampleConsequence, priorAttemptsForChallenge = 0)
        assertEquals(OutcomeTier.CORRECT, result.tier)
        assertEquals(20, result.xpAwarded)
    }

    @Test
    fun `correct option never carries a consequence`() {
        val option = DecisionOption(1, 100, "texto", isCorrect = true, isPartial = false, order = 1)
        val result = engine.evaluate(option, challengeXp = 20, explanationText = "bien", consequence = sampleConsequence, priorAttemptsForChallenge = 0)
        assertNull(result.consequence)
    }

    @Test
    fun `partial option yields half xp rounded down`() {
        val option = DecisionOption(2, 100, "texto", isCorrect = false, isPartial = true, order = 2)
        val result = engine.evaluate(option, challengeXp = 21, explanationText = "parcial", consequence = sampleConsequence, priorAttemptsForChallenge = 0)
        assertEquals(OutcomeTier.PARTIAL, result.tier)
        assertEquals(10, result.xpAwarded)
    }

    @Test
    fun `partial xp never rounds down to zero`() {
        val option = DecisionOption(2, 100, "texto", isCorrect = false, isPartial = true, order = 2)
        val result = engine.evaluate(option, challengeXp = 1, explanationText = "parcial", consequence = null, priorAttemptsForChallenge = 0)
        assertEquals(1, result.xpAwarded)
    }

    @Test
    fun `partial option keeps its consequence`() {
        val option = DecisionOption(2, 100, "texto", isCorrect = false, isPartial = true, order = 2)
        val result = engine.evaluate(option, challengeXp = 20, explanationText = "parcial", consequence = sampleConsequence, priorAttemptsForChallenge = 0)
        assertEquals(sampleConsequence, result.consequence)
    }

    @Test
    fun `incorrect option yields zero xp`() {
        val option = DecisionOption(3, 100, "texto", isCorrect = false, isPartial = false, order = 3)
        val result = engine.evaluate(option, challengeXp = 20, explanationText = "mal", consequence = sampleConsequence, priorAttemptsForChallenge = 0)
        assertEquals(OutcomeTier.INCORRECT, result.tier)
        assertEquals(0, result.xpAwarded)
    }

    @Test
    fun `review is not offered on the first incorrect attempt`() {
        val option = DecisionOption(3, 100, "texto", isCorrect = false, isPartial = false, order = 3)
        val result = engine.evaluate(option, challengeXp = 20, explanationText = "mal", consequence = null, priorAttemptsForChallenge = 0)
        assertFalse(result.shouldOfferReview)
    }

    @Test
    fun `review is offered after a prior failed attempt`() {
        val option = DecisionOption(3, 100, "texto", isCorrect = false, isPartial = false, order = 3)
        val result = engine.evaluate(option, challengeXp = 20, explanationText = "mal", consequence = null, priorAttemptsForChallenge = 1)
        assertTrue(result.shouldOfferReview)
    }

    @Test
    fun `review is never offered for correct or partial outcomes`() {
        val correct = DecisionOption(1, 100, "t", true, false, 1)
        val partial = DecisionOption(2, 100, "t", false, true, 2)
        assertFalse(engine.evaluate(correct, 20, "x", null, 5).shouldOfferReview)
        assertFalse(engine.evaluate(partial, 20, "x", null, 5).shouldOfferReview)
    }

    @Test
    fun `option set with exactly one correct answer is valid`() {
        val options = listOf(
            DecisionOption(1, 100, "a", true, false, 1),
            DecisionOption(2, 100, "b", false, false, 2)
        )
        assertTrue(engine.isValidOptionSet(options))
    }

    @Test
    fun `option set with no correct answer is invalid`() {
        val options = listOf(
            DecisionOption(1, 100, "a", false, false, 1),
            DecisionOption(2, 100, "b", false, true, 2)
        )
        assertFalse(engine.isValidOptionSet(options))
    }

    @Test
    fun `option set with two correct answers is invalid`() {
        val options = listOf(
            DecisionOption(1, 100, "a", true, false, 1),
            DecisionOption(2, 100, "b", true, false, 2)
        )
        assertFalse(engine.isValidOptionSet(options))
    }

    @Test
    fun `option set with fewer than two options is invalid`() {
        val options = listOf(DecisionOption(1, 100, "a", true, false, 1))
        assertFalse(engine.isValidOptionSet(options))
    }

    @Test
    fun `every seeded DECISION challenge has exactly one correct option`() {
        val decisionChallengeIds = ChallengeSeed.challenges.filter { it.type == "DECISION" }.map { it.id }
        for (challengeId in decisionChallengeIds) {
            val options = ChallengeSeed.decisions.filter { it.challengeId == challengeId }
                .map { DecisionOption(it.id, it.challengeId, it.text, it.isCorrect, it.isPartial, it.decisionOrder) }
            assertTrue("Challenge $challengeId debe tener un conjunto de opciones válido", engine.isValidOptionSet(options))
        }
    }
}
