package com.educalab.verdelegal.ui.illustration

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.educalab.verdelegal.ui.theme.*

/**
 * Insignias ilustradas: una medalla circular con un símbolo interior distinto
 * por insignia, más un anillo de color que varía según la categoría del logro.
 */
@Composable
fun BadgeIllustration(iconKey: String, locked: Boolean = false, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.aspectRatio(1f)) {
        val s = size.minDimension
        val ringColor = if (locked) Color(0xFFBFC7C2) else badgeRingColor(iconKey)
        val faceColor = if (locked) Color(0xFFE3E7E4) else CardWhite

        drawCircle(color = ringColor, radius = s * 0.48f, center = Offset(s / 2, s / 2))
        drawCircle(color = faceColor, radius = s * 0.40f, center = Offset(s / 2, s / 2))
        drawCircle(color = ringColor.copy(alpha = 0.5f), radius = s * 0.40f, center = Offset(s / 2, s / 2), style = Stroke(width = s * 0.02f))

        if (locked) {
            drawLockGlyph(s)
        } else {
            drawBadgeGlyph(iconKey, s, ringColor)
        }
    }
}

private fun badgeRingColor(key: String): Color = when (key) {
    "badge_first" -> SunGold
    "badge_river", "badge_defender" -> RiverBlue
    "badge_forest", "badge_builder", "badge_restorer" -> ForestMid
    "badge_explorer", "badge_detective", "badge_observer" -> ForestDeep
    "badge_animals" -> SoilBrown
    "badge_grand", "badge_master" -> SunGold
    else -> ForestMid
}

private fun DrawScope.drawLockGlyph(s: Float) {
    val c = Offset(s / 2, s / 2)
    drawRoundRect(
        color = Color(0xFF9AA39D),
        topLeft = Offset(c.x - s * 0.10f, c.y - s * 0.02f),
        size = androidx.compose.ui.geometry.Size(s * 0.20f, s * 0.16f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(s * 0.02f)
    )
    drawArc(
        color = Color(0xFF9AA39D),
        startAngle = 180f, sweepAngle = 180f, useCenter = false,
        topLeft = Offset(c.x - s * 0.07f, c.y - s * 0.14f),
        size = androidx.compose.ui.geometry.Size(s * 0.14f, s * 0.16f),
        style = Stroke(width = s * 0.02f)
    )
}

private fun DrawScope.drawBadgeGlyph(key: String, s: Float, accent: Color) {
    val c = Offset(s / 2, s / 2)
    when (key) {
        "badge_first" -> { // estrella
            drawStar(c, s * 0.16f, s * 0.07f, accent)
        }
        "badge_river" -> { // gota
            drawDrop(c, s * 0.14f, RiverBlue)
        }
        "badge_forest" -> { // pino
            drawPine(c, s * 0.16f, ForestMid)
        }
        "badge_explorer" -> { // brújula
            drawCircle(color = accent, radius = s * 0.14f, center = c, style = Stroke(width = s * 0.018f))
            drawPath(Path().apply {
                moveTo(c.x, c.y - s * 0.11f); lineTo(c.x + s * 0.05f, c.y); lineTo(c.x, c.y + s * 0.11f); lineTo(c.x - s * 0.05f, c.y); close()
            }, color = accent)
        }
        "badge_detective" -> { // lupa
            drawCircle(color = accent, radius = s * 0.10f, center = Offset(c.x - s * 0.03f, c.y - s * 0.03f), style = Stroke(width = s * 0.02f))
            drawLine(color = accent, start = Offset(c.x + s * 0.04f, c.y + s * 0.04f), end = Offset(c.x + s * 0.13f, c.y + s * 0.13f), strokeWidth = s * 0.025f)
        }
        "badge_observer" -> { // ojo
            drawOval(color = accent, topLeft = Offset(c.x - s * 0.14f, c.y - s * 0.07f), size = androidx.compose.ui.geometry.Size(s * 0.28f, s * 0.14f), style = Stroke(width = s * 0.018f))
            drawCircle(color = accent, radius = s * 0.045f, center = c)
        }
        "badge_builder" -> { // martillo
            drawRoundRect(color = accent, topLeft = Offset(c.x - s * 0.02f, c.y - s * 0.14f), size = androidx.compose.ui.geometry.Size(s * 0.04f, s * 0.22f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(s * 0.01f))
            drawRoundRect(color = accent, topLeft = Offset(c.x - s * 0.10f, c.y - s * 0.16f), size = androidx.compose.ui.geometry.Size(s * 0.20f, s * 0.08f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(s * 0.02f))
        }
        "badge_restorer" -> { // brote / planta
            drawPine(c, s * 0.14f, ForestLight)
            drawRoundRect(color = SoilBrown, topLeft = Offset(c.x - s * 0.02f, c.y + s * 0.06f), size = androidx.compose.ui.geometry.Size(s * 0.04f, s * 0.10f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(s * 0.01f))
        }
        "badge_animals" -> { // huella
            drawCircle(color = accent, radius = s * 0.09f, center = Offset(c.x, c.y + s * 0.03f))
            drawCircle(color = accent, radius = s * 0.04f, center = Offset(c.x - s * 0.09f, c.y - s * 0.07f))
            drawCircle(color = accent, radius = s * 0.045f, center = Offset(c.x + s * 0.09f, c.y - s * 0.07f))
        }
        "badge_defender" -> { // escudo
            val shield = Path().apply {
                moveTo(c.x, c.y - s * 0.14f)
                lineTo(c.x + s * 0.11f, c.y - s * 0.08f)
                lineTo(c.x + s * 0.11f, c.y + s * 0.04f)
                cubicTo(c.x + s * 0.11f, c.y + s * 0.14f, c.x, c.y + s * 0.18f, c.x, c.y + s * 0.18f)
                cubicTo(c.x, c.y + s * 0.18f, c.x - s * 0.11f, c.y + s * 0.14f, c.x - s * 0.11f, c.y + s * 0.04f)
                lineTo(c.x - s * 0.11f, c.y - s * 0.08f)
                close()
            }
            drawPath(shield, color = accent)
        }
        "badge_grand" -> { // llave / mapa desbloqueado
            drawCircle(color = accent, radius = s * 0.07f, center = Offset(c.x - s * 0.08f, c.y))
            drawRoundRect(color = accent, topLeft = Offset(c.x - s * 0.01f, c.y - s * 0.025f), size = androidx.compose.ui.geometry.Size(s * 0.16f, s * 0.05f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(s * 0.01f))
        }
        "badge_master" -> { // corona
            val crown = Path().apply {
                moveTo(c.x - s * 0.14f, c.y + s * 0.08f)
                lineTo(c.x - s * 0.14f, c.y - s * 0.02f)
                lineTo(c.x - s * 0.06f, c.y + s * 0.04f)
                lineTo(c.x, c.y - s * 0.10f)
                lineTo(c.x + s * 0.06f, c.y + s * 0.04f)
                lineTo(c.x + s * 0.14f, c.y - s * 0.02f)
                lineTo(c.x + s * 0.14f, c.y + s * 0.08f)
                close()
            }
            drawPath(crown, color = SunGold)
        }
        else -> drawStar(c, s * 0.14f, s * 0.06f, accent)
    }
}

private fun DrawScope.drawStar(center: Offset, outerR: Float, innerR: Float, color: Color) {
    val path = Path()
    for (i in 0 until 10) {
        val angle = Math.PI / 5 * i - Math.PI / 2
        val r = if (i % 2 == 0) outerR else innerR
        val x = center.x + (r * Math.cos(angle)).toFloat()
        val y = center.y + (r * Math.sin(angle)).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color = color)
}

private fun DrawScope.drawDrop(center: Offset, r: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x, center.y - r * 1.3f)
        cubicTo(center.x + r, center.y - r * 0.2f, center.x + r, center.y + r, center.x, center.y + r)
        cubicTo(center.x - r, center.y + r, center.x - r, center.y - r * 0.2f, center.x, center.y - r * 1.3f)
        close()
    }
    drawPath(path, color = color)
}

private fun DrawScope.drawPine(center: Offset, r: Float, color: Color) {
    val path = Path().apply {
        moveTo(center.x, center.y - r * 1.2f)
        lineTo(center.x + r, center.y + r * 0.2f)
        lineTo(center.x + r * 0.5f, center.y + r * 0.2f)
        lineTo(center.x + r * 0.9f, center.y + r * 0.9f)
        lineTo(center.x - r * 0.9f, center.y + r * 0.9f)
        lineTo(center.x - r * 0.5f, center.y + r * 0.2f)
        lineTo(center.x - r, center.y + r * 0.2f)
        close()
    }
    drawPath(path, color = color)
}

/** 8 avatares locales: rostro circular de un animal del valle, sin fotos reales. */
@Composable
fun AvatarIllustration(key: String, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.aspectRatio(1f)) {
        val s = size.minDimension
        val c = Offset(s / 2, s / 2)
        val (base, accent) = avatarColors(key)
        drawCircle(color = base, radius = s * 0.46f, center = c)
        drawAvatarFeatures(key, s, c, accent)
    }
}

private fun avatarColors(key: String): Pair<Color, Color> = when (key) {
    "avatar_zorro" -> SunGold to Color.White
    "avatar_nutria" -> SoilBrown to CreamBg
    "avatar_buho" -> ForestDeep to SunLight
    "avatar_tortuga" -> ForestMid to ForestLight
    "avatar_colibri" -> RiverBlue to SunGold
    "avatar_conejo" -> CreamBg to SoilBrown
    "avatar_mapache" -> Color(0xFF6B6B6B) to Color.White
    "avatar_ardilla" -> Color(0xFFB5651D) to CreamBg
    else -> ForestMid to CreamBg
}

private fun DrawScope.drawAvatarFeatures(key: String, s: Float, c: Offset, accent: Color) {
    // orejas genéricas (varían de forma sutil por especie)
    val earR = s * 0.11f
    val earDX = s * 0.20f
    val earDY = s * 0.24f
    when (key) {
        "avatar_buho" -> {
            drawCircle(color = accent, radius = earR * 0.9f, center = Offset(c.x - earDX, c.y - earDY))
            drawCircle(color = accent, radius = earR * 0.9f, center = Offset(c.x + earDX, c.y - earDY))
        }
        else -> {
            drawCircle(color = accent, radius = earR, center = Offset(c.x - earDX, c.y - earDY))
            drawCircle(color = accent, radius = earR, center = Offset(c.x + earDX, c.y - earDY))
        }
    }
    // ojos
    drawCircle(color = InkDark, radius = s * 0.035f, center = Offset(c.x - s * 0.09f, c.y - s * 0.02f))
    drawCircle(color = InkDark, radius = s * 0.035f, center = Offset(c.x + s * 0.09f, c.y - s * 0.02f))
    // nariz/pico
    drawPath(Path().apply {
        moveTo(c.x - s * 0.03f, c.y + s * 0.05f)
        lineTo(c.x + s * 0.03f, c.y + s * 0.05f)
        lineTo(c.x, c.y + s * 0.10f)
        close()
    }, color = InkDark)
}
