package com.educalab.ecolegal.ui.illustration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import com.educalab.ecolegal.ui.theme.*

/**
 * Iconografía para problemas ambientales (Detective Verde), elementos de
 * reparación (arrastrar/soltar) y actividades de autorización.
 *
 * Cada elemento se identifica por un emoji congruente con lo que representa
 * (en vez de una figura geométrica abstracta), sobre una tarjeta con un tinte
 * de color propio de su categoría para dar variedad visual sin perder
 * coherencia con el resto de la app.
 */
@Composable
fun ItemIcon(key: String, modifier: Modifier = Modifier) {
    val (emoji, tint) = emojiAndTintFor(key)
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(18.dp))
            .background(tint.copy(alpha = 0.18f)),
        contentAlignment = Alignment.Center
    ) {
        val emojiSize = with(LocalDensity.current) { (maxWidth * 0.56f).toSp() }
        Text(emoji, fontSize = emojiSize, textAlign = TextAlign.Center)
    }
}

private fun emojiAndTintFor(key: String): Pair<String, Color> = when {
    "donation" in key -> "🎁" to SunGold
    "fishing" in key -> "🎣" to RiverBlue
    "fish" in key -> "🐟" to RiverBlue
    "flowerseed" in key -> "🌸" to SunGold
    "bottle" in key -> "🧴" to AlertCoral
    "bowl" in key -> "🥣" to RiverBlue
    "foam" in key -> "🫧" to RiverLight
    "hosefix" in key -> "🔧" to SoilBrown
    "hose" in key -> "🚰" to RiverBlue
    "water" in key -> "💧" to RiverBlue
    "reed" in key -> "🌾" to ForestLight
    "weed" in key -> "🌿" to ForestMid
    "compost" in key -> "🍂" to SoilBrown
    "leaves" in key -> "🍂" to SoilBrown
    "seed" in key || "planting" in key -> "🌱" to ForestMid
    "plant" in key -> "🌿" to ForestMid
    "branch" in key -> "🪵" to SoilBrown
    "restsign" in key -> "🪧" to ForestMid
    "sign" in key -> "🪧" to SunGold
    "trashbag" in key -> "🗑️" to InkDark
    "bag" in key -> "🛍️" to AlertCoral
    "litter" in key -> "🚮" to AlertCoral
    "trash" in key -> "🗑️" to InkDark
    "debris" in key || "stones" in key -> "🪨" to SoilBrown
    "paper" in key -> "📄" to SoilBrown
    "hammer" in key -> "🔨" to SoilBrown
    "planks" in key -> "🪵" to SoilBrown
    "net" in key -> "🥅" to RiverBlue
    "shade" in key -> "⛱️" to SunGold
    "check" in key -> "✅" to ForestMid
    "camera" in key -> "📷" to InkDark
    "fair" in key -> "🎪" to SunGold
    "mural" in key -> "🎨" to AlertCoral
    "trail" in key -> "🥾" to SoilBrown
    "visit" in key -> "👥" to RiverBlue
    "fence" in key -> "🚧" to SunGold
    else -> "🍃" to ForestMid
}
