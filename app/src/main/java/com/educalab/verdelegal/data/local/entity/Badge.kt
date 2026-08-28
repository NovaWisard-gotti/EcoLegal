package com.educalab.verdelegal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Catálogo de insignias ilustradas (definición, no la posesión del usuario). */
@Entity(tableName = "badge")
data class Badge(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val code: String,
    val name: String,
    val description: String,
    val iconKey: String,
    val criteriaKey: String    // clave interpretada por RewardEngine
)
