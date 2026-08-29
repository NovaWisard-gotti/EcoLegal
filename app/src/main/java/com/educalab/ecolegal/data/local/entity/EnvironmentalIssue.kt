package com.educalab.ecolegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Un elemento/problema detectable dentro de una escena (modo Detective Verde). */
@Entity(
    tableName = "environmental_issue",
    foreignKeys = [ForeignKey(
        entity = EnvironmentalScenario::class,
        parentColumns = ["id"],
        childColumns = ["scenarioId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("scenarioId")]
)
data class EnvironmentalIssue(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val scenarioId: Long,
    val title: String,
    val description: String,
    val iconKey: String,
    val severity: String,      // SeverityLevel.name
    val positionX: Float,      // posición dentro de la escena 0..1
    val positionY: Float
)
