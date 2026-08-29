package com.educalab.ecolegal.ui.navigation

/** Rutas de navegación de EcoLegal (Navigation Compose). */
object Routes {
    const val ONBOARDING = "onboarding"
    const val PROFILE_SETUP = "profile_setup"
    const val MAP = "map"
    const val ZONE = "zone/{zoneId}"
    const val CHALLENGE = "challenge/{challengeId}"
    const val RESTORATION = "restoration/{missionId}"
    const val AUTHORIZATION = "authorization/{activityId}"
    const val BADGES = "badges"
    const val REVIEW = "review"
    const val SETTINGS = "settings"

    fun zone(zoneId: Long) = "zone/$zoneId"
    fun challenge(challengeId: Long) = "challenge/$challengeId"
    fun restoration(missionId: Long) = "restoration/$missionId"
    fun authorization(activityId: Long) = "authorization/$activityId"
}
