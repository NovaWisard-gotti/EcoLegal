package com.educalab.verdelegal.ui.illustration

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.educalab.verdelegal.ui.theme.*

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
    Canvas(modifier = modifier.aspectRatio(1f)) {
        val s = size.minDimension
        when (code) {
            "BOSQUE" -> drawForestIcon(s)
            "RIO" -> drawRiverIcon(s)
            "COMUNIDAD" -> drawCommunityIcon(s)
            "ANIMALES" -> drawAnimalsIcon(s)
            "AGRICOLA" -> drawFarmIcon(s)
            else -> drawForestIcon(s)
        }
    }
}

private fun DrawScope.drawForestIcon(s: Float) {
    drawCircle(color = ForestLight.copy(alpha = 0.25f), radius = s * 0.46f, center = Offset(s / 2, s / 2))
    fun tree(cx: Float, trunkH: Float, canopyR: Float, color: Color) {
        drawRoundRect(
            color = SoilBrown,
            topLeft = Offset(cx - s * 0.03f, s * 0.62f),
            size = Size(s * 0.06f, trunkH),
            cornerRadius = CornerRadius(s * 0.02f)
        )
        drawCircle(color = color, radius = canopyR, center = Offset(cx, s * 0.62f - canopyR * 0.55f))
    }
    tree(s * 0.32f, s * 0.16f, s * 0.16f, ForestMid)
    tree(s * 0.52f, s * 0.22f, s * 0.20f, ForestDeep)
    tree(s * 0.72f, s * 0.14f, s * 0.14f, ForestLight)
}

private fun DrawScope.drawRiverIcon(s: Float) {
    drawCircle(color = RiverLight.copy(alpha = 0.25f), radius = s * 0.46f, center = Offset(s / 2, s / 2))
    val wave = Path().apply {
        moveTo(s * 0.15f, s * 0.55f)
        cubicTo(s * 0.3f, s * 0.4f, s * 0.4f, s * 0.7f, s * 0.55f, s * 0.55f)
        cubicTo(s * 0.7f, s * 0.4f, s * 0.8f, s * 0.7f, s * 0.9f, s * 0.55f)
        lineTo(s * 0.9f, s * 0.8f)
        lineTo(s * 0.15f, s * 0.8f)
        close()
    }
    drawPath(wave, color = RiverBlue)
    drawCircle(color = RiverLight, radius = s * 0.06f, center = Offset(s * 0.34f, s * 0.28f))
    drawCircle(color = SunGold.copy(alpha = 0.8f), radius = s * 0.10f, center = Offset(s * 0.66f, s * 0.22f))
}

private fun DrawScope.drawCommunityIcon(s: Float) {
    drawCircle(color = SunLight.copy(alpha = 0.35f), radius = s * 0.46f, center = Offset(s / 2, s / 2))
    fun house(cx: Float, w: Float, h: Float, roof: Color, wall: Color) {
        drawRect(color = wall, topLeft = Offset(cx - w / 2, s * 0.55f), size = Size(w, h))
        val roofPath = Path().apply {
            moveTo(cx - w / 2 - w * 0.15f, s * 0.55f)
            lineTo(cx, s * 0.55f - h * 0.7f)
            lineTo(cx + w / 2 + w * 0.15f, s * 0.55f)
            close()
        }
        drawPath(roofPath, color = roof)
    }
    house(s * 0.32f, s * 0.22f, s * 0.2f, ForestDeep, CardWhite)
    house(s * 0.62f, s * 0.28f, s * 0.26f, RiverBlue, SunLight)
    drawRoundRect(color = ForestLight, topLeft = Offset(s * 0.1f, s * 0.78f), size = Size(s * 0.8f, s * 0.05f), cornerRadius = CornerRadius(s * 0.02f))
}

private fun DrawScope.drawAnimalsIcon(s: Float) {
    drawCircle(color = ForestLight.copy(alpha = 0.25f), radius = s * 0.46f, center = Offset(s / 2, s / 2))
    // silueta de huella (paw print)
    drawCircle(color = SoilBrown, radius = s * 0.16f, center = Offset(s * 0.5f, s * 0.6f))
    drawCircle(color = SoilBrown, radius = s * 0.075f, center = Offset(s * 0.33f, s * 0.42f))
    drawCircle(color = SoilBrown, radius = s * 0.08f, center = Offset(s * 0.44f, s * 0.32f))
    drawCircle(color = SoilBrown, radius = s * 0.08f, center = Offset(s * 0.58f, s * 0.32f))
    drawCircle(color = SoilBrown, radius = s * 0.075f, center = Offset(s * 0.68f, s * 0.42f))
}

private fun DrawScope.drawFarmIcon(s: Float) {
    drawCircle(color = SunLight.copy(alpha = 0.35f), radius = s * 0.46f, center = Offset(s / 2, s / 2))
    // espiga de trigo
    val stem = Path().apply {
        moveTo(s * 0.5f, s * 0.82f)
        lineTo(s * 0.5f, s * 0.30f)
    }
    drawPath(stem, color = ForestMid, style = androidx.compose.ui.graphics.drawscope.Stroke(width = s * 0.025f))
    for (i in 0..4) {
        val y = s * 0.34f + i * s * 0.09f
        drawOval(color = SunGold, topLeft = Offset(s * 0.5f, y), size = Size(s * 0.12f, s * 0.06f))
        drawOval(color = SunGold, topLeft = Offset(s * 0.5f - s * 0.12f, y), size = Size(s * 0.12f, s * 0.06f))
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
    Canvas(modifier = modifier) {
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
        drawCircle(color = SunGold.copy(alpha = 0.85f), radius = w * 0.09f, center = Offset(w * 0.82f, h * 0.18f))
    }
}
