package com.educalab.verdelegal.data.local.seed

import com.educalab.verdelegal.data.local.entity.RestorationMission
import com.educalab.verdelegal.data.local.entity.RestorationStep

/** 10 misiones de reparación (2 por zona), cada una con 3 pasos de arrastrar-y-soltar. */
object RestorationSeed {

    val missions = listOf(
        RestorationMission(701, ZoneSeed.ID_BOSQUE, "Limpieza del sendero", "Ayuda a dejar el sendero del bosque limpio y seguro.", 30, 7L),
        RestorationMission(702, ZoneSeed.ID_BOSQUE, "Restaurar el área pisoteada", "Ayuda a que una plantita pequeña vuelva a crecer.", 30, null),

        RestorationMission(703, ZoneSeed.ID_RIO, "Río más limpio", "Ayuda a retirar lo que no pertenece al río.", 30, 2L),
        RestorationMission(704, ZoneSeed.ID_RIO, "Orilla protegida", "Ayuda a proteger la orilla del río con plantas y señales.", 30, null),

        RestorationMission(705, ZoneSeed.ID_COMUNIDAD, "Parque en orden", "Ayuda a dejar el parque del barrio en buen estado.", 25, null),
        RestorationMission(706, ZoneSeed.ID_COMUNIDAD, "Jardín compartido floreciendo", "Ayuda a que el jardín comunitario florezca.", 25, null),

        RestorationMission(707, ZoneSeed.ID_ANIMALES, "Cerca reparada", "Ayuda a reparar la cerca del refugio animal.", 30, 9L),
        RestorationMission(708, ZoneSeed.ID_ANIMALES, "Estanque tranquilo", "Ayuda a que el estanque de los patos esté en calma.", 30, null),

        RestorationMission(709, ZoneSeed.ID_AGRICOLA, "Cultivo cuidado", "Ayuda a cuidar el agua y las plantas del cultivo.", 25, null),
        RestorationMission(710, ZoneSeed.ID_AGRICOLA, "Tierra descansada", "Ayuda a que la tierra del huerto se recupere.", 25, null)
    )

    val steps = listOf(
        // 701 Limpieza del sendero
        RestorationStep(801, 701, "Recoge los residuos del sendero", 1, "item_trashbag", "slot_trailbin"),
        RestorationStep(802, 701, "Coloca un cesto de reciclaje", 2, "item_bin", "slot_trailspot"),
        RestorationStep(803, 701, "Coloca una señal de sendero cuidado", 3, "item_sign", "slot_trailsign"),
        // 702 Restaurar área pisoteada
        RestorationStep(804, 702, "Delimita el área con piedritas", 1, "item_stones", "slot_border"),
        RestorationStep(805, 702, "Planta una semilla nueva", 2, "item_seed", "slot_soil"),
        RestorationStep(806, 702, "Riega la planta con cuidado", 3, "item_water", "slot_plant"),

        // 703 Río más limpio
        RestorationStep(807, 703, "Retira el envase que flota", 1, "item_bottle", "slot_riverbank"),
        RestorationStep(808, 703, "Retira la espuma con la red", 2, "item_net", "slot_riverfoam"),
        RestorationStep(809, 703, "Coloca una señal de agua cuidada", 3, "item_sign", "slot_riversign"),
        // 704 Orilla protegida
        RestorationStep(810, 704, "Coloca una señal de zona protegida", 1, "item_sign", "slot_bank"),
        RestorationStep(811, 704, "Planta vegetación de orilla", 2, "item_reed", "slot_bankplant"),
        RestorationStep(812, 704, "Ordena las piedras de la orilla", 3, "item_stones", "slot_bankstones"),

        // 705 Parque en orden
        RestorationStep(813, 705, "Recoge los papeles sueltos", 1, "item_paper", "slot_parkbin"),
        RestorationStep(814, 705, "Mueve la rama caída a un lado", 2, "item_branch", "slot_parkside"),
        RestorationStep(815, 705, "Separa lo reciclable", 3, "item_bin", "slot_parkrecycle"),
        // 706 Jardín compartido
        RestorationStep(816, 706, "Riega las plantas del jardín", 1, "item_water", "slot_gardenplant"),
        RestorationStep(817, 706, "Quita la maleza", 2, "item_weed", "slot_gardenbed"),
        RestorationStep(818, 706, "Siembra nuevas flores", 3, "item_flowerseed", "slot_gardensoil"),

        // 707 Cerca reparada
        RestorationStep(819, 707, "Reúne los materiales para reparar", 1, "item_planks", "slot_fencepile"),
        RestorationStep(820, 707, "Repara la parte rota de la cerca", 2, "item_hammer", "slot_fencegap"),
        RestorationStep(821, 707, "Revisa que quede segura", 3, "item_check", "slot_fencecheck"),
        // 708 Estanque tranquilo
        RestorationStep(822, 708, "Limpia el agua del estanque", 1, "item_net", "slot_pondwater"),
        RestorationStep(823, 708, "Llena el cuenco de agua fresca", 2, "item_water", "slot_bowl"),
        RestorationStep(824, 708, "Coloca sombra para los patos", 3, "item_shade", "slot_pondshade"),

        // 709 Cultivo cuidado
        RestorationStep(825, 709, "Repara la manguera que gotea", 1, "item_hosefix", "slot_hose"),
        RestorationStep(826, 709, "Riega con la cantidad justa", 2, "item_water", "slot_field"),
        RestorationStep(827, 709, "Revisa que las plantas estén bien", 3, "item_check", "slot_plantcheck"),
        // 710 Tierra descansada
        RestorationStep(828, 710, "Retira los residuos del cultivo anterior", 1, "item_debris", "slot_soilclear"),
        RestorationStep(829, 710, "Agrega abono natural", 2, "item_compost", "slot_soilfeed"),
        RestorationStep(830, 710, "Deja la tierra en descanso", 3, "item_restsign", "slot_soilrest")
    )
}
