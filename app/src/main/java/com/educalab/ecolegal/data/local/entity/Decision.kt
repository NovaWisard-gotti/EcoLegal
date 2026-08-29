package com.educalab.ecolegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Una opción de decisión disponible dentro de un reto de tipo DECISION. */
@Entity(
    tableName = "decision",
    foreignKeys = [ForeignKey(
        entity = Challenge::class,
        parentColumns = ["id"],
        childColumns = ["challengeId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("challengeId")]
)
data class Decision(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val challengeId: Long,
    val text: String,
    val isCorrect: Boolean,
    val isPartial: Boolean,
    val decisionOrder: Int
)
