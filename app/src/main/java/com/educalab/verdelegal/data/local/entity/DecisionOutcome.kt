package com.educalab.verdelegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Explicación educativa + consecuencia asociada a haber elegido una Decision. */
@Entity(
    tableName = "decision_outcome",
    foreignKeys = [
        ForeignKey(entity = Decision::class, parentColumns = ["id"], childColumns = ["decisionId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Consequence::class, parentColumns = ["id"], childColumns = ["consequenceId"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [Index("decisionId"), Index("consequenceId")]
)
data class DecisionOutcome(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val decisionId: Long,
    val explanationText: String,
    val consequenceId: Long?
)
