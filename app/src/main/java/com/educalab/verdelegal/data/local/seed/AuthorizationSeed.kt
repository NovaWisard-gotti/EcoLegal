package com.educalab.verdelegal.data.local.seed

import com.educalab.verdelegal.data.local.entity.AuthorizationActivity
import com.educalab.verdelegal.data.local.entity.EnvironmentalImpact
import com.educalab.verdelegal.data.local.entity.ProtectionMeasure

/** 10 actividades de autorización ambiental simplificada (2 por zona). */
object AuthorizationSeed {

    val activities = listOf(
        AuthorizationActivity(901, ZoneSeed.ID_BOSQUE, "Construir un sendero nuevo",
            "Un grupo quiere abrir un camino nuevo cerca de árboles jóvenes.", "auth_trail", "SOLICITAR_CAMBIOS"),
        AuthorizationActivity(902, ZoneSeed.ID_BOSQUE, "Recolectar hojas para un proyecto escolar",
            "Un grupo quiere recolectar hojas caídas del suelo del bosque.", "auth_leaves", "AUTORIZAR"),

        AuthorizationActivity(903, ZoneSeed.ID_RIO, "Pescar cerca del río",
            "Alguien quiere pescar cerca de la zona donde crecen peces pequeños.", "auth_fishing", "SOLICITAR_CAMBIOS"),
        AuthorizationActivity(904, ZoneSeed.ID_RIO, "Tomar fotografías del río",
            "Un grupo quiere fotografiar el río para un proyecto sobre la naturaleza.", "auth_camera", "AUTORIZAR"),

        AuthorizationActivity(905, ZoneSeed.ID_COMUNIDAD, "Organizar una feria en la plaza",
            "Se quiere organizar una feria con varios puestos en la plaza.", "auth_fair", "SOLICITAR_CAMBIOS"),
        AuthorizationActivity(906, ZoneSeed.ID_COMUNIDAD, "Pintar un mural en la plaza",
            "Un grupo de vecinos quiere pintar un mural con mensajes ambientales.", "auth_mural", "AUTORIZAR"),

        AuthorizationActivity(907, ZoneSeed.ID_ANIMALES, "Visitar el refugio con un grupo grande",
            "Un grupo escolar quiere visitar el refugio de animales.", "auth_visit", "SOLICITAR_CAMBIOS"),
        AuthorizationActivity(908, ZoneSeed.ID_ANIMALES, "Donar alimento para los animales",
            "Una familia quiere donar comida para los animales del refugio.", "auth_donation", "AUTORIZAR"),

        AuthorizationActivity(909, ZoneSeed.ID_AGRICOLA, "Usar más agua para el cultivo en verano",
            "Se pide usar más agua de la habitual para regar el cultivo.", "auth_water", "SOLICITAR_CAMBIOS"),
        AuthorizationActivity(910, ZoneSeed.ID_AGRICOLA, "Sembrar plantas nuevas junto al camino",
            "Se quiere sembrar plantas nuevas junto al camino agrícola.", "auth_planting", "AUTORIZAR")
    )

    val impacts = listOf(
        EnvironmentalImpact(1001, 901, "Podría dañar las raíces de árboles jóvenes", "HIGH"),
        EnvironmentalImpact(1002, 901, "Podría afectar el paso de animales pequeños", "MEDIUM"),
        EnvironmentalImpact(1003, 902, "Casi no afecta si se recogen solo hojas ya caídas", "LOW"),

        EnvironmentalImpact(1004, 903, "Podría afectar a peces pequeños que están creciendo", "HIGH"),
        EnvironmentalImpact(1005, 903, "Podría afectar el equilibrio del río", "MEDIUM"),
        EnvironmentalImpact(1006, 904, "Casi no afecta al río ni a los animales", "LOW"),

        EnvironmentalImpact(1007, 905, "Podría generar mucha basura si no se organiza bien", "MEDIUM"),
        EnvironmentalImpact(1008, 905, "Podría afectar las áreas verdes de la plaza", "MEDIUM"),
        EnvironmentalImpact(1009, 906, "Casi no afecta si se usa un espacio ya designado", "LOW"),

        EnvironmentalImpact(1010, 907, "Podría estresar a los animales si el grupo es ruidoso", "MEDIUM"),
        EnvironmentalImpact(1011, 907, "Podría afectar el descanso de los animales", "MEDIUM"),
        EnvironmentalImpact(1012, 908, "Casi no afecta si el alimento es el adecuado", "LOW"),

        EnvironmentalImpact(1013, 909, "Podría afectar el agua disponible para otros usos", "HIGH"),
        EnvironmentalImpact(1014, 909, "Podría afectar el suelo si se usa en exceso", "MEDIUM"),
        EnvironmentalImpact(1015, 910, "Casi no afecta si se eligen plantas adecuadas para la zona", "LOW")
    )

    val measures = listOf(
        ProtectionMeasure(1101, 901, "Trazar el sendero lejos de los árboles jóvenes", true),
        ProtectionMeasure(1102, 901, "Usar señales para guiar a los visitantes", true),
        ProtectionMeasure(1103, 901, "Abrir el sendero sin revisar el terreno", false),
        ProtectionMeasure(1104, 902, "Recoger solo hojas que ya están en el suelo", true),
        ProtectionMeasure(1105, 902, "Llevar solo una pequeña cantidad", true),

        ProtectionMeasure(1106, 903, "Pescar lejos de la zona de peces pequeños", true),
        ProtectionMeasure(1107, 903, "Usar solo la cantidad justa, sin exceso", true),
        ProtectionMeasure(1108, 903, "Pescar sin ninguna revisión previa", false),
        ProtectionMeasure(1109, 904, "Mantenerse en la orilla sin entrar al agua", true),

        ProtectionMeasure(1110, 905, "Colocar varios puntos de reciclaje", true),
        ProtectionMeasure(1111, 905, "Delimitar las áreas verdes para protegerlas", true),
        ProtectionMeasure(1112, 905, "No planear nada y improvisar todo", false),
        ProtectionMeasure(1113, 906, "Usar pinturas seguras en un espacio ya designado", true),

        ProtectionMeasure(1114, 907, "Dividir la visita en grupos pequeños", true),
        ProtectionMeasure(1115, 907, "Mantener un tono de voz bajo", true),
        ProtectionMeasure(1116, 907, "Entrar todos juntos sin ningún orden", false),
        ProtectionMeasure(1117, 908, "Consultar qué alimento necesitan los animales", true),

        ProtectionMeasure(1118, 909, "Regar solo en las horas más frescas del día", true),
        ProtectionMeasure(1119, 909, "Medir el agua que se utiliza", true),
        ProtectionMeasure(1120, 909, "Usar toda el agua disponible sin medir nada", false),
        ProtectionMeasure(1121, 910, "Elegir plantas que ya crecen bien en la zona", true)
    )
}
