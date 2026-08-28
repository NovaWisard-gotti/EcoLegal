package com.educalab.verdelegal.data.local.seed

import com.educalab.verdelegal.data.local.entity.EnvironmentalScenario
import com.educalab.verdelegal.data.local.entity.EnvironmentalIssue

/**
 * 20 situaciones ambientales (4 por zona) narradas brevemente por Luma.
 * Los IDs son fijos y legibles para facilitar las relaciones con Challenge.
 */
object ScenarioSeed {

    val scenarios = listOf(
        // ---- BOSQUE (101-104) ----
        EnvironmentalScenario(101, ZoneSeed.ID_BOSQUE, "El sendero descuidado",
            "¡Hola! Soy Luma. Alguien dejó cosas fuera de lugar en este sendero. ¿Me ayudas a investigar?", 1, "bg_forest_trail"),
        EnvironmentalScenario(102, ZoneSeed.ID_BOSQUE, "El árbol señalado",
            "Este árbol tiene una señal especial. Vamos a entender qué significa.", 2, "bg_forest_tree"),
        EnvironmentalScenario(103, ZoneSeed.ID_BOSQUE, "Vecinos del bosque",
            "Unas ardillas viven cerca de aquí. Sus casas merecen respeto.", 3, "bg_forest_animals"),
        EnvironmentalScenario(104, ZoneSeed.ID_BOSQUE, "Un nuevo sendero",
            "Alguien quiere construir un sendero nuevo. Hay que pensarlo bien.", 4, "bg_forest_path"),

        // ---- RIO (105-108) ----
        EnvironmentalScenario(105, ZoneSeed.ID_RIO, "Algo extraño en el agua",
            "¡Algo está afectando este río! Vamos a investigar juntos.", 1, "bg_river_main"),
        EnvironmentalScenario(106, ZoneSeed.ID_RIO, "La orilla del río",
            "Cerca de la orilla hay pistas de lo que ha estado pasando.", 2, "bg_river_bank"),
        EnvironmentalScenario(107, ZoneSeed.ID_RIO, "Los peces del río",
            "Los peces necesitan agua limpia para vivir bien.", 3, "bg_river_fish"),
        EnvironmentalScenario(108, ZoneSeed.ID_RIO, "El puente sobre el río",
            "Cerca del puente, algunas personas realizan actividades distintas.", 4, "bg_river_bridge"),

        // ---- COMUNIDAD (109-112) ----
        EnvironmentalScenario(109, ZoneSeed.ID_COMUNIDAD, "El parque del barrio",
            "El parque necesita un poco de atención esta semana.", 1, "bg_town_park"),
        EnvironmentalScenario(110, ZoneSeed.ID_COMUNIDAD, "El día de reciclaje",
            "Hoy toca separar los residuos del barrio. ¿Sabes cómo hacerlo?", 2, "bg_town_recycle"),
        EnvironmentalScenario(111, ZoneSeed.ID_COMUNIDAD, "Una fiesta comunitaria",
            "Se organiza una fiesta en la plaza. Hay que pensar en el entorno.", 3, "bg_town_plaza"),
        EnvironmentalScenario(112, ZoneSeed.ID_COMUNIDAD, "El jardín compartido",
            "Los vecinos cuidan juntos un jardín comunitario.", 4, "bg_town_garden"),

        // ---- ANIMALES (113-116) ----
        EnvironmentalScenario(113, ZoneSeed.ID_ANIMALES, "El hábitat en peligro",
            "Este refugio necesita ayuda para que los animales estén seguros.", 1, "bg_shelter_main"),
        EnvironmentalScenario(114, ZoneSeed.ID_ANIMALES, "Un nido escondido",
            "Hay un nido cerca. Debemos observar antes de actuar.", 2, "bg_shelter_nest"),
        EnvironmentalScenario(115, ZoneSeed.ID_ANIMALES, "El estanque de los patos",
            "Los patos del refugio necesitan agua limpia y tranquila.", 3, "bg_shelter_pond"),
        EnvironmentalScenario(116, ZoneSeed.ID_ANIMALES, "Visita al refugio",
            "Un grupo quiere visitar el refugio. ¿Cómo lo hacemos sin molestar a los animales?", 4, "bg_shelter_visit"),

        // ---- AGRICOLA (117-120) ----
        EnvironmentalScenario(117, ZoneSeed.ID_AGRICOLA, "El campo sediento",
            "Este cultivo necesita agua, pero hay que usarla con cuidado.", 1, "bg_farm_field"),
        EnvironmentalScenario(118, ZoneSeed.ID_AGRICOLA, "La tierra del huerto",
            "La tierra también necesita descanso y cuidado.", 2, "bg_farm_soil"),
        EnvironmentalScenario(119, ZoneSeed.ID_AGRICOLA, "Los residuos del campo",
            "Después de la cosecha, quedan restos que hay que manejar bien.", 3, "bg_farm_waste"),
        EnvironmentalScenario(120, ZoneSeed.ID_AGRICOLA, "Un nuevo cultivo",
            "Se quiere sembrar algo nuevo cerca del río. Hay que revisarlo.", 4, "bg_farm_new")
    )

    // Issues (modo Detective Verde) para una escena por zona.
    val issues = listOf(
        EnvironmentalIssue(201, 101, "Basura abandonada", "Alguien dejó envolturas junto al sendero.", "issue_litter", "MEDIUM", 0.30f, 0.55f),
        EnvironmentalIssue(202, 101, "Planta pisoteada", "Una planta pequeña fue pisada al salir del camino.", "issue_plant", "LOW", 0.62f, 0.40f),
        EnvironmentalIssue(203, 101, "Señal de zona protegida", "Un cartel indica que esta zona necesita cuidado especial.", "issue_sign", "LOW", 0.78f, 0.65f),

        EnvironmentalIssue(204, 106, "Envase en la orilla", "Un envase flota cerca de la orilla del río.", "issue_bottle", "MEDIUM", 0.25f, 0.60f),
        EnvironmentalIssue(205, 106, "Agua con espuma", "El agua tiene una espuma que no debería estar ahí.", "issue_foam", "HIGH", 0.55f, 0.45f),
        EnvironmentalIssue(206, 106, "Pez cerca de la orilla", "Un pez nada cerca de la zona afectada.", "issue_fish", "LOW", 0.70f, 0.58f),

        EnvironmentalIssue(207, 109, "Papeles en el pasto", "Hay papeles sueltos sobre el pasto del parque.", "issue_paper", "LOW", 0.40f, 0.50f),
        EnvironmentalIssue(208, 109, "Rama caída", "Una rama cayó y bloquea un camino pequeño.", "issue_branch", "LOW", 0.65f, 0.35f),

        EnvironmentalIssue(209, 113, "Cerca dañada", "Una parte de la cerca del refugio está rota.", "issue_fence", "MEDIUM", 0.35f, 0.42f),
        EnvironmentalIssue(210, 113, "Cuenco vacío", "El cuenco de agua de los animales está vacío.", "issue_bowl", "MEDIUM", 0.58f, 0.60f),

        EnvironmentalIssue(211, 117, "Manguera goteando", "Una manguera gotea agua sin control.", "issue_hose", "MEDIUM", 0.30f, 0.50f),
        EnvironmentalIssue(212, 117, "Bolsa de semillas abierta", "Una bolsa quedó abierta cerca del cultivo.", "issue_bag", "LOW", 0.66f, 0.44f)
    )
}
