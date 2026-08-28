package com.educalab.verdelegal.data.local.seed

import com.educalab.verdelegal.data.local.entity.Challenge
import com.educalab.verdelegal.data.local.entity.Decision
import com.educalab.verdelegal.data.local.entity.DecisionOutcome
import com.educalab.verdelegal.data.local.entity.Consequence

/**
 * 15 retos (3 por zona), variando entre DECISION, SEMAFORO, DETECTIVE_FIND y
 * ORDER_STEPS para no reducir la app a un cuestionario de opción múltiple.
 *
 * La tabla Decision se reutiliza con distinto significado según el tipo de reto:
 *  - DECISION: cada Decision es una alternativa (isCorrect / isPartial).
 *  - SEMAFORO: cada Decision es una acción a clasificar (verde/amarillo/rojo,
 *    usando la misma codificación isCorrect/isPartial).
 *  - ORDER_STEPS: cada Decision es un paso; decisionOrder marca el orden correcto.
 */
object ChallengeSeed {

    val challenges = listOf(
        Challenge(301, 101, "DECISION", "Basura en el sendero", "Encontramos basura en el sendero del bosque. ¿Qué hacemos?", 1, 20, 1),
        Challenge(302, 101, "DETECTIVE_FIND", "Detective en el bosque", "Toca los elementos que podrían ser un problema para el bosque.", 1, 15, 2),
        Challenge(303, 102, "ORDER_STEPS", "Cuidar un árbol protegido", "Ordena los pasos para cuidar correctamente un árbol protegido.", 2, 20, 3),

        Challenge(304, 105, "DECISION", "Algo extraño en el agua", "Vemos espuma extraña en el río. ¿Qué hacemos primero?", 2, 20, 1),
        Challenge(305, 107, "SEMAFORO", "Semáforo del río", "Clasifica estas acciones cerca del río.", 2, 20, 2),
        Challenge(306, 106, "DETECTIVE_FIND", "Detective en la orilla", "Explora la orilla y encuentra lo que no debería estar ahí.", 1, 15, 3),

        Challenge(307, 110, "DECISION", "Día de reciclaje", "¿Dónde va una botella de plástico vacía?", 1, 15, 1),
        Challenge(308, 109, "ORDER_STEPS", "Parque en orden", "Ordena los pasos para dejar el parque en buen estado.", 1, 20, 2),
        Challenge(309, 111, "SEMAFORO", "Semáforo de la fiesta", "Clasifica estas acciones durante la fiesta comunitaria.", 2, 20, 3),

        Challenge(310, 113, "DECISION", "La cerca rota", "La cerca del refugio está rota. ¿Qué hacemos?", 2, 20, 1),
        Challenge(311, 113, "DETECTIVE_FIND", "Detective en el refugio", "Observa el refugio y encuentra lo que necesita atención.", 1, 15, 2),
        Challenge(312, 115, "SEMAFORO", "Semáforo del estanque", "Clasifica estas acciones cerca del estanque de los patos.", 2, 20, 3),

        Challenge(313, 117, "DECISION", "El campo sediento", "El cultivo necesita agua. ¿Cuál es la mejor manera de regar?", 2, 20, 1),
        Challenge(314, 118, "ORDER_STEPS", "Cuidar la tierra", "Ordena los pasos para cuidar la tierra del huerto.", 2, 20, 2),
        Challenge(315, 117, "DETECTIVE_FIND", "Detective en el campo", "Encuentra lo que el campo necesita revisar.", 1, 15, 3)
    )

    val decisions = listOf(
        // --- 301 (decision) ---
        Decision(401, 301, "Recogerla y llevarla a un cesto de reciclaje", true, false, 1),
        Decision(402, 301, "Dejarla ahí, alguien más la recogerá", false, false, 2),
        Decision(403, 301, "Empujarla fuera del sendero con el pie", false, true, 3),
        // --- 303 (order steps) ---
        Decision(404, 303, "Observar la señal de zona protegida", true, false, 1),
        Decision(405, 303, "No cortar ramas ni hojas", true, false, 2),
        Decision(406, 303, "Caminar solo por el sendero marcado", true, false, 3),
        Decision(407, 303, "Contarle a un adulto lo que aprendiste", true, false, 4),

        // --- 304 (decision) ---
        Decision(408, 304, "Observar de dónde viene el problema antes de actuar", true, false, 1),
        Decision(409, 304, "Meter las manos al agua para revisarla", false, false, 2),
        Decision(410, 304, "Contarle a Luma lo que encontramos", false, true, 3),
        // --- 305 (semaforo) ---
        Decision(411, 305, "Recoger un envase que ves flotando cerca de la orilla", true, false, 1),
        Decision(412, 305, "Lavar platos directamente en el río", false, false, 2),
        Decision(413, 305, "Acercarse a mirar el río sin tocar el agua", false, true, 3),

        // --- 307 (decision) ---
        Decision(414, 307, "En el contenedor de plástico", true, false, 1),
        Decision(415, 307, "En cualquier contenedor, no importa cuál", false, false, 2),
        // --- 308 (order steps) ---
        Decision(416, 308, "Recoger los papeles sueltos", true, false, 1),
        Decision(417, 308, "Separar lo que se puede reciclar", true, false, 2),
        Decision(418, 308, "Colocar la rama caída a un lado del camino", true, false, 3),
        Decision(419, 308, "Avisar si algo necesita reparación", true, false, 4),
        // --- 309 (semaforo) ---
        Decision(420, 309, "Usar vasos reutilizables en la fiesta", true, false, 1),
        Decision(421, 309, "Dejar todos los residuos tirados al terminar", false, false, 2),
        Decision(422, 309, "Usar globos que después quedan sueltos en el pasto", false, true, 3),

        // --- 310 (decision) ---
        Decision(423, 310, "Avisar para repararla y evitar que los animales se lastimen", true, false, 1),
        Decision(424, 310, "Dejarla así, seguro nadie nota la diferencia", false, false, 2),
        // --- 312 (semaforo) ---
        Decision(425, 312, "Observar a los patos desde una distancia segura", true, false, 1),
        Decision(426, 312, "Perseguir a los patos para verlos de cerca", false, false, 2),
        Decision(427, 312, "Acercarse mucho para tomar una foto", false, true, 3),

        // --- 313 (decision) ---
        Decision(428, 313, "Regar en la mañana o en la tarde, cuando hace menos calor", true, false, 1),
        Decision(429, 313, "Regar todo el día sin control", false, false, 2),
        Decision(430, 313, "Reparar primero la manguera que gotea", false, true, 3),
        // --- 314 (order steps) ---
        Decision(431, 314, "Revisar si la tierra está muy seca o dañada", true, false, 1),
        Decision(432, 314, "Agregar abono natural si hace falta", true, false, 2),
        Decision(433, 314, "Dejar descansar una parte de la tierra", true, false, 3),
        Decision(434, 314, "Sembrar de nuevo cuando la tierra esté lista", true, false, 4)
    )

    val consequences = listOf(
        Consequence(601, "La basura puede quedarse mucho tiempo en el bosque y afectar a las plantas y animales cercanos.", "MEDIUM", "consequence_litter_forest", 701),
        Consequence(602, "La basura sigue en el bosque, solo cambió de lugar.", "LOW", "consequence_litter_moved", 701),
        Consequence(603, "Sin observar primero, podríamos no entender qué originó el problema en el agua.", "LOW", "consequence_river_rush", 703),
        Consequence(604, "El agua del río puede afectar a los peces y otros animales si se usa sin cuidado.", "HIGH", "consequence_river_wash", 703),
        Consequence(605, "Los residuos de la fiesta pueden afectar el pasto y la plaza si nadie los recoge.", "MEDIUM", "consequence_party_litter", 706),
        Consequence(606, "Una cerca rota puede dejar salir a los animales o dejar entrar peligros.", "MEDIUM", "consequence_fence_broken", 707),
        Consequence(607, "Acercarse demasiado puede asustar a los patos y alejarlos de su hogar.", "LOW", "consequence_ducks_scared", 708),
        Consequence(608, "Regar sin control puede desperdiciar agua que otros cultivos también necesitan.", "MEDIUM", "consequence_water_waste", 709)
    )

    val decisionOutcomes = listOf(
        DecisionOutcome(501, 401, "¡Muy bien! Recoger la basura y llevarla a un cesto ayuda a que el sendero se mantenga sano.", null),
        DecisionOutcome(502, 402, "Si la dejamos ahí, puede afectar a las plantas y animales que viven cerca.", 601),
        DecisionOutcome(503, 403, "Empujarla ayuda un poco, pero el problema sigue porque la basura no salió del bosque.", 602),

        DecisionOutcome(504, 408, "¡Excelente decisión! Observar primero ayuda a entender el problema sin correr riesgos.", null),
        DecisionOutcome(505, 409, "Tocar un agua que no conocemos puede no ser seguro. Mejor observar primero.", 604),
        DecisionOutcome(506, 410, "Contarle a alguien es una gran idea, pero primero conviene observar la situación.", 603),

        DecisionOutcome(507, 411, "¡Correcto! Recoger un envase que flota ayuda a mantener el río limpio.", null),
        DecisionOutcome(508, 412, "Lavar platos en el río puede afectar el agua que usan los animales.", 604),
        DecisionOutcome(509, 413, "Observar con cuidado está bien, pero podríamos hacer algo más para ayudar.", null),

        DecisionOutcome(510, 414, "¡Correcto! El plástico va en su propio contenedor para poder reciclarse.", null),
        DecisionOutcome(511, 415, "Si mezclamos los residuos, es más difícil reciclarlos correctamente.", null),

        DecisionOutcome(512, 420, "¡Buena elección! Los vasos reutilizables generan menos residuos.", null),
        DecisionOutcome(513, 421, "Dejar todo tirado afecta la plaza y el trabajo de quienes la cuidan.", 605),
        DecisionOutcome(514, 422, "Los globos sueltos pueden terminar como basura en el pasto o en el río.", 605),

        DecisionOutcome(515, 423, "¡Bien pensado! Avisar a tiempo evita que los animales corran peligro.", null),
        DecisionOutcome(516, 424, "Una cerca rota puede ser un problema serio para los animales del refugio.", 606),

        DecisionOutcome(517, 425, "¡Correcto! Observar desde lejos mantiene tranquilos a los patos.", null),
        DecisionOutcome(518, 426, "Perseguir a los patos los asusta y puede lastimarlos.", 607),
        DecisionOutcome(519, 427, "Acercarse mucho puede incomodar a los patos, aunque sea solo para una foto.", 607),

        DecisionOutcome(520, 428, "¡Muy bien! Regar en horas frescas ayuda a que el agua se aproveche mejor.", null),
        DecisionOutcome(521, 429, "Regar sin control desperdicia agua que otros cultivos también necesitan.", 608),
        DecisionOutcome(522, 430, "Reparar la manguera es una gran idea, pero no responde cuándo es mejor regar.", null)
    )
}
