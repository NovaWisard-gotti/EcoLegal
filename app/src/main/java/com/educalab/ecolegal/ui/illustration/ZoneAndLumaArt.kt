package com.educalab.ecolegal.ui.illustration

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.educalab.ecolegal.ui.theme.*

/**
 * Motor de ilustración basado en Compose Canvas (ver ESPECIFICACIÓN MAESTRA §4,
 * prioridad 3: "Ilustraciones generadas mediante Compose Canvas"). Todas las
 * ilustraciones son locales, vectoriales y funcionan 100% sin conexión.
 *
 * Cada zona, insignia, avatar y estado del personaje guía Luma se identifica
 * por una "key" textual, para que el resto de la app solo declare qué quiere
 * mostrar sin preocuparse por el dibujo.
 */

@Composable
fun ZoneIllustration(code: String, modifier: Modifier = Modifier) {
    val (emoji, tint) = when (code) {
        "BOSQUE" -> "🌳" to ForestLight
        "RIO" -> "🌊" to RiverBlue
        "COMUNIDAD" -> "🏘️" to SunGold
        "ANIMALES" -> "🐾" to SoilBrown
        "AGRICOLA" -> "🌾" to SunGold
        else -> "🌳" to ForestLight
    }
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(tint.copy(alpha = 0.25f)),
        contentAlignment = Alignment.Center
    ) {
        val emojiSize = with(LocalDensity.current) { (maxWidth * 0.56f).toSp() }
        Text(emoji, fontSize = emojiSize)
    }
}

/** Estado visual de Luma, la exploradora ambiental que guía al niño. */
enum class LumaPose { NEUTRAL, CELEBRATING, THINKING, POINTING }

@Composable
fun LumaCharacter(pose: LumaPose = LumaPose.NEUTRAL, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.aspectRatio(1f)) {
        val s = size.minDimension
        drawLuma(s, pose)
    }
}

private fun DrawScope.drawLuma(s: Float, pose: LumaPose) {
    val bodyCenter = Offset(s * 0.5f, s * 0.56f)
    // cuerpo tipo hoja/espíritu del bosque
    val body = Path().apply {
        moveTo(bodyCenter.x, bodyCenter.y - s * 0.30f)
        cubicTo(bodyCenter.x + s * 0.26f, bodyCenter.y - s * 0.20f, bodyCenter.x + s * 0.22f, bodyCenter.y + s * 0.24f, bodyCenter.x, bodyCenter.y + s * 0.30f)
        cubicTo(bodyCenter.x - s * 0.22f, bodyCenter.y + s * 0.24f, bodyCenter.x - s * 0.26f, bodyCenter.y - s * 0.20f, bodyCenter.x, bodyCenter.y - s * 0.30f)
        close()
    }
    drawPath(body, color = ForestLight)
    // vena central de la hoja
    drawPath(
        Path().apply {
            moveTo(bodyCenter.x, bodyCenter.y - s * 0.26f)
            lineTo(bodyCenter.x, bodyCenter.y + s * 0.26f)
        },
        color = ForestDeep,
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = s * 0.015f)
    )
    // mejillas / brillo
    drawCircle(color = SunLight.copy(alpha = 0.55f), radius = s * 0.07f, center = Offset(bodyCenter.x - s * 0.12f, bodyCenter.y - s * 0.02f))

    // ojos
    val eyeY = bodyCenter.y - s * 0.06f
    val eyeOffset = if (pose == LumaPose.THINKING) s * 0.02f else 0f
    drawCircle(color = InkDark, radius = s * 0.035f, center = Offset(bodyCenter.x - s * 0.08f, eyeY + eyeOffset))
    drawCircle(color = InkDark, radius = s * 0.035f, center = Offset(bodyCenter.x + s * 0.08f, eyeY - eyeOffset))

    // boca según pose
    val mouth = Path()
    when (pose) {
        LumaPose.CELEBRATING -> mouth.apply {
            moveTo(bodyCenter.x - s * 0.08f, bodyCenter.y + s * 0.06f)
            quadraticBezierTo(bodyCenter.x, bodyCenter.y + s * 0.16f, bodyCenter.x + s * 0.08f, bodyCenter.y + s * 0.06f)
        }
        LumaPose.THINKING -> mouth.apply {
            moveTo(bodyCenter.x - s * 0.05f, bodyCenter.y + s * 0.09f)
            lineTo(bodyCenter.x + s * 0.05f, bodyCenter.y + s * 0.09f)
        }
        else -> mouth.apply {
            moveTo(bodyCenter.x - s * 0.06f, bodyCenter.y + s * 0.07f)
            quadraticBezierTo(bodyCenter.x, bodyCenter.y + s * 0.12f, bodyCenter.x + s * 0.06f, bodyCenter.y + s * 0.07f)
        }
    }
    drawPath(mouth, color = InkDark, style = androidx.compose.ui.graphics.drawscope.Stroke(width = s * 0.018f))

    // pequeñas antenas con brillo (luciérnaga exploradora)
    drawCircle(color = SunGold, radius = s * 0.025f, center = Offset(bodyCenter.x - s * 0.10f, bodyCenter.y - s * 0.32f))
    drawCircle(color = SunGold, radius = s * 0.025f, center = Offset(bodyCenter.x + s * 0.10f, bodyCenter.y - s * 0.32f))

    if (pose == LumaPose.CELEBRATING) {
        // pequeñas chispas alrededor
        listOf(-0.34f to -0.10f, 0.32f to -0.14f, -0.28f to 0.30f, 0.30f to 0.28f).forEach { (dx, dy) ->
            drawCircle(color = SunGold, radius = s * 0.02f, center = Offset(bodyCenter.x + s * dx, bodyCenter.y + s * dy))
        }
    }
}

/** Fondo decorativo suave reutilizable (colinas onduladas) para pantallas de portada. */
@Composable
fun ValleyBackdrop(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val hill1 = Path().apply {
                moveTo(0f, h * 0.55f)
                cubicTo(w * 0.25f, h * 0.40f, w * 0.5f, h * 0.62f, w, h * 0.45f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            val hill2 = Path().apply {
                moveTo(0f, h * 0.70f)
                cubicTo(w * 0.3f, h * 0.58f, w * 0.6f, h * 0.78f, w, h * 0.62f)
                lineTo(w, h)
                lineTo(0f, h)
                close()
            }
            drawPath(hill1, color = ForestMid.copy(alpha = 0.55f))
            drawPath(hill2, color = ForestDeep.copy(alpha = 0.75f))
        }
        Text(
            "☀️",
            fontSize = with(LocalDensity.current) { 34.dp.toSp() },
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 18.dp, end = 28.dp)
        )
    }
}
