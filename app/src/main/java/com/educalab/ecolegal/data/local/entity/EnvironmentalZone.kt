package com.educalab.ecolegal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Una de las zonas del Valle Verde (bosque, río, comunidad, animales, agrícola). */
@Entity(tableName = "environmental_zone")
data class EnvironmentalZone(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val code: String,          // ZoneCode.name
    val displayName: String,
    val shortDescription: String,
    val mapOrder: Int,
    val mapX: Float,           // posición relativa 0..1 en el mapa ilustrado
    val mapY: Float,
    val unlockRequiredBadges: Int,   // insignias mínimas para desbloquear
    val iconKey: String
)
