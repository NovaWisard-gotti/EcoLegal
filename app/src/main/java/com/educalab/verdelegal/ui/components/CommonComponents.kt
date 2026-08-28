package com.educalab.verdelegal.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.verdelegal.data.local.entity.ZoneStatus
import com.educalab.verdelegal.ui.theme.*

/** Estado visual de un módulo: SIEMPRE icono + texto, nunca solo color (sección 19). */
@Composable
fun ZoneStatusBadge(status: ZoneStatus, modifier: Modifier = Modifier) {
    val (icon, label, color) = when (status) {
        ZoneStatus.LOCKED -> Triple(Icons.Filled.Lock, "Bloqueada", Color(0xFF9AA39D))
        ZoneStatus.AVAILABLE -> Triple(Icons.Filled.Explore, "Disponible", RiverBlue)
        ZoneStatus.STARTED -> Triple(Icons.Filled.HourglassTop, "En camino", SunGold)
        ZoneStatus.COMPLETED -> Triple(Icons.Filled.CheckCircle, "Completada", ForestMid)
        ZoneStatus.MASTERED -> Triple(Icons.Filled.WorkspacePremium, "Dominada", ForestDeep)
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Text(label, color = color, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun XpChip(xp: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(SunGold.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(Icons.Filled.Bolt, contentDescription = null, tint = SunGold, modifier = Modifier.size(18.dp))
        Text("$xp XP", fontWeight = FontWeight.Bold, color = InkDark)
    }
}

@Composable
fun BadgeCountChip(count: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(ForestMid.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(Icons.Filled.WorkspacePremium, contentDescription = null, tint = ForestMid, modifier = Modifier.size(18.dp))
        Text("$count", fontWeight = FontWeight.Bold, color = InkDark)
    }
}

@Composable
fun ProgressBar(progress: Float, modifier: Modifier = Modifier, color: Color = ForestMid) {
    val animated by animateFloatAsState(targetValue = progress.coerceIn(0f, 1f), animationSpec = tween(500), label = "progress")
    Box(
        modifier = modifier
            .height(10.dp)
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.18f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animated)
                .clip(RoundedCornerShape(50))
                .background(color)
        )
    }
}

enum class FeedbackTone { SUCCESS, PARTIAL, INCORRECT }

@Composable
fun FeedbackBanner(tone: FeedbackTone, title: String, explanation: String, modifier: Modifier = Modifier) {
    val (bg, accent, icon) = when (tone) {
        FeedbackTone.SUCCESS -> Triple(ForestLight.copy(alpha = 0.25f), ForestMid, Icons.Filled.CheckCircle)
        FeedbackTone.PARTIAL -> Triple(SunLight.copy(alpha = 0.5f), SunGold, Icons.Filled.Info)
        FeedbackTone.INCORRECT -> Triple(AlertCoral.copy(alpha = 0.15f), AlertCoral, Icons.Filled.Refresh)
    }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, contentDescription = null, tint = accent)
            Text(title, style = MaterialTheme.typography.titleMedium, color = accent, fontWeight = FontWeight.Bold)
        }
        Text(explanation, style = MaterialTheme.typography.bodyLarge, color = InkDark)
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String? = null, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.titleLarge, color = InkDark, fontWeight = FontWeight.Bold)
        if (subtitle != null) {
            Spacer(Modifier.height(2.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = InkDark.copy(alpha = 0.7f))
        }
    }
}

@Composable
fun LumaSpeechBubble(text: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(ForestLight.copy(alpha = 0.25f)),
            contentAlignment = Alignment.Center
        ) {
            com.educalab.verdelegal.ui.illustration.LumaCharacter(modifier = Modifier.size(44.dp))
        }
        Spacer(Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp))
                .background(CardWhite)
                .padding(12.dp)
        ) {
            Text(text, style = MaterialTheme.typography.bodyLarge, color = InkDark)
        }
    }
}
