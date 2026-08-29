package com.educalab.ecolegal.domain.engine

import com.educalab.ecolegal.data.local.seed.BadgeSeed
import com.educalab.ecolegal.domain.model.BadgeInfo
import com.educalab.ecolegal.domain.model.RewardContext
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RewardEngineTest {

    private lateinit var engine: RewardEngine
    private val catalog = BadgeSeed.badges.map { BadgeInfo(it.id, it.code, it.name, it.description, it.iconKey, it.criteriaKey) }

    @Before
    fun setUp() { engine = RewardEngine() }

    private fun emptyContext() = RewardContext(0, 0, 0, emptySet(), 0, 5, 0, 0)

    @Test
    fun `no badges are earned with an empty context`() {
        val earned = engine.evaluateNewBadges(emptyContext(), catalog, emptySet())
        assertTrue(earned.isEmpty())
    }

    @Test
    fun `first challenge completed unlocks Primer Guardian`() {
        val context = emptyContext().copy(totalChallengesCompleted = 1)
        val earned = engine.evaluateNewBadges(context, catalog, emptySet())
        assertTrue(earned.any { it.code == "PRIMER_GUARDIAN" })
    }

    @Test
    fun `explorador responsable requires eight completed challenges`() {
        val below = engine.evaluateNewBadges(emptyContext().copy(totalChallengesCompleted = 7), catalog, emptySet())
        val atThreshold = engine.evaluateNewBadges(emptyContext().copy(totalChallengesCompleted = 8), catalog, emptySet())
        assertFalse(below.any { it.code == "EXPLORADOR_RESPONSABLE" })
        assertTrue(atThreshold.any { it.code == "EXPLORADOR_RESPONSABLE" })
    }

    @Test
    fun `zone mastered badges require the exact zone code`() {
        val context = emptyContext().copy(zonesMasteredCodes = setOf("RIO"))
        val earned = engine.evaluateNewBadges(context, catalog, emptySet())
        assertTrue(earned.any { it.code == "AMIGO_DEL_RIO" })
        assertFalse(earned.any { it.code == "PROTECTOR_DEL_BOSQUE" })
    }

    @Test
    fun `already earned badges are never returned again`() {
        val context = emptyContext().copy(totalChallengesCompleted = 1)
        val earned = engine.evaluateNewBadges(context, catalog, alreadyEarnedCodes = setOf("PRIMER_GUARDIAN"))
        assertFalse(earned.any { it.code == "PRIMER_GUARDIAN" })
    }

    @Test
    fun `restoration badges scale with completed missions`() {
        val threeCompleted = engine.evaluateNewBadges(emptyContext().copy(totalRestorationsCompleted = 3), catalog, emptySet())
        val sixCompleted = engine.evaluateNewBadges(emptyContext().copy(totalRestorationsCompleted = 6), catalog, emptySet())
        assertTrue(threeCompleted.any { it.code == "CONSTRUCTOR_RESPONSABLE" })
        assertFalse(threeCompleted.any { it.code == "RESTAURADOR" })
        assertTrue(sixCompleted.any { it.code == "RESTAURADOR" })
    }

    @Test
    fun `detective badges scale with issues discovered`() {
        val five = engine.evaluateNewBadges(emptyContext().copy(issuesDiscovered = 5), catalog, emptySet())
        val ten = engine.evaluateNewBadges(emptyContext().copy(issuesDiscovered = 10), catalog, emptySet())
        assertTrue(five.any { it.code == "BUEN_OBSERVADOR" })
        assertFalse(five.any { it.code == "DETECTIVE_VERDE" })
        assertTrue(ten.any { it.code == "DETECTIVE_VERDE" })
    }

    @Test
    fun `defensor del valle requires five correct authorizations`() {
        val earned = engine.evaluateNewBadges(emptyContext().copy(totalAuthorizationsCorrect = 5), catalog, emptySet())
        assertTrue(earned.any { it.code == "DEFENSOR_DEL_VALLE" })
    }

    @Test
    fun `gran guardian requires every zone unlocked`() {
        val partial = engine.evaluateNewBadges(emptyContext().copy(zonesUnlockedCount = 4, totalZones = 5), catalog, emptySet())
        val full = engine.evaluateNewBadges(emptyContext().copy(zonesUnlockedCount = 5, totalZones = 5), catalog, emptySet())
        assertFalse(partial.any { it.code == "GRAN_GUARDIAN" })
        assertTrue(full.any { it.code == "GRAN_GUARDIAN" })
    }

    @Test
    fun `maestro ecolegal requires every zone mastered`() {
        val allCodes = setOf("BOSQUE", "RIO", "COMUNIDAD", "ANIMALES", "AGRICOLA")
        val context = emptyContext().copy(zonesMasteredCodes = allCodes, totalZones = 5)
        val earned = engine.evaluateNewBadges(context, catalog, emptySet())
        assertTrue(earned.any { it.code == "MAESTRO_VERDELEGAL" })
    }

    @Test
    fun `all twelve badge codes are unique in the seed catalog`() {
        val codes = BadgeSeed.badges.map { it.code }
        assertEquals(12, codes.size)
        assertEquals(12, codes.toSet().size)
    }

    @Test
    fun `a rich context can unlock several badges at once`() {
        val context = RewardContext(
            totalChallengesCompleted = 8, totalRestorationsCompleted = 6,
            totalAuthorizationsCorrect = 5, zonesMasteredCodes = setOf("RIO"),
            zonesUnlockedCount = 5, totalZones = 5, issuesDiscovered = 10, currentBadgeCount = 0
        )
        val earned = engine.evaluateNewBadges(context, catalog, emptySet())
        assertTrue(earned.size >= 6)
    }
}
