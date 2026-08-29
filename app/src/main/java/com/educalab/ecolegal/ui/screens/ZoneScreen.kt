package com.educalab.ecolegal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ecolegal.data.local.entity.ChallengeType
import com.educalab.ecolegal.domain.model.AuthorizationActivityInfo
import com.educalab.ecolegal.domain.model.ChallengeInfo
import com.educalab.ecolegal.domain.model.RestorationMissionInfo
import com.educalab.ecolegal.ui.components.*
import com.educalab.ecolegal.ui.illustration.ItemIcon
import com.educalab.ecolegal.ui.illustration.LumaCharacter
import com.educalab.ecolegal.ui.illustration.LumaPose
import com.educalab.ecolegal.ui.illustration.ZoneIllustration
import com.educalab.ecolegal.ui.theme.*
import com.educalab.ecolegal.ui.viewmodel.ZoneViewModel
import com.educalab.ecolegal.ui.viewmodel.ecoLegalViewModel

@Composable
fun ZoneScreen(
    userId: Long,
    zoneId: Long,
    onBack: () -> Unit,
    onOpenChallenge: (Long) -> Unit,
    onOpenRestoration: (Long) -> Unit,
    onOpenAuthorization: (Long) -> Unit
) {
    val viewModel = ecoLegalViewModel(key = "zone_$zoneId") { repo -> ZoneViewModel(repo, userId, zoneId) }
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(CreamBg)) {
        TopAppBar(
            title = { Text(uiState.zone?.displayName ?: "", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBg)
        )

        if (uiState.loading || uiState.zone == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = ForestMid) }
            return@Column
        }

        val zone = uiState.zone!!

        LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .background(ForestLight.copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) { ZoneIllustration(code = zone.code, modifier = Modifier.size(56.dp)) }
                    Spacer(Modifier.width(14.dp))
                    Column {
                        Text(zone.displayName, style = MaterialTheme.typography.headlineMedium, color = InkDark, fontWeight = FontWeight.Bold)
                        uiState.progress?.let {
                            Text("${it.xp} XP en esta zona", style = MaterialTheme.typography.bodyMedium, color = InkDark.copy(alpha = 0.6f))
                        }
                    }
                }
            }

            item {
                LumaSpeechBubble(
                    text = "Esta zona tiene varias situaciones para explorar. ¡Elige por dónde empezar!",
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item { SectionHeader(title = "Situaciones y retos") }

            items(uiState.scenarios) { swc ->
                ScenarioBlock(scenarioTitle = swc.scenario.title, lumaIntro = swc.scenario.lumaIntro, challenges = swc.challenges, onOpenChallenge = onOpenChallenge)
            }

            if (uiState.restorationMissions.isNotEmpty()) {
                item { SectionHeader(title = "Reparar el entorno", subtitle = "Ayuda a restaurar esta zona paso a paso.") }
                items(uiState.restorationMissions) { mission ->
                    RestorationCard(mission = mission, onClick = { onOpenRestoration(mission.id) })
                }
            }

            if (uiState.authorizationActivities.isNotEmpty()) {
                item { SectionHeader(title = "Ruta de la autorización", subtitle = "Revisa actividades y decide con responsabilidad.") }
                items(uiState.authorizationActivities) { activity ->
                    AuthorizationCard(activity = activity, onClick = { onOpenAuthorization(activity.id) })
                }
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun ScenarioBlock(
    scenarioTitle: String,
    lumaIntro: String,
    challenges: List<ChallengeInfo>,
    onOpenChallenge: (Long) -> Unit
) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = CardWhite)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(scenarioTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = InkDark)
            Spacer(Modifier.height(4.dp))
            Text(lumaIntro, style = MaterialTheme.typography.bodyMedium, color = InkDark.copy(alpha = 0.7f))
            Spacer(Modifier.height(10.dp))
            challenges.forEach { challenge ->
                ChallengeRow(challenge = challenge, onClick = { onOpenChallenge(challenge.id) })
                if (challenge != challenges.last()) Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ChallengeRow(challenge: ChallengeInfo, onClick: () -> Unit) {
    val (icon, label) = when (challenge.type) {
        ChallengeType.DECISION -> Icons.Filled.Balance to "Decisión"
        ChallengeType.SEMAFORO -> Icons.Filled.Traffic to "Semáforo"
        ChallengeType.DETECTIVE_FIND -> Icons.Filled.Search to "Detective"
        ChallengeType.ORDER_STEPS -> Icons.Filled.FormatListNumbered to "Ordenar"
        ChallengeType.DRAG_RESTORE -> Icons.Filled.Build to "Reparar"
        ChallengeType.AUTHORIZATION -> Icons.Filled.Gavel to "Autorización"
    }
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        color = CreamBg,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = ForestMid, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(challenge.title, style = MaterialTheme.typography.titleMedium, color = InkDark, fontWeight = FontWeight.SemiBold)
                Text("$label · ${challenge.xpReward} XP", style = MaterialTheme.typography.labelLarge, color = InkDark.copy(alpha = 0.6f))
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = InkDark.copy(alpha = 0.4f))
        }
    }
}

@Composable
private fun RestorationCard(mission: RestorationMissionInfo, onClick: () -> Unit) {
    Surface(onClick = onClick, shape = RoundedCornerShape(18.dp), color = ForestLight.copy(alpha = 0.15f), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            ItemIcon(key = mission.steps.firstOrNull()?.itemKey ?: "item_seed", modifier = Modifier.size(48.dp))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(mission.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = InkDark)
                Text(mission.description, style = MaterialTheme.typography.bodyMedium, color = InkDark.copy(alpha = 0.7f), maxLines = 2)
                Text("${mission.steps.size} pasos · ${mission.xpReward} XP", style = MaterialTheme.typography.labelLarge, color = ForestMid)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = InkDark.copy(alpha = 0.4f))
        }
    }
}

@Composable
private fun AuthorizationCard(activity: AuthorizationActivityInfo, onClick: () -> Unit) {
    Surface(onClick = onClick, shape = RoundedCornerShape(18.dp), color = RiverLight.copy(alpha = 0.2f), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            ItemIcon(key = activity.iconKey, modifier = Modifier.size(48.dp))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(activity.activityName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = InkDark)
                Text(activity.description, style = MaterialTheme.typography.bodyMedium, color = InkDark.copy(alpha = 0.7f), maxLines = 2)
            }
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = InkDark.copy(alpha = 0.4f))
        }
    }
}
