package com.educalab.ecolegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Misión de reparación de una zona (arrastrar/colocar/reconstruir). */
@Entity(
    tableName = "restoration_mission",
    foreignKeys = [ForeignKey(
        entity = EnvironmentalZone::class,
        parentColumns = ["id"],
        childColumns = ["zoneId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("zoneId")]
)
data class RestorationMission(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val zoneId: Long,
    val title: String,
    val description: String,
    val xpReward: Int,
    val badgeIdOnComplete: Long? = null
)
