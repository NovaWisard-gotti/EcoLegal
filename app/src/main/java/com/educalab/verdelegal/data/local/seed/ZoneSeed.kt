package com.educalab.verdelegal.data.local.seed

import com.educalab.verdelegal.data.local.entity.EnvironmentalZone

/**
 * Las 5 zonas del Valle Verde. mapX/mapY son posiciones relativas (0..1) sobre
 * la ilustración del mapa principal. unlockRequiredBadges crea una progresión
 * suave: la primera zona siempre está disponible.
 */
object ZoneSeed {
    const val ID_BOSQUE = 1L
    const val ID_RIO = 2L
    const val ID_COMUNIDAD = 3L
    const val ID_ANIMALES = 4L
    const val ID_AGRICOLA = 5L

    val zones = listOf(
        EnvironmentalZone(
            id = ID_BOSQUE, code = "BOSQUE", displayName = "El Bosque",
            shortDescription = "Árboles altos, senderos y muchos secretos por descubrir.",
            mapOrder = 1, mapX = 0.22f, mapY = 0.30f, unlockRequiredBadges = 0, iconKey = "zone_bosque"
        ),
        EnvironmentalZone(
            id = ID_RIO, code = "RIO", displayName = "El Río Claro",
            shortDescription = "El agua que cruza todo el valle y alimenta a sus habitantes.",
            mapOrder = 2, mapX = 0.55f, mapY = 0.22f, unlockRequiredBadges = 1, iconKey = "zone_rio"
        ),
        EnvironmentalZone(
            id = ID_COMUNIDAD, code = "COMUNIDAD", displayName = "La Comunidad",
            shortDescription = "Casas, plazas y vecinos que cuidan su barrio juntos.",
            mapOrder = 3, mapX = 0.78f, mapY = 0.42f, unlockRequiredBadges = 2, iconKey = "zone_comunidad"
        ),
        EnvironmentalZone(
            id = ID_ANIMALES, code = "ANIMALES", displayName = "Refugio Animal",
            shortDescription = "Un hogar para los animales del valle.",
            mapOrder = 4, mapX = 0.35f, mapY = 0.66f, unlockRequiredBadges = 3, iconKey = "zone_animales"
        ),
        EnvironmentalZone(
            id = ID_AGRICOLA, code = "AGRICOLA", displayName = "Campos Verdes",
            shortDescription = "Cultivos y tierra fértil que alimentan al valle.",
            mapOrder = 5, mapX = 0.68f, mapY = 0.78f, unlockRequiredBadges = 4, iconKey = "zone_agricola"
        )
    )
}
