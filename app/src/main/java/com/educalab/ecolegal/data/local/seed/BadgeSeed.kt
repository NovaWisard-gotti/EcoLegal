package com.educalab.ecolegal.data.local.seed

import com.educalab.ecolegal.data.local.entity.Badge

/**
 * 12 insignias. criteriaKey sigue la convención interpretada por RewardEngine
 * (ver KDoc de RewardEngine y docs/BASE_DE_DATOS.md).
 */
object BadgeSeed {
    val badges = listOf(
        Badge(1, "PRIMER_GUARDIAN", "Primer Guardián", "Completaste tu primer reto en el Valle Verde.", "badge_first", "CHALLENGES>=1"),
        Badge(2, "AMIGO_DEL_RIO", "Amigo del Río", "Dominaste por completo la zona del Río Claro.", "badge_river", "ZONE_MASTERED:RIO"),
        Badge(3, "PROTECTOR_DEL_BOSQUE", "Protector del Bosque", "Dominaste por completo la zona del Bosque.", "badge_forest", "ZONE_MASTERED:BOSQUE"),
        Badge(4, "EXPLORADOR_RESPONSABLE", "Explorador Responsable", "Completaste 8 retos en el Valle Verde.", "badge_explorer", "CHALLENGES>=8"),
        Badge(5, "DETECTIVE_VERDE", "Detective Verde", "Descubriste 10 problemas ambientales escondidos.", "badge_detective", "ISSUES>=10"),
        Badge(6, "BUEN_OBSERVADOR", "Buen Observador", "Descubriste 5 problemas ambientales escondidos.", "badge_observer", "ISSUES>=5"),
        Badge(7, "CONSTRUCTOR_RESPONSABLE", "Constructor Responsable", "Completaste 3 misiones de reparación.", "badge_builder", "RESTORATIONS>=3"),
        Badge(8, "RESTAURADOR", "Restaurador", "Completaste 6 misiones de reparación.", "badge_restorer", "RESTORATIONS>=6"),
        Badge(9, "PROTECTOR_DE_ANIMALES", "Protector de Animales", "Dominaste por completo la zona del Refugio Animal.", "badge_animals", "ZONE_MASTERED:ANIMALES"),
        Badge(10, "DEFENSOR_DEL_VALLE", "Defensor del Valle", "Tomaste 5 decisiones correctas de autorización ambiental.", "badge_defender", "AUTHORIZATIONS>=5"),
        Badge(11, "GRAN_GUARDIAN", "Gran Guardián", "Desbloqueaste todas las zonas del Valle Verde.", "badge_grand", "ALL_ZONES_UNLOCKED"),
        Badge(12, "MAESTRO_VERDELEGAL", "Maestro EcoLegal", "Dominaste por completo todas las zonas del Valle Verde.", "badge_master", "ALL_ZONES_MASTERED")
    )
}
