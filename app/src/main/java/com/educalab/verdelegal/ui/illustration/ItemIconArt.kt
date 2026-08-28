package com.educalab.verdelegal.ui.illustration

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.educalab.verdelegal.ui.theme.*

/**
 * Iconografía para problemas ambientales (Detective Verde), elementos de
 * reparación (arrastrar/soltar) y actividades de autorización.
 *
 * En vez de 40+ dibujos completamente distintos, las claves se agrupan por
 * categoría visual (agua, planta, señal, objeto, animal, herramienta...) para
 * mantener un estilo coherente en toda la aplicación, tal como exige la
 * especificación, sin sacrificar variedad reconocible por el niño.
 */
@Composable
fun ItemIcon(key: String, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.aspectRatio(1f)) {
        val s = size.minDimension
        val c = Offset(s / 2, s / 2)
        drawRoundRect(
            color = CreamBg,
            topLeft = Offset(s * 0.05f, s * 0.05f),
            size = Size(s * 0.9f, s * 0.9f),
            cornerRadius = CornerRadius(s * 0.18f)
        )
        drawCategoryGlyph(categoryFor(key), s, c)
    }
}

private enum class IconCategory { WATER, PLANT, SIGN, TRASH, ANIMAL, TOOL, PAPER, STRUCTURE, PEOPLE, CHECK }

private fun categoryFor(key: String): IconCategory = when {
    "water" in key || "hose" in key || "foam" in key || "bowl" in key -> IconCategory.WATER
    "seed" in key || "plant" in key || "flower" in key || "reed" in key || "weed" in key || "compost" in key || "leaves" in key -> IconCategory.PLANT
    "sign" in key || "restsign" in key -> IconCategory.SIGN
    "bag" in key || "trash" in key || "litter" in key || "bottle" in key || "debris" in key || "paper" in key -> IconCategory.TRASH
    "fish" in key || "donation" in key -> IconCategory.ANIMAL
    "hammer" in key || "planks" in key || "net" in key || "stones" in key || "hosefix" in key || "shade" in key -> IconCategory.TOOL
    "branch" in key -> IconCategory.STRUCTURE
    "fence" in key || "bin" in key -> IconCategory.STRUCTURE
    "trail" in key || "fair" in key || "mural" in key || "visit" in key || "camera" in key || "fishing" in key || "planting" in key -> IconCategory.PEOPLE
    "check" in key -> IconCategory.CHECK
    else -> IconCategory.TRASH
}

private fun DrawScope.drawCategoryGlyph(category: IconCategory, s: Float, c: Offset) {
    when (category) {
        IconCategory.WATER -> {
            val drop = Path().apply {
                moveTo(c.x, c.y - s * 0.20f)
                cubicTo(c.x + s * 0.14f, c.y - s * 0.02f, c.x + s * 0.14f, c.y + s * 0.14f, c.x, c.y + s * 0.14f)
                cubicTo(c.x - s * 0.14f, c.y + s * 0.14f, c.x - s * 0.14f, c.y - s * 0.02f, c.x, c.y - s * 0.20f)
                close()
            }
            drawPath(drop, color = RiverBlue)
        }
        IconCategory.PLANT -> {
            drawLine(SoilBrown, Offset(c.x, c.y + s * 0.16f), Offset(c.x, c.y - s * 0.02f), strokeWidth = s * 0.025f)
            drawCircle(color = ForestLight, radius = s * 0.10f, center = Offset(c.x - s * 0.06f, c.y - s * 0.08f))
            drawCircle(color = ForestMid, radius = s * 0.10f, center = Offset(c.x + s * 0.06f, c.y - s * 0.08f))
        }
        IconCategory.SIGN -> {
            drawLine(SoilBrown, Offset(c.x, c.y + s * 0.18f), Offset(c.x, c.y - s * 0.02f), strokeWidth = s * 0.025f)
            val tri = Path().apply {
                moveTo(c.x, c.y - s * 0.22f)
                lineTo(c.x + s * 0.14f, c.y - s * 0.02f)
                lineTo(c.x - s * 0.14f, c.y - s * 0.02f)
                close()
            }
            drawPath(tri, color = SunGold)
        }
        IconCategory.TRASH -> {
            drawRoundRect(
                color = AlertCoral.copy(alpha = 0.85f),
                topLeft = Offset(c.x - s * 0.12f, c.y - s * 0.12f),
                size = Size(s * 0.24f, s * 0.24f),
                cornerRadius = CornerRadius(s * 0.03f)
            )
            drawLine(CardWhite, Offset(c.x - s * 0.06f, c.y - s * 0.06f), Offset(c.x + s * 0.06f, c.y + s * 0.06f), strokeWidth = s * 0.02f)
            drawLine(CardWhite, Offset(c.x + s * 0.06f, c.y - s * 0.06f), Offset(c.x - s * 0.06f, c.y + s * 0.06f), strokeWidth = s * 0.02f)
        }
        IconCategory.ANIMAL -> {
            drawOval(color = RiverBlue, topLeft = Offset(c.x - s * 0.16f, c.y - s * 0.06f), size = Size(s * 0.28f, s * 0.14f))
            val tail = Path().apply {
                moveTo(c.x - s * 0.16f, c.y)
                lineTo(c.x - s * 0.24f, c.y - s * 0.06f)
                lineTo(c.x - s * 0.24f, c.y + s * 0.06f)
                close()
            }
            drawPath(tail, color = RiverBlue)
        }
        IconCategory.TOOL -> {
            drawRoundRect(color = SoilBrown, topLeft = Offset(c.x - s * 0.02f, c.y - s * 0.14f), size = Size(s * 0.04f, s * 0.22f), cornerRadius = CornerRadius(s * 0.01f))
            drawRoundRect(color = InkDark, topLeft = Offset(c.x - s * 0.10f, c.y - s * 0.16f), size = Size(s * 0.20f, s * 0.08f), cornerRadius = CornerRadius(s * 0.02f))
        }
        IconCategory.STRUCTURE -> {
            for (i in 0..2) {
                drawLine(SoilBrown, Offset(c.x - s * 0.14f + i * s * 0.14f, c.y - s * 0.14f), Offset(c.x - s * 0.14f + i * s * 0.14f, c.y + s * 0.14f), strokeWidth = s * 0.025f)
            }
            drawLine(SoilBrown, Offset(c.x - s * 0.16f, c.y - s * 0.04f), Offset(c.x + s * 0.16f, c.y - s * 0.04f), strokeWidth = s * 0.02f)
        }
        IconCategory.PEOPLE -> {
            drawCircle(color = SunGold, radius = s * 0.07f, center = Offset(c.x, c.y - s * 0.12f))
            val body = Path().apply {
                moveTo(c.x - s * 0.10f, c.y + s * 0.16f)
                quadraticBezierTo(c.x, c.y - s * 0.02f, c.x + s * 0.10f, c.y + s * 0.16f)
                close()
            }
            drawPath(body, color = ForestMid)
        }
        IconCategory.CHECK -> {
            drawCircle(color = ForestMid, radius = s * 0.16f, center = c, style = Stroke(width = s * 0.02f))
            drawPath(Path().apply {
                moveTo(c.x - s * 0.07f, c.y)
                lineTo(c.x - s * 0.015f, c.y + s * 0.06f)
                lineTo(c.x + s * 0.08f, c.y - s * 0.06f)
            }, color = ForestMid, style = Stroke(width = s * 0.025f))
        }
    }
}
