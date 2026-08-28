package com.educalab.verdelegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Decisión final tomada por el niño sobre una AuthorizationActivity (persistida). */
@Entity(
    tableName = "authorization_decision",
    foreignKeys = [
        ForeignKey(entity = AuthorizationActivity::class, parentColumns = ["id"], childColumns = ["authorizationActivityId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = UserProfile::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("authorizationActivityId"), Index("userId")]
)
data class AuthorizationDecision(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val authorizationActivityId: Long,
    val userId: Long,
    val choice: String,        // AuthorizationChoice.name
    val isCorrect: Boolean,
    val timestamp: Long
)
