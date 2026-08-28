package com.educalab.verdelegal.domain.engine

import com.educalab.verdelegal.data.local.entity.SeverityLevel
import com.educalab.verdelegal.data.local.seed.ScenarioSeed
import com.educalab.verdelegal.data.local.seed.ZoneSeed
import com.educalab.verdelegal.domain.model.IssueInfo
import com.educalab.verdelegal.domain.model.ScenarioInfo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class EnvironmentalScenarioEngineTest {

    private lateinit var engine: EnvironmentalScenarioEngine

    private val scenarios = listOf(
        ScenarioInfo(1, 1, "Escena 1", "intro", 1, "bg1"),
        ScenarioInfo(2, 1, "Escena 2", "intro", 2, "bg2"),
        ScenarioInfo(3, 1, "Escena 3", "intro", 3, "bg3")
    )

    private val issues = listOf(
        IssueInfo(1, 1, "Bajo", "d", "icon", SeverityLevel.LOW, 0f, 0f),
        IssueInfo(2, 1, "Alto", "d", "icon", SeverityLevel.HIGH, 0f, 0f),
        IssueInfo(3, 1, "Medio", "d", "icon", SeverityLevel.MEDIUM, 0f, 0f)
    )

    @Before
    fun setUp() { engine = EnvironmentalScenarioEngine() }

    @Test
    fun `next scenario is the first not yet completed, in order`() {
        assertEquals(1L, engine.nextScenario(scenarios, emptySet())?.id)
        assertEquals(2L, engine.nextScenario(scenarios, setOf(1L))?.id)
    }

    @Test
    fun `next scenario is null when all are completed`() {
        assertNull(engine.nextScenario(scenarios, setOf(1L, 2L, 3L)))
    }

    @Test
    fun `issues are sorted with the most severe first`() {
        val sorted = engine.issuesBySeverity(issues)
        assertEquals(SeverityLevel.HIGH, sorted.first().severity)
        assertEquals(SeverityLevel.LOW, sorted.last().severity)
    }

    @Test
    fun `discovery progress is zero with nothing found`() {
        assertEquals(0f, engine.discoveryProgress(issues, emptySet()), 0.001f)
    }

    @Test
    fun `discovery progress reflects partial discovery`() {
        assertEquals(1f / 3f, engine.discoveryProgress(issues, setOf(1L)), 0.001f)
    }

    @Test
    fun `discovery progress with an empty issue list is zero`() {
        assertEquals(0f, engine.discoveryProgress(emptyList(), setOf(1L)), 0.001f)
    }

    @Test
    fun `scene is fully explored only when every issue is found`() {
        assertFalse(engine.isSceneFullyExplored(issues, setOf(1L, 2L)))
        assertTrue(engine.isSceneFullyExplored(issues, setOf(1L, 2L, 3L)))
    }

    @Test
    fun `an empty scene is never considered fully explored`() {
        assertFalse(engine.isSceneFullyExplored(emptyList(), emptySet()))
    }

    @Test
    fun `there are exactly twenty seeded environmental scenarios`() {
        assertEquals(20, ScenarioSeed.scenarios.size)
    }

    @Test
    fun `scenarios are distributed across all five zones`() {
        val zoneIds = setOf(ZoneSeed.ID_BOSQUE, ZoneSeed.ID_RIO, ZoneSeed.ID_COMUNIDAD, ZoneSeed.ID_ANIMALES, ZoneSeed.ID_AGRICOLA)
        for (zoneId in zoneIds) {
            val count = ScenarioSeed.scenarios.count { it.zoneId == zoneId }
            assertEquals("La zona $zoneId debe tener 4 escenarios", 4, count)
        }
    }
}
