package com.educalab.ecolegal.ui.illustration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import com.educalab.ecolegal.ui.theme.*

/**
 * Insignias ilustradas: una medalla circular con un emoji distinto por
 * insignia, más un anillo de color que varía según la categoría del logro.
 */
@Composable
fun BadgeIllustration(iconKey: String, locked: Boolean = false, modifier: Modifier = Modifier) {
    val ringColor = if (locked) Color(0xFFBFC7C2) else badgeRingColor(iconKey)
    val faceColor = if (locked) Color(0xFFE3E7E4) else CardWhite
    val emoji = if (locked) "🔒" else badgeEmoji(iconKey)

    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(ringColor),
        contentAlignment = Alignment.Center
    ) {
        val ringPadding = maxWidth * 0.08f
        BoxWithConstraints(
            modifier = Modifier
                .padding(ringPadding)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(faceColor),
            contentAlignment = Alignment.Center
        ) {
            val emojiSize = with(LocalDensity.current) { (maxWidth * 0.5f).toSp() }
            Text(emoji, fontSize = emojiSize)
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

private fun badgeEmoji(key: String): String = when (key) {
    "badge_first" -> "⭐"
    "badge_river" -> "💧"
    "badge_forest" -> "🌲"
    "badge_explorer" -> "🧭"
    "badge_detective" -> "🔍"
    "badge_observer" -> "👁️"
    "badge_builder" -> "🔨"
    "badge_restorer" -> "🌱"
    "badge_animals" -> "🐾"
    "badge_defender" -> "🛡️"
    "badge_grand" -> "🗝️"
    "badge_master" -> "👑"
    else -> "⭐"
}

/** 8 avatares locales: rostro de un animal del valle, sin fotos reales. */
@Composable
fun AvatarIllustration(key: String, modifier: Modifier = Modifier) {
    val (base, emoji) = avatarStyle(key)
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(base),
        contentAlignment = Alignment.Center
    ) {
        val emojiSize = with(LocalDensity.current) { (maxWidth * 0.58f).toSp() }
        Text(emoji, fontSize = emojiSize)
    }
}

private fun avatarStyle(key: String): Pair<Color, String> = when (key) {
    "avatar_zorro" -> SunGold.copy(alpha = 0.3f) to "🦊"
    "avatar_nutria" -> SoilBrown.copy(alpha = 0.3f) to "🦦"
    "avatar_buho" -> ForestDeep.copy(alpha = 0.25f) to "🦉"
    "avatar_tortuga" -> ForestMid.copy(alpha = 0.25f) to "🐢"
    "avatar_colibri" -> RiverBlue.copy(alpha = 0.25f) to "🐦"
    "avatar_conejo" -> SunLight.copy(alpha = 0.5f) to "🐰"
    "avatar_mapache" -> Color(0xFF6B6B6B).copy(alpha = 0.25f) to "🦝"
    "avatar_ardilla" -> Color(0xFFB5651D).copy(alpha = 0.25f) to "🐿️"
    else -> ForestLight.copy(alpha = 0.25f) to "🦊"
}
