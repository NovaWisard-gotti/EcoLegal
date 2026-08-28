package com.educalab.verdelegal.domain.engine

import com.educalab.verdelegal.data.local.seed.RestorationSeed
import com.educalab.verdelegal.domain.model.RestorationMissionInfo
import com.educalab.verdelegal.domain.model.RestorationStepInfo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RestorationEngineTest {

    private lateinit var engine: RestorationEngine

    private val steps = listOf(
        RestorationStepInfo(1, 10, "paso 1", 1, "item_a", "slot_a"),
        RestorationStepInfo(2, 10, "paso 2", 2, "item_b", "slot_b"),
        RestorationStepInfo(3, 10, "paso 3", 3, "item_c", "slot_c")
    )
    private val mission = RestorationMissionInfo(10, 1, "Misión de prueba", "desc", 30, null, steps)

    @Before
    fun setUp() { engine = RestorationEngine() }

    @Test
    fun `placement with matching item and slot is valid`() {
        assertTrue(engine.validatePlacement(steps[0], "item_a", "slot_a"))
    }

    @Test
    fun `placement with wrong item is invalid`() {
        assertFalse(engine.validatePlacement(steps[0], "item_b", "slot_a"))
    }

    @Test
    fun `placement with wrong slot is invalid`() {
        assertFalse(engine.validatePlacement(steps[0], "item_a", "slot_b"))
    }

    @Test
    fun `mission progress is zero with no completed steps`() {
        assertEquals(0f, engine.missionProgress(mission, emptySet()), 0.001f)
    }

    @Test
    fun `mission progress is partial with some completed steps`() {
        assertEquals(1f / 3f, engine.missionProgress(mission, setOf(1L)), 0.001f)
    }

    @Test
    fun `mission progress is complete when all steps are done`() {
        assertEquals(1f, engine.missionProgress(mission, setOf(1L, 2L, 3L)), 0.001f)
    }

    @Test
    fun `mission with no steps has zero progress and is never complete`() {
        val empty = mission.copy(steps = emptyList())
        assertEquals(0f, engine.missionProgress(empty, emptySet()), 0.001f)
        assertFalse(engine.isMissionComplete(empty, emptySet()))
    }

    @Test
    fun `mission is not complete until every step id is present`() {
        assertFalse(engine.isMissionComplete(mission, setOf(1L, 2L)))
        assertTrue(engine.isMissionComplete(mission, setOf(1L, 2L, 3L)))
    }

    @Test
    fun `next pending step follows declared order`() {
        assertEquals(1L, engine.nextPendingStep(mission, emptySet())?.id)
        assertEquals(2L, engine.nextPendingStep(mission, setOf(1L))?.id)
        assertNull(engine.nextPendingStep(mission, setOf(1L, 2L, 3L)))
    }

    @Test
    fun `every seeded restoration mission has at least one step`() {
        for (m in RestorationSeed.missions) {
            val stepsForMission = RestorationSeed.steps.filter { it.missionId == m.id }
            assertTrue("Misión ${m.id} debe tener al menos un paso", stepsForMission.isNotEmpty())
        }
    }

    @Test
    fun `every seeded restoration step has a unique order within its mission`() {
        val byMission = RestorationSeed.steps.groupBy { it.missionId }
        for ((missionId, stepsForMission) in byMission) {
            val orders = stepsForMission.map { it.stepOrder }
            assertEquals("Misión $missionId tiene pasos con orden duplicado", orders.size, orders.toSet().size)
        }
    }
}
