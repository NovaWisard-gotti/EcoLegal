package com.educalab.verdelegal.data.local.seed

import org.junit.Assert.*
import org.junit.Test

/**
 * Verifica que los datos semilla cumplen los mínimos de la especificación
 * (sección "DATOS SEMILLA") y que las relaciones entre tablas son consistentes,
 * sin necesidad de levantar Room ni Android.
 */
class SeedDataIntegrityTest {

    @Test
    fun `there are exactly five zones`() {
        assertEquals(5, ZoneSeed.zones.size)
    }

    @Test
    fun `zone codes are unique and match the five expected zones`() {
        val codes = ZoneSeed.zones.map { it.code }.toSet()
        assertEquals(setOf("BOSQUE", "RIO", "COMUNIDAD", "ANIMALES", "AGRICOLA"), codes)
    }

    @Test
    fun `the first zone requires no badges to unlock`() {
        val first = ZoneSeed.zones.minByOrNull { it.mapOrder }
        assertEquals(0, first?.unlockRequiredBadges)
    }

    @Test
    fun `there are at least twenty environmental scenarios`() {
        assertTrue(ScenarioSeed.scenarios.size >= 20)
    }

    @Test
    fun `there are at least fifteen challenges`() {
        assertTrue(ChallengeSeed.challenges.size >= 15)
    }

    @Test
    fun `challenge types include more than just DECISION`() {
        val types = ChallengeSeed.challenges.map { it.type }.toSet()
        assertTrue(types.size > 1)
        assertTrue(types.contains("DETECTIVE_FIND"))
        assertTrue(types.contains("ORDER_STEPS"))
        assertTrue(types.contains("SEMAFORO"))
    }

    @Test
    fun `no more than half of the challenges are DECISION type`() {
        val decisionCount = ChallengeSeed.challenges.count { it.type == "DECISION" }
        assertTrue(decisionCount.toDouble() / ChallengeSeed.challenges.size <= 0.5)
    }

    @Test
    fun `there are at least twelve decision options across DECISION challenges`() {
        val decisionChallengeIds = ChallengeSeed.challenges.filter { it.type == "DECISION" }.map { it.id }.toSet()
        val optionsForDecisionChallenges = ChallengeSeed.decisions.count { it.challengeId in decisionChallengeIds }
        assertTrue(optionsForDecisionChallenges >= 12)
    }

    @Test
    fun `there are at least ten restoration missions`() {
        assertTrue(RestorationSeed.missions.size >= 10)
    }

    @Test
    fun `there are at least ten authorization activities`() {
        assertTrue(AuthorizationSeed.activities.size >= 10)
    }

    @Test
    fun `there are exactly twelve badges`() {
        assertEquals(12, BadgeSeed.badges.size)
    }

    @Test
    fun `there are eight local avatars and none reference real photos`() {
        assertEquals(8, AvatarSeed.avatars.size)
        assertTrue(AvatarSeed.avatars.all { it.key.startsWith("avatar_") })
    }

    @Test
    fun `every challenge references an existing scenario`() {
        val scenarioIds = ScenarioSeed.scenarios.map { it.id }.toSet()
        for (challenge in ChallengeSeed.challenges) {
            assertTrue("Challenge ${challenge.id} referencia un escenario inexistente", challenge.scenarioId in scenarioIds)
        }
    }

    @Test
    fun `every scenario references an existing zone`() {
        val zoneIds = ZoneSeed.zones.map { it.id }.toSet()
        for (scenario in ScenarioSeed.scenarios) {
            assertTrue("Escenario ${scenario.id} referencia una zona inexistente", scenario.zoneId in zoneIds)
        }
    }

    @Test
    fun `every restoration mission references an existing zone`() {
        val zoneIds = ZoneSeed.zones.map { it.id }.toSet()
        for (mission in RestorationSeed.missions) {
            assertTrue(mission.zoneId in zoneIds)
        }
    }

    @Test
    fun `every authorization activity references an existing zone`() {
        val zoneIds = ZoneSeed.zones.map { it.id }.toSet()
        for (activity in AuthorizationSeed.activities) {
            assertTrue(activity.zoneId in zoneIds)
        }
    }

    @Test
    fun `every decision outcome references an existing decision`() {
        val decisionIds = ChallengeSeed.decisions.map { it.id }.toSet()
        for (outcome in ChallengeSeed.decisionOutcomes) {
            assertTrue(outcome.decisionId in decisionIds)
        }
    }

    @Test
    fun `every restoration step references an existing mission`() {
        val missionIds = RestorationSeed.missions.map { it.id }.toSet()
        for (step in RestorationSeed.steps) {
            assertTrue(step.missionId in missionIds)
        }
    }

    @Test
    fun `all entity ids within each seed table are unique`() {
        assertEquals(ZoneSeed.zones.size, ZoneSeed.zones.map { it.id }.toSet().size)
        assertEquals(ScenarioSeed.scenarios.size, ScenarioSeed.scenarios.map { it.id }.toSet().size)
        assertEquals(ChallengeSeed.challenges.size, ChallengeSeed.challenges.map { it.id }.toSet().size)
        assertEquals(ChallengeSeed.decisions.size, ChallengeSeed.decisions.map { it.id }.toSet().size)
        assertEquals(RestorationSeed.missions.size, RestorationSeed.missions.map { it.id }.toSet().size)
        assertEquals(RestorationSeed.steps.size, RestorationSeed.steps.map { it.id }.toSet().size)
        assertEquals(AuthorizationSeed.activities.size, AuthorizationSeed.activities.map { it.id }.toSet().size)
        assertEquals(BadgeSeed.badges.size, BadgeSeed.badges.map { it.id }.toSet().size)
    }
}
