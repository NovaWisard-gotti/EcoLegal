package com.educalab.verdelegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Registro de que una zona fue desbloqueada realmente para un usuario. */
@Entity(
    tableName = "unlocked_zone",
    foreignKeys = [
        ForeignKey(entity = UserProfile::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = EnvironmentalZone::class, parentColumns = ["id"], childColumns = ["zoneId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("userId"), Index("zoneId"), Index(value = ["userId", "zoneId"], unique = true)]
)
data class UnlockedZone(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val zoneId: Long,
    val unlockedAt: Long
)
