package com.educalab.ecolegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Posible impacto de una AuthorizationActivity que el niño debe revisar. */
@Entity(
    tableName = "environmental_impact",
    foreignKeys = [ForeignKey(
        entity = AuthorizationActivity::class,
        parentColumns = ["id"],
        childColumns = ["authorizationActivityId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("authorizationActivityId")]
)
data class EnvironmentalImpact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorizationActivityId: Long,
    val impactText: String,
    val impactLevel: String   // SeverityLevel.name
)
