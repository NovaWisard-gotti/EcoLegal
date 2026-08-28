-- VerdeLegal — sample_data.sql
-- Espejo en SQL puro de los datos semilla reales usados por la app
-- (app/src/main/java/.../data/local/seed/*.kt). Útil para inspeccionar o
-- cargar el contenido fuera de Android (p. ej. con `sqlite3`).

PRAGMA foreign_keys = ON;

-- ==================== ZONAS ====================
INSERT INTO environmental_zone (id, code, displayName, shortDescription, mapOrder, mapX, mapY, unlockRequiredBadges, iconKey) VALUES
(1, 'BOSQUE', 'El Bosque', 'Árboles altos, senderos y muchos secretos por descubrir.', 1, 0.22, 0.30, 0, 'zone_bosque'),
(2, 'RIO', 'El Río Claro', 'El agua que cruza todo el valle y alimenta a sus habitantes.', 2, 0.55, 0.22, 1, 'zone_rio'),
(3, 'COMUNIDAD', 'La Comunidad', 'Casas, plazas y vecinos que cuidan su barrio juntos.', 3, 0.78, 0.42, 2, 'zone_comunidad'),
(4, 'ANIMALES', 'Refugio Animal', 'Un hogar para los animales del valle.', 4, 0.35, 0.66, 3, 'zone_animales'),
(5, 'AGRICOLA', 'Campos Verdes', 'Cultivos y tierra fértil que alimentan al valle.', 5, 0.68, 0.78, 4, 'zone_agricola');

-- ==================== ESCENARIOS (20) ====================
INSERT INTO environmental_scenario (id, zoneId, title, lumaIntro, sceneOrder, backgroundKey) VALUES
(101, 1, 'El sendero descuidado', '¡Hola! Soy Luma. Alguien dejó cosas fuera de lugar en este sendero. ¿Me ayudas a investigar?', 1, 'bg_forest_trail'),
(102, 1, 'El árbol señalado', 'Este árbol tiene una señal especial. Vamos a entender qué significa.', 2, 'bg_forest_tree'),
(103, 1, 'Vecinos del bosque', 'Unas ardillas viven cerca de aquí. Sus casas merecen respeto.', 3, 'bg_forest_animals'),
(104, 1, 'Un nuevo sendero', 'Alguien quiere construir un sendero nuevo. Hay que pensarlo bien.', 4, 'bg_forest_path'),
(105, 2, 'Algo extraño en el agua', '¡Algo está afectando este río! Vamos a investigar juntos.', 1, 'bg_river_main'),
(106, 2, 'La orilla del río', 'Cerca de la orilla hay pistas de lo que ha estado pasando.', 2, 'bg_river_bank'),
(107, 2, 'Los peces del río', 'Los peces necesitan agua limpia para vivir bien.', 3, 'bg_river_fish'),
(108, 2, 'El puente sobre el río', 'Cerca del puente, algunas personas realizan actividades distintas.', 4, 'bg_river_bridge'),
(109, 3, 'El parque del barrio', 'El parque necesita un poco de atención esta semana.', 1, 'bg_town_park'),
(110, 3, 'El día de reciclaje', 'Hoy toca separar los residuos del barrio. ¿Sabes cómo hacerlo?', 2, 'bg_town_recycle'),
(111, 3, 'Una fiesta comunitaria', 'Se organiza una fiesta en la plaza. Hay que pensar en el entorno.', 3, 'bg_town_plaza'),
(112, 3, 'El jardín compartido', 'Los vecinos cuidan juntos un jardín comunitario.', 4, 'bg_town_garden'),
(113, 4, 'El hábitat en peligro', 'Este refugio necesita ayuda para que los animales estén seguros.', 1, 'bg_shelter_main'),
(114, 4, 'Un nido escondido', 'Hay un nido cerca. Debemos observar antes de actuar.', 2, 'bg_shelter_nest'),
(115, 4, 'El estanque de los patos', 'Los patos del refugio necesitan agua limpia y tranquila.', 3, 'bg_shelter_pond'),
(116, 4, 'Visita al refugio', 'Un grupo quiere visitar el refugio. ¿Cómo lo hacemos sin molestar a los animales?', 4, 'bg_shelter_visit'),
(117, 5, 'El campo sediento', 'Este cultivo necesita agua, pero hay que usarla con cuidado.', 1, 'bg_farm_field'),
(118, 5, 'La tierra del huerto', 'La tierra también necesita descanso y cuidado.', 2, 'bg_farm_soil'),
(119, 5, 'Los residuos del campo', 'Después de la cosecha, quedan restos que hay que manejar bien.', 3, 'bg_farm_waste'),
(120, 5, 'Un nuevo cultivo', 'Se quiere sembrar algo nuevo cerca del río. Hay que revisarlo.', 4, 'bg_farm_new');

-- ==================== PROBLEMAS AMBIENTALES (Detective Verde) ====================
INSERT INTO environmental_issue (id, scenarioId, title, description, iconKey, severity, positionX, positionY) VALUES
(201, 101, 'Basura abandonada', 'Alguien dejó envolturas junto al sendero.', 'issue_litter', 'MEDIUM', 0.30, 0.55),
(202, 101, 'Planta pisoteada', 'Una planta pequeña fue pisada al salir del camino.', 'issue_plant', 'LOW', 0.62, 0.40),
(203, 101, 'Señal de zona protegida', 'Un cartel indica que esta zona necesita cuidado especial.', 'issue_sign', 'LOW', 0.78, 0.65),
(204, 106, 'Envase en la orilla', 'Un envase flota cerca de la orilla del río.', 'issue_bottle', 'MEDIUM', 0.25, 0.60),
(205, 106, 'Agua con espuma', 'El agua tiene una espuma que no debería estar ahí.', 'issue_foam', 'HIGH', 0.55, 0.45),
(206, 106, 'Pez cerca de la orilla', 'Un pez nada cerca de la zona afectada.', 'issue_fish', 'LOW', 0.70, 0.58),
(207, 109, 'Papeles en el pasto', 'Hay papeles sueltos sobre el pasto del parque.', 'issue_paper', 'LOW', 0.40, 0.50),
(208, 109, 'Rama caída', 'Una rama cayó y bloquea un camino pequeño.', 'issue_branch', 'LOW', 0.65, 0.35),
(209, 113, 'Cerca dañada', 'Una parte de la cerca del refugio está rota.', 'issue_fence', 'MEDIUM', 0.35, 0.42),
(210, 113, 'Cuenco vacío', 'El cuenco de agua de los animales está vacío.', 'issue_bowl', 'MEDIUM', 0.58, 0.60),
(211, 117, 'Manguera goteando', 'Una manguera gotea agua sin control.', 'issue_hose', 'MEDIUM', 0.30, 0.50),
(212, 117, 'Bolsa de semillas abierta', 'Una bolsa quedó abierta cerca del cultivo.', 'issue_bag', 'LOW', 0.66, 0.44);

-- ==================== RETOS (15) ====================
INSERT INTO challenge (id, scenarioId, type, title, prompt, difficulty, xpReward, challengeOrder) VALUES
(301, 101, 'DECISION', 'Basura en el sendero', 'Encontramos basura en el sendero del bosque. ¿Qué hacemos?', 1, 20, 1),
(302, 101, 'DETECTIVE_FIND', 'Detective en el bosque', 'Toca los elementos que podrían ser un problema para el bosque.', 1, 15, 2),
(303, 102, 'ORDER_STEPS', 'Cuidar un árbol protegido', 'Ordena los pasos para cuidar correctamente un árbol protegido.', 2, 20, 3),
(304, 105, 'DECISION', 'Algo extraño en el agua', 'Vemos espuma extraña en el río. ¿Qué hacemos primero?', 2, 20, 1),
(305, 107, 'SEMAFORO', 'Semáforo del río', 'Clasifica estas acciones cerca del río.', 2, 20, 2),
(306, 106, 'DETECTIVE_FIND', 'Detective en la orilla', 'Explora la orilla y encuentra lo que no debería estar ahí.', 1, 15, 3),
(307, 110, 'DECISION', 'Día de reciclaje', '¿Dónde va una botella de plástico vacía?', 1, 15, 1),
(308, 109, 'ORDER_STEPS', 'Parque en orden', 'Ordena los pasos para dejar el parque en buen estado.', 1, 20, 2),
(309, 111, 'SEMAFORO', 'Semáforo de la fiesta', 'Clasifica estas acciones durante la fiesta comunitaria.', 2, 20, 3),
(310, 113, 'DECISION', 'La cerca rota', 'La cerca del refugio está rota. ¿Qué hacemos?', 2, 20, 1),
(311, 113, 'DETECTIVE_FIND', 'Detective en el refugio', 'Observa el refugio y encuentra lo que necesita atención.', 1, 15, 2),
(312, 115, 'SEMAFORO', 'Semáforo del estanque', 'Clasifica estas acciones cerca del estanque de los patos.', 2, 20, 3),
(313, 117, 'DECISION', 'El campo sediento', 'El cultivo necesita agua. ¿Cuál es la mejor manera de regar?', 2, 20, 1),
(314, 118, 'ORDER_STEPS', 'Cuidar la tierra', 'Ordena los pasos para cuidar la tierra del huerto.', 2, 20, 2),
(315, 117, 'DETECTIVE_FIND', 'Detective en el campo', 'Encuentra lo que el campo necesita revisar.', 1, 15, 3);

-- ==================== DECISIONES / OPCIONES (34) ====================
INSERT INTO decision (id, challengeId, text, isCorrect, isPartial, decisionOrder) VALUES
(401, 301, 'Recogerla y llevarla a un cesto de reciclaje', 1, 0, 1),
(402, 301, 'Dejarla ahí, alguien más la recogerá', 0, 0, 2),
(403, 301, 'Empujarla fuera del sendero con el pie', 0, 1, 3),
(404, 303, 'Observar la señal de zona protegida', 1, 0, 1),
(405, 303, 'No cortar ramas ni hojas', 1, 0, 2),
(406, 303, 'Caminar solo por el sendero marcado', 1, 0, 3),
(407, 303, 'Contarle a un adulto lo que aprendiste', 1, 0, 4),
(408, 304, 'Observar de dónde viene el problema antes de actuar', 1, 0, 1),
(409, 304, 'Meter las manos al agua para revisarla', 0, 0, 2),
(410, 304, 'Contarle a Luma lo que encontramos', 0, 1, 3),
(411, 305, 'Recoger un envase que ves flotando cerca de la orilla', 1, 0, 1),
(412, 305, 'Lavar platos directamente en el río', 0, 0, 2),
(413, 305, 'Acercarse a mirar el río sin tocar el agua', 0, 1, 3),
(414, 307, 'En el contenedor de plástico', 1, 0, 1),
(415, 307, 'En cualquier contenedor, no importa cuál', 0, 0, 2),
(416, 308, 'Recoger los papeles sueltos', 1, 0, 1),
(417, 308, 'Separar lo que se puede reciclar', 1, 0, 2),
(418, 308, 'Colocar la rama caída a un lado del camino', 1, 0, 3),
(419, 308, 'Avisar si algo necesita reparación', 1, 0, 4),
(420, 309, 'Usar vasos reutilizables en la fiesta', 1, 0, 1),
(421, 309, 'Dejar todos los residuos tirados al terminar', 0, 0, 2),
(422, 309, 'Usar globos que después quedan sueltos en el pasto', 0, 1, 3),
(423, 310, 'Avisar para repararla y evitar que los animales se lastimen', 1, 0, 1),
(424, 310, 'Dejarla así, seguro nadie nota la diferencia', 0, 0, 2),
(425, 312, 'Observar a los patos desde una distancia segura', 1, 0, 1),
(426, 312, 'Perseguir a los patos para verlos de cerca', 0, 0, 2),
(427, 312, 'Acercarse mucho para tomar una foto', 0, 1, 3),
(428, 313, 'Regar en la mañana o en la tarde, cuando hace menos calor', 1, 0, 1),
(429, 313, 'Regar todo el día sin control', 0, 0, 2),
(430, 313, 'Reparar primero la manguera que gotea', 0, 1, 3),
(431, 314, 'Revisar si la tierra está muy seca o dañada', 1, 0, 1),
(432, 314, 'Agregar abono natural si hace falta', 1, 0, 2),
(433, 314, 'Dejar descansar una parte de la tierra', 1, 0, 3),
(434, 314, 'Sembrar de nuevo cuando la tierra esté lista', 1, 0, 4);

-- ==================== CONSECUENCIAS (8) ====================
INSERT INTO consequence (id, description, severity, visualKey, relatedRestorationMissionId) VALUES
(601, 'La basura puede quedarse mucho tiempo en el bosque y afectar a las plantas y animales cercanos.', 'MEDIUM', 'consequence_litter_forest', 701),
(602, 'La basura sigue en el bosque, solo cambió de lugar.', 'LOW', 'consequence_litter_moved', 701),
(603, 'Sin observar primero, podríamos no entender qué originó el problema en el agua.', 'LOW', 'consequence_river_rush', 703),
(604, 'El agua del río puede afectar a los peces y otros animales si se usa sin cuidado.', 'HIGH', 'consequence_river_wash', 703),
(605, 'Los residuos de la fiesta pueden afectar el pasto y la plaza si nadie los recoge.', 'MEDIUM', 'consequence_party_litter', 706),
(606, 'Una cerca rota puede dejar salir a los animales o dejar entrar peligros.', 'MEDIUM', 'consequence_fence_broken', 707),
(607, 'Acercarse demasiado puede asustar a los patos y alejarlos de su hogar.', 'LOW', 'consequence_ducks_scared', 708),
(608, 'Regar sin control puede desperdiciar agua que otros cultivos también necesitan.', 'MEDIUM', 'consequence_water_waste', 709);

-- ==================== EXPLICACIONES DE DECISIÓN (22) ====================
INSERT INTO decision_outcome (id, decisionId, explanationText, consequenceId) VALUES
(501, 401, '¡Muy bien! Recoger la basura y llevarla a un cesto ayuda a que el sendero se mantenga sano.', NULL),
(502, 402, 'Si la dejamos ahí, puede afectar a las plantas y animales que viven cerca.', 601),
(503, 403, 'Empujarla ayuda un poco, pero el problema sigue porque la basura no salió del bosque.', 602),
(504, 408, '¡Excelente decisión! Observar primero ayuda a entender el problema sin correr riesgos.', NULL),
(505, 409, 'Tocar un agua que no conocemos puede no ser seguro. Mejor observar primero.', 604),
(506, 410, 'Contarle a alguien es una gran idea, pero primero conviene observar la situación.', 603),
(507, 411, '¡Correcto! Recoger un envase que flota ayuda a mantener el río limpio.', NULL),
(508, 412, 'Lavar platos en el río puede afectar el agua que usan los animales.', 604),
(509, 413, 'Observar con cuidado está bien, pero podríamos hacer algo más para ayudar.', NULL),
(510, 414, '¡Correcto! El plástico va en su propio contenedor para poder reciclarse.', NULL),
(511, 415, 'Si mezclamos los residuos, es más difícil reciclarlos correctamente.', NULL),
(512, 420, '¡Buena elección! Los vasos reutilizables generan menos residuos.', NULL),
(513, 421, 'Dejar todo tirado afecta la plaza y el trabajo de quienes la cuidan.', 605),
(514, 422, 'Los globos sueltos pueden terminar como basura en el pasto o en el río.', 605),
(515, 423, '¡Bien pensado! Avisar a tiempo evita que los animales corran peligro.', NULL),
(516, 424, 'Una cerca rota puede ser un problema serio para los animales del refugio.', 606),
(517, 425, '¡Correcto! Observar desde lejos mantiene tranquilos a los patos.', NULL),
(518, 426, 'Perseguir a los patos los asusta y puede lastimarlos.', 607),
(519, 427, 'Acercarse mucho puede incomodar a los patos, aunque sea solo para una foto.', 607),
(520, 428, '¡Muy bien! Regar en horas frescas ayuda a que el agua se aproveche mejor.', NULL),
(521, 429, 'Regar sin control desperdicia agua que otros cultivos también necesitan.', 608),
(522, 430, 'Reparar la manguera es una gran idea, pero no responde cuándo es mejor regar.', NULL);

-- ==================== MISIONES DE REPARACIÓN (10) ====================
INSERT INTO restoration_mission (id, zoneId, title, description, xpReward, badgeIdOnComplete) VALUES
(701, 1, 'Limpieza del sendero', 'Ayuda a dejar el sendero del bosque limpio y seguro.', 30, 7),
(702, 1, 'Restaurar el área pisoteada', 'Ayuda a que una plantita pequeña vuelva a crecer.', 30, NULL),
(703, 2, 'Río más limpio', 'Ayuda a retirar lo que no pertenece al río.', 30, 2),
(704, 2, 'Orilla protegida', 'Ayuda a proteger la orilla del río con plantas y señales.', 30, NULL),
(705, 3, 'Parque en orden', 'Ayuda a dejar el parque del barrio en buen estado.', 25, NULL),
(706, 3, 'Jardín compartido floreciendo', 'Ayuda a que el jardín comunitario florezca.', 25, NULL),
(707, 4, 'Cerca reparada', 'Ayuda a reparar la cerca del refugio animal.', 30, 9),
(708, 4, 'Estanque tranquilo', 'Ayuda a que el estanque de los patos esté en calma.', 30, NULL),
(709, 5, 'Cultivo cuidado', 'Ayuda a cuidar el agua y las plantas del cultivo.', 25, NULL),
(710, 5, 'Tierra descansada', 'Ayuda a que la tierra del huerto se recupere.', 25, NULL);

-- ==================== PASOS DE REPARACIÓN (30) ====================
INSERT INTO restoration_step (id, missionId, description, stepOrder, itemKey, targetSlotKey) VALUES
(801, 701, 'Recoge los residuos del sendero', 1, 'item_trashbag', 'slot_trailbin'),
(802, 701, 'Coloca un cesto de reciclaje', 2, 'item_bin', 'slot_trailspot'),
(803, 701, 'Coloca una señal de sendero cuidado', 3, 'item_sign', 'slot_trailsign'),
(804, 702, 'Delimita el área con piedritas', 1, 'item_stones', 'slot_border'),
(805, 702, 'Planta una semilla nueva', 2, 'item_seed', 'slot_soil'),
(806, 702, 'Riega la planta con cuidado', 3, 'item_water', 'slot_plant'),
(807, 703, 'Retira el envase que flota', 1, 'item_bottle', 'slot_riverbank'),
(808, 703, 'Retira la espuma con la red', 2, 'item_net', 'slot_riverfoam'),
(809, 703, 'Coloca una señal de agua cuidada', 3, 'item_sign', 'slot_riversign'),
(810, 704, 'Coloca una señal de zona protegida', 1, 'item_sign', 'slot_bank'),
(811, 704, 'Planta vegetación de orilla', 2, 'item_reed', 'slot_bankplant'),
(812, 704, 'Ordena las piedras de la orilla', 3, 'item_stones', 'slot_bankstones'),
(813, 705, 'Recoge los papeles sueltos', 1, 'item_paper', 'slot_parkbin'),
(814, 705, 'Mueve la rama caída a un lado', 2, 'item_branch', 'slot_parkside'),
(815, 705, 'Separa lo reciclable', 3, 'item_bin', 'slot_parkrecycle'),
(816, 706, 'Riega las plantas del jardín', 1, 'item_water', 'slot_gardenplant'),
(817, 706, 'Quita la maleza', 2, 'item_weed', 'slot_gardenbed'),
(818, 706, 'Siembra nuevas flores', 3, 'item_flowerseed', 'slot_gardensoil'),
(819, 707, 'Reúne los materiales para reparar', 1, 'item_planks', 'slot_fencepile'),
(820, 707, 'Repara la parte rota de la cerca', 2, 'item_hammer', 'slot_fencegap'),
(821, 707, 'Revisa que quede segura', 3, 'item_check', 'slot_fencecheck'),
(822, 708, 'Limpia el agua del estanque', 1, 'item_net', 'slot_pondwater'),
(823, 708, 'Llena el cuenco de agua fresca', 2, 'item_water', 'slot_bowl'),
(824, 708, 'Coloca sombra para los patos', 3, 'item_shade', 'slot_pondshade'),
(825, 709, 'Repara la manguera que gotea', 1, 'item_hosefix', 'slot_hose'),
(826, 709, 'Riega con la cantidad justa', 2, 'item_water', 'slot_field'),
(827, 709, 'Revisa que las plantas estén bien', 3, 'item_check', 'slot_plantcheck'),
(828, 710, 'Retira los residuos del cultivo anterior', 1, 'item_debris', 'slot_soilclear'),
(829, 710, 'Agrega abono natural', 2, 'item_compost', 'slot_soilfeed'),
(830, 710, 'Deja la tierra en descanso', 3, 'item_restsign', 'slot_soilrest');

-- ==================== ACTIVIDADES DE AUTORIZACIÓN (10) ====================
INSERT INTO authorization_activity (id, zoneId, activityName, description, iconKey, correctChoice) VALUES
(901, 1, 'Construir un sendero nuevo', 'Un grupo quiere abrir un camino nuevo cerca de árboles jóvenes.', 'auth_trail', 'SOLICITAR_CAMBIOS'),
(902, 1, 'Recolectar hojas para un proyecto escolar', 'Un grupo quiere recolectar hojas caídas del suelo del bosque.', 'auth_leaves', 'AUTORIZAR'),
(903, 2, 'Pescar cerca del río', 'Alguien quiere pescar cerca de la zona donde crecen peces pequeños.', 'auth_fishing', 'SOLICITAR_CAMBIOS'),
(904, 2, 'Tomar fotografías del río', 'Un grupo quiere fotografiar el río para un proyecto sobre la naturaleza.', 'auth_camera', 'AUTORIZAR'),
(905, 3, 'Organizar una feria en la plaza', 'Se quiere organizar una feria con varios puestos en la plaza.', 'auth_fair', 'SOLICITAR_CAMBIOS'),
(906, 3, 'Pintar un mural en la plaza', 'Un grupo de vecinos quiere pintar un mural con mensajes ambientales.', 'auth_mural', 'AUTORIZAR'),
(907, 4, 'Visitar el refugio con un grupo grande', 'Un grupo escolar quiere visitar el refugio de animales.', 'auth_visit', 'SOLICITAR_CAMBIOS'),
(908, 4, 'Donar alimento para los animales', 'Una familia quiere donar comida para los animales del refugio.', 'auth_donation', 'AUTORIZAR'),
(909, 5, 'Usar más agua para el cultivo en verano', 'Se pide usar más agua de la habitual para regar el cultivo.', 'auth_water', 'SOLICITAR_CAMBIOS'),
(910, 5, 'Sembrar plantas nuevas junto al camino', 'Se quiere sembrar plantas nuevas junto al camino agrícola.', 'auth_planting', 'AUTORIZAR');

-- ==================== IMPACTOS AMBIENTALES (15) ====================
INSERT INTO environmental_impact (id, authorizationActivityId, impactText, impactLevel) VALUES
(1001, 901, 'Podría dañar las raíces de árboles jóvenes', 'HIGH'),
(1002, 901, 'Podría afectar el paso de animales pequeños', 'MEDIUM'),
(1003, 902, 'Casi no afecta si se recogen solo hojas ya caídas', 'LOW'),
(1004, 903, 'Podría afectar a peces pequeños que están creciendo', 'HIGH'),
(1005, 903, 'Podría afectar el equilibrio del río', 'MEDIUM'),
(1006, 904, 'Casi no afecta al río ni a los animales', 'LOW'),
(1007, 905, 'Podría generar mucha basura si no se organiza bien', 'MEDIUM'),
(1008, 905, 'Podría afectar las áreas verdes de la plaza', 'MEDIUM'),
(1009, 906, 'Casi no afecta si se usa un espacio ya designado', 'LOW'),
(1010, 907, 'Podría estresar a los animales si el grupo es ruidoso', 'MEDIUM'),
(1011, 907, 'Podría afectar el descanso de los animales', 'MEDIUM'),
(1012, 908, 'Casi no afecta si el alimento es el adecuado', 'LOW'),
(1013, 909, 'Podría afectar el agua disponible para otros usos', 'HIGH'),
(1014, 909, 'Podría afectar el suelo si se usa en exceso', 'MEDIUM'),
(1015, 910, 'Casi no afecta si se eligen plantas adecuadas para la zona', 'LOW');

-- ==================== MEDIDAS DE PROTECCIÓN (21) ====================
INSERT INTO protection_measure (id, authorizationActivityId, measureText, isRecommended) VALUES
(1101, 901, 'Trazar el sendero lejos de los árboles jóvenes', 1),
(1102, 901, 'Usar señales para guiar a los visitantes', 1),
(1103, 901, 'Abrir el sendero sin revisar el terreno', 0),
(1104, 902, 'Recoger solo hojas que ya están en el suelo', 1),
(1105, 902, 'Llevar solo una pequeña cantidad', 1),
(1106, 903, 'Pescar lejos de la zona de peces pequeños', 1),
(1107, 903, 'Usar solo la cantidad justa, sin exceso', 1),
(1108, 903, 'Pescar sin ninguna revisión previa', 0),
(1109, 904, 'Mantenerse en la orilla sin entrar al agua', 1),
(1110, 905, 'Colocar varios puntos de reciclaje', 1),
(1111, 905, 'Delimitar las áreas verdes para protegerlas', 1),
(1112, 905, 'No planear nada y improvisar todo', 0),
(1113, 906, 'Usar pinturas seguras en un espacio ya designado', 1),
(1114, 907, 'Dividir la visita en grupos pequeños', 1),
(1115, 907, 'Mantener un tono de voz bajo', 1),
(1116, 907, 'Entrar todos juntos sin ningún orden', 0),
(1117, 908, 'Consultar qué alimento necesitan los animales', 1),
(1118, 909, 'Regar solo en las horas más frescas del día', 1),
(1119, 909, 'Medir el agua que se utiliza', 1),
(1120, 909, 'Usar toda el agua disponible sin medir nada', 0),
(1121, 910, 'Elegir plantas que ya crecen bien en la zona', 1);

-- ==================== INSIGNIAS (12) ====================
INSERT INTO badge (id, code, name, description, iconKey, criteriaKey) VALUES
(1, 'PRIMER_GUARDIAN', 'Primer Guardián', 'Completaste tu primer reto en el Valle Verde.', 'badge_first', 'CHALLENGES>=1'),
(2, 'AMIGO_DEL_RIO', 'Amigo del Río', 'Dominaste por completo la zona del Río Claro.', 'badge_river', 'ZONE_MASTERED:RIO'),
(3, 'PROTECTOR_DEL_BOSQUE', 'Protector del Bosque', 'Dominaste por completo la zona del Bosque.', 'badge_forest', 'ZONE_MASTERED:BOSQUE'),
(4, 'EXPLORADOR_RESPONSABLE', 'Explorador Responsable', 'Completaste 8 retos en el Valle Verde.', 'badge_explorer', 'CHALLENGES>=8'),
(5, 'DETECTIVE_VERDE', 'Detective Verde', 'Descubriste 10 problemas ambientales escondidos.', 'badge_detective', 'ISSUES>=10'),
(6, 'BUEN_OBSERVADOR', 'Buen Observador', 'Descubriste 5 problemas ambientales escondidos.', 'badge_observer', 'ISSUES>=5'),
(7, 'CONSTRUCTOR_RESPONSABLE', 'Constructor Responsable', 'Completaste 3 misiones de reparación.', 'badge_builder', 'RESTORATIONS>=3'),
(8, 'RESTAURADOR', 'Restaurador', 'Completaste 6 misiones de reparación.', 'badge_restorer', 'RESTORATIONS>=6'),
(9, 'PROTECTOR_DE_ANIMALES', 'Protector de Animales', 'Dominaste por completo la zona del Refugio Animal.', 'badge_animals', 'ZONE_MASTERED:ANIMALES'),
(10, 'DEFENSOR_DEL_VALLE', 'Defensor del Valle', 'Tomaste 5 decisiones correctas de autorización ambiental.', 'badge_defender', 'AUTHORIZATIONS>=5'),
(11, 'GRAN_GUARDIAN', 'Gran Guardián', 'Desbloqueaste todas las zonas del Valle Verde.', 'badge_grand', 'ALL_ZONES_UNLOCKED'),
(12, 'MAESTRO_VERDELEGAL', 'Maestro VerdeLegal', 'Dominaste por completo todas las zonas del Valle Verde.', 'badge_master', 'ALL_ZONES_MASTERED');
