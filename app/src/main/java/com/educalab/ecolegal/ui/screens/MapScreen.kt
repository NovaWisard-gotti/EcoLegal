package com.educalab.ecolegal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ecolegal.data.local.entity.ZoneStatus
import com.educalab.ecolegal.data.repository.EcoLegalRepository
import com.educalab.ecolegal.domain.model.ZoneInfo
import com.educalab.ecolegal.ui.components.*
import com.educalab.ecolegal.ui.illustration.ValleyBackdrop
import com.educalab.ecolegal.ui.illustration.ZoneIllustration
import com.educalab.ecolegal.ui.theme.*
import com.educalab.ecolegal.ui.viewmodel.MapViewModel
import com.educalab.ecolegal.ui.viewmodel.ecoLegalViewModel

@Composable
fun MapScreen(
    userId: Long,
    onOpenZone: (Long) -> Unit,
    onOpenBadges: () -> Unit
) {
    val viewModel = ecoLegalViewModel(key = "map_$userId") { repo -> MapViewModel(repo, userId) }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) { viewModel.refresh() }

    Box(modifier = Modifier.fillMaxSize().background(CreamBg)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Cabecera tipo "portada" del Valle Verde
            Box(modifier = Modifier.fillMaxWidth().height(232.dp)) {
                ValleyBackdrop(modifier = Modifier.fillMaxSize())
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text("Valle Verde", style = MaterialTheme.typography.headlineMedium, color = CardWhite, fontWeight = FontWeight.ExtraBold)
                            Text("Tu mapa de aventuras", style = MaterialTheme.typography.bodyMedium, color = SunLight)
                        }
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(CardWhite)
                                .clickable { onOpenBadges() }
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Filled.WorkspacePremium, contentDescription = "Insignias", tint = SunGold)
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        XpChip(xp = uiState.totalXp)
                        BadgeCountChip(count = uiState.badgeCount)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            SectionHeader(
                title = "Elige una zona para explorar",
                subtitle = "Cada zona esconde situaciones distintas para descubrir.",
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(12.dp))

            if (uiState.loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = ForestMid) }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(uiState.zones) { overview ->
                        ZoneMapCard(
                            zone = overview.zone,
                            status = overview.progress.status,
                            challengesDone = overview.progress.challengesCompleted,
                            challengesTotal = overview.progress.totalChallenges,
                            onClick = { if (overview.progress.status != ZoneStatus.LOCKED) onOpenZone(overview.zone.id) }
                        )
                    }
                    item { Spacer(Modifier.height(12.dp)) }
                }
            }
        }
    }
}

@Composable
private fun ZoneMapCard(
    zone: ZoneInfo,
    status: ZoneStatus,
    challengesDone: Int,
    challengesTotal: Int,
    onClick: () -> Unit
) {
    val locked = status == ZoneStatus.LOCKED
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = if (locked) CardWhite.copy(alpha = 0.6f) else CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = if (locked) 0.dp else 3.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(if (locked) Color(0xFFEDEDED) else ForestLight.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (locked) {
                    Icon(Icons.Filled.WorkspacePremium, contentDescription = null, tint = Color(0xFFBFC7C2))
                } else {
                    ZoneIllustration(code = zone.code, modifier = Modifier.size(50.dp))
                }
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(zone.displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = InkDark)
                Text(
                    if (locked) "Consigue más insignias para desbloquear esta zona" else zone.shortDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = InkDark.copy(alpha = 0.7f),
                    maxLines = 2
                )
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ZoneStatusBadge(status = status)
                    if (!locked && challengesTotal > 0) {
                        Text("$challengesDone/$challengesTotal retos", style = MaterialTheme.typography.labelLarge, color = InkDark.copy(alpha = 0.6f))
                    }
                }
                if (!locked && challengesTotal > 0) {
                    Spacer(Modifier.height(6.dp))
                    ProgressBar(progress = challengesDone.toFloat() / challengesTotal, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}
