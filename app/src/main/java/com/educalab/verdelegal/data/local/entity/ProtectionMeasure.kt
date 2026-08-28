package com.educalab.verdelegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Medida de cuidado propuesta para una AuthorizationActivity. */
@Entity(
    tableName = "protection_measure",
    foreignKeys = [ForeignKey(
        entity = AuthorizationActivity::class,
        parentColumns = ["id"],
        childColumns = ["authorizationActivityId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("authorizationActivityId")]
)
data class ProtectionMeasure(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorizationActivityId: Long,
    val measureText: String,
    val isRecommended: Boolean
)
