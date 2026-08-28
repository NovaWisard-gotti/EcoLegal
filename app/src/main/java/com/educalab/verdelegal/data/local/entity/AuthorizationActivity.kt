package com.educalab.verdelegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Actividad ficticia que el niño debe revisar y autorizar (licenciamiento simplificado). */
@Entity(
    tableName = "authorization_activity",
    foreignKeys = [ForeignKey(
        entity = EnvironmentalZone::class,
        parentColumns = ["id"],
        childColumns = ["zoneId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("zoneId")]
)
data class AuthorizationActivity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val zoneId: Long,
    val activityName: String,
    val description: String,
    val iconKey: String,
    val correctChoice: String   // AuthorizationChoice.name
)
