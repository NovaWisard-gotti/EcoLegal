package com.educalab.ecolegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Situación concreta dentro de una zona, presentada por Luma. */
@Entity(
    tableName = "environmental_scenario",
    foreignKeys = [ForeignKey(
        entity = EnvironmentalZone::class,
        parentColumns = ["id"],
        childColumns = ["zoneId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("zoneId")]
)
data class EnvironmentalScenario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val zoneId: Long,
    val title: String,
    val lumaIntro: String,     // línea corta de Luma presentando la situación
    val sceneOrder: Int,
    val backgroundKey: String
)
