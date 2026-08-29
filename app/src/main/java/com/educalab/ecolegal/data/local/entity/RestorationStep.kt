package com.educalab.ecolegal.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** Un paso de arrastrar-y-soltar dentro de una RestorationMission. */
@Entity(
    tableName = "restoration_step",
    foreignKeys = [ForeignKey(
        entity = RestorationMission::class,
        parentColumns = ["id"],
        childColumns = ["missionId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("missionId")]
)
data class RestorationStep(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val missionId: Long,
    val description: String,
    val stepOrder: Int,
    val itemKey: String,        // ilustración del elemento a colocar
    val targetSlotKey: String   // dónde debe colocarse
)
