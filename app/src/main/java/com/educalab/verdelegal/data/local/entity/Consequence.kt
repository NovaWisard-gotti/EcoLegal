package com.educalab.verdelegal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Consecuencia ambiental reutilizable (acción -> consecuencia -> posible reparación). */
@Entity(tableName = "consequence")
data class Consequence(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val description: String,
    val severity: String,      // SeverityLevel.name
    val visualKey: String,
    val relatedRestorationMissionId: Long? = null
)
