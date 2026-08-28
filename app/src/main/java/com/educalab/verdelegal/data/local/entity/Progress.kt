package com.educalab.verdelegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Progreso del usuario por zona. Deriva SIEMPRE de acciones reales registradas. */
@Entity(
    tableName = "progress",
    foreignKeys = [
        ForeignKey(entity = UserProfile::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = EnvironmentalZone::class, parentColumns = ["id"], childColumns = ["zoneId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("userId"), Index("zoneId"), Index(value = ["userId", "zoneId"], unique = true)]
)
data class Progress(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val zoneId: Long,
    val challengesCompleted: Int = 0,
    val totalChallenges: Int = 0,
    val restorationCompleted: Int = 0,
    val totalRestorationMissions: Int = 0,
    val authorizationsCorrect: Int = 0,
    val totalAuthorizations: Int = 0,
    val xp: Int = 0,
    val status: String = "LOCKED" // ZoneStatus.name
)
