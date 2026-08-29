package com.educalab.ecolegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Un reto jugable, de uno de varios tipos (ver ChallengeType). */
@Entity(
    tableName = "challenge",
    foreignKeys = [ForeignKey(
        entity = EnvironmentalScenario::class,
        parentColumns = ["id"],
        childColumns = ["scenarioId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("scenarioId")]
)
data class Challenge(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val scenarioId: Long,
    val type: String,          // ChallengeType.name
    val title: String,
    val prompt: String,
    val difficulty: Int,       // 1..3
    val xpReward: Int,
    val challengeOrder: Int
)
