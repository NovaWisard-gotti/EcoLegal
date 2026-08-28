package com.educalab.verdelegal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Perfil local del niño/a. Nunca contiene datos personales reales:
 * solo un alias elegido y un avatar ilustrado local (ver AvatarCatalog).
 */
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val alias: String,
    val avatarKey: String,
    val createdAt: Long,
    val soundEnabled: Boolean = true,
    val hapticEnabled: Boolean = true,
    val onboardingCompleted: Boolean = false
)
