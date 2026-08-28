package com.educalab.verdelegal.domain.engine

import com.educalab.verdelegal.data.local.entity.SeverityLevel
import com.educalab.verdelegal.domain.model.ConsequenceInfo
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ConsequenceEngineTest {

    private lateinit var engine: ConsequenceEngine

    @Before
    fun setUp() { engine = ConsequenceEngine() }

    private fun consequence(severity: SeverityLevel, missionId: Long? = null) =
        ConsequenceInfo(1, "desc", severity, "visual", missionId)

    @Test
    fun `suggested restoration mission id passes through from consequence`() {
        assertEquals(42L, engine.suggestedRestorationMissionId(consequence(SeverityLevel.LOW, 42L)))
    }

    @Test
    fun `suggested restoration mission id can be null`() {
        assertNull(engine.suggestedRestorationMissionId(consequence(SeverityLevel.LOW, null)))
    }

    @Test
    fun `zone risk is low with no recent consequences`() {
        assertEquals(SeverityLevel.LOW, engine.zoneRiskLevel(emptyList()))
    }

    @Test
    fun `zone risk is low with a single medium consequence`() {
        assertEquals(SeverityLevel.LOW, engine.zoneRiskLevel(listOf(consequence(SeverityLevel.MEDIUM))))
    }

    @Test
    fun `zone risk is medium with two medium consequences`() {
        assertEquals(SeverityLevel.MEDIUM, engine.zoneRiskLevel(listOf(consequence(SeverityLevel.MEDIUM), consequence(SeverityLevel.MEDIUM))))
    }

    @Test
    fun `zone risk is medium with a single high consequence`() {
        assertEquals(SeverityLevel.MEDIUM, engine.zoneRiskLevel(listOf(consequence(SeverityLevel.HIGH))))
    }

    @Test
    fun `zone risk is high with two or more high consequences`() {
        assertEquals(SeverityLevel.HIGH, engine.zoneRiskLevel(listOf(consequence(SeverityLevel.HIGH), consequence(SeverityLevel.HIGH))))
    }

    @Test
    fun `cause effect summary joins action and description`() {
        val summary = engine.causeEffectSummary("Dejar basura", consequence(SeverityLevel.MEDIUM))
        assertTrue(summary.contains("Dejar basura"))
        assertTrue(summary.contains("desc"))
    }
}
