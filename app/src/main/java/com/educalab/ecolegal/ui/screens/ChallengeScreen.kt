package com.educalab.ecolegal.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ecolegal.data.local.entity.ChallengeType
import com.educalab.ecolegal.domain.model.DecisionOption
import com.educalab.ecolegal.domain.model.IssueInfo
import com.educalab.ecolegal.ui.components.*
import com.educalab.ecolegal.ui.illustration.LumaCharacter
import com.educalab.ecolegal.ui.illustration.LumaPose
import com.educalab.ecolegal.ui.theme.*
import com.educalab.ecolegal.ui.viewmodel.ChallengeViewModel
import com.educalab.ecolegal.ui.viewmodel.SemaforoColor
import com.educalab.ecolegal.ui.viewmodel.ecoLegalViewModel
import kotlinx.coroutines.delay

@Composable
fun ChallengeScreen(userId: Long, challengeId: Long, onBack: () -> Unit) {
    val viewModel = ecoLegalViewModel(key = "challenge_$challengeId") { repo -> ChallengeViewModel(repo, userId, challengeId) }
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(CreamBg)) {
        TopAppBar(
            title = { Text(uiState.challenge?.title ?: "", fontWeight = FontWeight.Bold) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBg)
        )

        if (uiState.loading || uiState.challenge == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = ForestMid) }
            return@Column
        }
        val challenge = uiState.challenge!!

        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            LumaSpeechBubble(text = challenge.prompt, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChallengeTypeChip(type = challenge.type)
                XpChip(xp = challenge.xpReward)
            }
            Spacer(Modifier.height(16.dp))

            val result = uiState.result
            if (result != null) {
                val tone = when {
                    result.success -> FeedbackTone.SUCCESS
                    result.partial -> FeedbackTone.PARTIAL
                    else -> FeedbackTone.INCORRECT
                }
                val title = when {
                    result.success -> "¡Muy bien! +${result.xpAwarded} XP"
                    result.partial -> "¡Buen intento! +${result.xpAwarded} XP"
                    else -> "Vamos a intentarlo de nuevo"
                }
                FeedbackBanner(tone = tone, title = title, explanation = result.explanation, modifier = Modifier.fillMaxWidth())
                result.consequence?.let {
                    Spacer(Modifier.height(10.dp))
                    Text("Consecuencia: ${it.description}", style = MaterialTheme.typography.bodyMedium, color = InkDark.copy(alpha = 0.75f))
                }
                Spacer(Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (!result.success) {
                        OutlinedButton(onClick = { viewModel.retry() }) { Text("Intentar de nuevo") }
                    }
                    Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = ForestMid)) { Text("Volver a la zona") }
                }
            } else {
                when (challenge.type) {
                    ChallengeType.DECISION -> DecisionBody(options = uiState.options, onChoose = viewModel::submitSingleDecision)
                    ChallengeType.SEMAFORO -> SemaforoBody(
                        options = uiState.options,
                        assignments = uiState.semaforoAssignments,
                        onAssign = viewModel::assignSemaforo
                    )
                    ChallengeType.DETECTIVE_FIND -> DetectiveBody(
                        issues = uiState.issues,
                        found = uiState.foundIssueIds,
                        onFind = viewModel::markIssueFound
                    )
                    ChallengeType.ORDER_STEPS -> OrderStepsBody(
                        options = uiState.options,
                        orderedIds = uiState.orderedIds,
                        onMove = viewModel::moveStep,
                        onConfirm = viewModel::confirmOrder
                    )
                    ChallengeType.DRAG_RESTORE, ChallengeType.AUTHORIZATION -> {
                        // Estos tipos se juegan en sus propias pantallas (RestorationScreen / AuthorizationScreen),
                        // nunca se instancian como reto dentro de ChallengeScreen.
                    }
                }
            }
        }
    }
}

@Composable
private fun ChallengeTypeChip(type: ChallengeType, modifier: Modifier = Modifier) {
    val (emoji, label) = when (type) {
        ChallengeType.DECISION -> "⚖️" to "Decisión"
        ChallengeType.SEMAFORO -> "🚦" to "Semáforo"
        ChallengeType.DETECTIVE_FIND -> "🔍" to "Detective"
        ChallengeType.ORDER_STEPS -> "📋" to "Ordenar"
        ChallengeType.DRAG_RESTORE -> "🛠️" to "Reparar"
        ChallengeType.AUTHORIZATION -> "📝" to "Autorización"
    }
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(RiverBlue.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(emoji, style = MaterialTheme.typography.labelLarge)
        Text(label, fontWeight = FontWeight.Bold, color = InkDark)
    }
}

private val optionAccents = listOf(ForestMid, RiverBlue, SunGold.copy(alpha = 0.9f), AlertCoral, ForestDeep)

@Composable
private fun DecisionBody(options: List<DecisionOption>, onChoose: (DecisionOption) -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🤔", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.width(8.dp))
            Text("¿Qué harías tú?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = InkDark)
        }
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            itemsIndexed(options) { index, option ->
                val accent = optionAccents[index % optionAccents.size]
                DecisionOptionCard(index = index, accent = accent, text = option.text, onClick = { onChoose(option) })
            }
        }
    }
}

@Composable
private fun DecisionOptionCard(index: Int, accent: Color, text: String, onClick: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 90L)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(320)) + slideInVertically(tween(320)) { it / 3 }
    ) {
        Surface(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            color = accent.copy(alpha = 0.12f),
            border = androidx.compose.foundation.BorderStroke(1.dp, accent.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(32.dp).clip(CircleShape).background(accent),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${index + 1}", color = textColorFor(accent), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                }
                Spacer(Modifier.width(12.dp))
                Text(text, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge, color = InkDark)
                Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = accent)
            }
        }
    }
}

@Composable
private fun SemaforoBody(
    options: List<DecisionOption>,
    assignments: Map<Long, SemaforoColor>,
    onAssign: (Long, SemaforoColor) -> Unit
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        itemsIndexed(options) { index, option ->
            val assigned = assignments[option.id]
            val tint = optionAccents[index % optionAccents.size]
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = tint.copy(alpha = 0.10f))) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(option.text, style = MaterialTheme.typography.bodyLarge, color = InkDark)
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SemaforoDot(SemaforoColor.VERDE, ForestMid, assigned) { onAssign(option.id, SemaforoColor.VERDE) }
                        SemaforoDot(SemaforoColor.AMARILLO, SunGold, assigned) { onAssign(option.id, SemaforoColor.AMARILLO) }
                        SemaforoDot(SemaforoColor.ROJO, AlertCoral, assigned) { onAssign(option.id, SemaforoColor.ROJO) }
                    }
                }
            }
        }
    }
}

@Composable
private fun SemaforoDot(color: SemaforoColor, tint: Color, assigned: SemaforoColor?, onClick: () -> Unit) {
    val selected = assigned == color
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(if (selected) tint else tint.copy(alpha = 0.18f))
            .border(2.dp, tint, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (selected) Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White)
    }
}

@Composable
private fun DetectiveBody(issues: List<IssueInfo>, found: Set<Long>, onFind: (Long) -> Unit) {
    Text("🕵️ Toca cada elemento que podría ser un problema.", style = MaterialTheme.typography.bodyMedium, color = InkDark.copy(alpha = 0.7f))
    Spacer(Modifier.height(12.dp))
    Text("${found.size}/${issues.size} encontrados", style = MaterialTheme.typography.labelLarge, color = ForestMid)
    Spacer(Modifier.height(8.dp))
    ProgressBar(progress = if (issues.isEmpty()) 0f else found.size.toFloat() / issues.size, modifier = Modifier.fillMaxWidth())
    Spacer(Modifier.height(16.dp))
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(issues) { issue ->
            val isFound = issue.id in found
            Surface(
                onClick = { if (!isFound) onFind(issue.id) },
                shape = RoundedCornerShape(14.dp),
                color = if (isFound) ForestLight.copy(alpha = 0.3f) else CardWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isFound) ForestMid else Color(0xFFE0E0E0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    com.educalab.ecolegal.ui.illustration.ItemIcon(key = issue.iconKey, modifier = Modifier.size(40.dp))
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(issue.title, fontWeight = FontWeight.SemiBold, color = InkDark)
                        if (isFound) Text(issue.description, style = MaterialTheme.typography.bodyMedium, color = InkDark.copy(alpha = 0.7f))
                    }
                    if (isFound) Icon(Icons.Filled.Check, contentDescription = null, tint = ForestMid)
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.OrderStepsBody(
    options: List<DecisionOption>,
    orderedIds: List<Long>,
    onMove: (Int, Int) -> Unit,
    onConfirm: () -> Unit
) {
    Text("📋 Ordena los pasos usando las flechas.", style = MaterialTheme.typography.bodyMedium, color = InkDark.copy(alpha = 0.7f))
    Spacer(Modifier.height(12.dp))
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
        itemsIndexed(orderedIds) { index, id ->
            val option = options.firstOrNull { it.id == id } ?: return@itemsIndexed
            Card(shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = CardWhite)) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(28.dp).clip(CircleShape).background(ForestMid),
                        contentAlignment = Alignment.Center
                    ) { Text("${index + 1}", color = Color.White, fontWeight = FontWeight.Bold) }
                    Spacer(Modifier.width(10.dp))
                    Text(option.text, modifier = Modifier.weight(1f), color = InkDark)
                    IconButton(onClick = { if (index > 0) onMove(index, index - 1) }) {
                        Icon(Icons.Filled.ArrowUpward, contentDescription = "Subir")
                    }
                    IconButton(onClick = { if (index < orderedIds.lastIndex) onMove(index, index + 1) }) {
                        Icon(Icons.Filled.ArrowDownward, contentDescription = "Bajar")
                    }
                }
            }
        }
    }
    Spacer(Modifier.height(12.dp))
    Button(onClick = onConfirm, modifier = Modifier.fillMaxWidth().height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = ForestMid)) {
        Text("Confirmar orden")
    }
}
