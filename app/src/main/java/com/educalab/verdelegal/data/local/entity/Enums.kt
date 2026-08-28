package com.educalab.verdelegal.data.local.entity

/** Estado visual/de progreso de una zona o módulo. Nunca se representa solo con color. */
enum class ZoneStatus { LOCKED, AVAILABLE, STARTED, COMPLETED, MASTERED }

/** Tipos de reto para evitar que la app sea solo opción múltiple. */
enum class ChallengeType {
    DECISION,       // elegir la mejor decisión ante una situación
    SEMAFORO,       // clasificar una acción como verde/amarillo/rojo
    DRAG_RESTORE,   // arrastrar y soltar elementos para reparar una zona
    AUTHORIZATION,  // revisar una actividad y autorizar/pedir cambios/no autorizar
    DETECTIVE_FIND, // tocar y descubrir elementos en una escena
    ORDER_STEPS     // ordenar pasos de una acción responsable
}

enum class SeverityLevel { LOW, MEDIUM, HIGH }

enum class AuthorizationChoice { AUTORIZAR, SOLICITAR_CAMBIOS, NO_AUTORIZAR }

enum class ZoneCode { BOSQUE, RIO, COMUNIDAD, ANIMALES, AGRICOLA }
