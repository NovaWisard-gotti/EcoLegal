package com.educalab.verdelegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Registro persistido de cada intento real de un reto (para progreso y repaso). */
@Entity(
    tableName = "challenge_attempt",
    foreignKeys = [
        ForeignKey(entity = Challenge::class, parentColumns = ["id"], childColumns = ["challengeId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = UserProfile::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("challengeId"), Index("userId")]
)
data class ChallengeAttempt(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val challengeId: Long,
    val userId: Long,
    val success: Boolean,
    val partial: Boolean,
    val attemptNumber: Int,
    val timestamp: Long
)
