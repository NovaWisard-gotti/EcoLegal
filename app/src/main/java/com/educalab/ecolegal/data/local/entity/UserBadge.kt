package com.educalab.ecolegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Insignia realmente obtenida por un usuario (persistida, con fecha). */
@Entity(
    tableName = "user_badge",
    foreignKeys = [
        ForeignKey(entity = UserProfile::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Badge::class, parentColumns = ["id"], childColumns = ["badgeId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("userId"), Index("badgeId"), Index(value = ["userId", "badgeId"], unique = true)]
)
data class UserBadge(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val badgeId: Long,
    val earnedAt: Long
)
