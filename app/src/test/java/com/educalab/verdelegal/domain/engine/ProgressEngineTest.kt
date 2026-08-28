package com.educalab.verdelegal.domain.engine

import com.educalab.verdelegal.data.local.entity.ZoneStatus
import com.educalab.verdelegal.domain.model.ZoneInfo
import com.educalab.verdelegal.domain.model.ZoneProgress
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ProgressEngineTest {

    private lateinit var engine: ProgressEngine

    @Before
    fun setUp() { engine = ProgressEngine() }

    private fun progress(
        challengesCompleted: Int = 0, totalChallenges: Int = 0,
        restorationCompleted: Int = 0, totalRestoration: Int = 0,
        authCorrect: Int = 0, totalAuth: Int = 0, xp: Int = 0
    ) = ZoneProgress(1, ZoneStatus.LOCKED, challengesCompleted, totalChallenges, restorationCompleted, totalRestoration, authCorrect, totalAuth, xp)

    @Test
    fun `a zone that is not unlocked is always LOCKED`() {
        val p = progress(challengesCompleted = 5, totalChallenges = 5)
        assertEquals(ZoneStatus.LOCKED, engine.computeStatus(p, isUnlocked = false))
    }

    @Test
    fun `an unlocked zone with no progress is AVAILABLE`() {
        val p = progress()
        assertEquals(ZoneStatus.AVAILABLE, engine.computeStatus(p, isUnlocked = true))
    }

    @Test
    fun `an unlocked zone with some progress is STARTED`() {
        val p = progress(challengesCompleted = 1, totalChallenges = 3)
        assertEquals(ZoneStatus.STARTED, engine.computeStatus(p, isUnlocked = true))
    }

    @Test
    fun `a zone with all challenges and restorations done is COMPLETED`() {
        val p = progress(challengesCompleted = 3, totalChallenges = 3, restorationCompleted = 2, totalRestoration = 2)
        assertEquals(ZoneStatus.COMPLETED, engine.computeStatus(p, isUnlocked = true))
    }

    @Test
    fun `a zone with everything done including authorizations is MASTERED`() {
        val p = progress(
            challengesCompleted = 3, totalChallenges = 3,
            restorationCompleted = 2, totalRestoration = 2,
            authCorrect = 2, totalAuth = 2
        )
        assertEquals(ZoneStatus.MASTERED, engine.computeStatus(p, isUnlocked = true))
    }

    @Test
    fun `mastered requires ALL authorizations correct, not just completion`() {
        val p = progress(
            challengesCompleted = 3, totalChallenges = 3,
            restorationCompleted = 2, totalRestoration = 2,
            authCorrect = 1, totalAuth = 2
        )
        assertEquals(ZoneStatus.COMPLETED, engine.computeStatus(p, isUnlocked = true))
    }

    @Test
    fun `zone unlock depends on having enough earned badges`() {
        val zone = ZoneInfo(1, "RIO", "El Río", "desc", 2, 0.5f, 0.5f, unlockRequiredBadges = 1, "icon")
        assertFalse(engine.canUnlockZone(zone, earnedBadgeCount = 0))
        assertTrue(engine.canUnlockZone(zone, earnedBadgeCount = 1))
        assertTrue(engine.canUnlockZone(zone, earnedBadgeCount = 3))
    }

    @Test
    fun `a zone requiring zero badges is always unlockable`() {
        val zone = ZoneInfo(1, "BOSQUE", "El Bosque", "desc", 1, 0.2f, 0.3f, unlockRequiredBadges = 0, "icon")
        assertTrue(engine.canUnlockZone(zone, earnedBadgeCount = 0))
    }

    @Test
    fun `overall completion percent is zero with no progress`() {
        assertEquals(0f, engine.overallCompletionPercent(emptyList()), 0.001f)
    }

    @Test
    fun `overall completion percent averages tasks across zones`() {
        val zones = listOf(
            progress(challengesCompleted = 2, totalChallenges = 2, restorationCompleted = 0, totalRestoration = 0),
            progress(challengesCompleted = 0, totalChallenges = 2, restorationCompleted = 0, totalRestoration = 2)
        )
        // total tasks = 2 + 2 + 2 = 6, done tasks = 2
        assertEquals(2f / 6f, engine.overallCompletionPercent(zones), 0.001f)
    }

    @Test
    fun `total xp sums across all zones`() {
        val zones = listOf(progress(xp = 10), progress(xp = 25))
        assertEquals(35, engine.totalXp(zones))
    }
}
