package com.educalab.ecolegal.ui.screens

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ecolegal.domain.model.RestorationStepInfo
import com.educalab.ecolegal.ui.components.*
import com.educalab.ecolegal.ui.illustration.ItemIcon
import com.educalab.ecolegal.ui.illustration.LumaCharacter
import com.educalab.ecolegal.ui.illustration.LumaPose
import com.educalab.ecolegal.ui.theme.*
import com.educalab.ecolegal.ui.viewmodel.RestorationViewModel
import com.educalab.ecolegal.ui.viewmodel.ecoLegalViewModel

/**
 * Modo "Reparar el entorno". El niño elige, entre varias opciones, el elemento
 * correcto para cada paso de la misión y lo coloca sobre el destino resaltado
 * (mecánica de "elegir y colocar", una simplificación táctil documentada de un
 * arrastre físico completo — ver docs/MANUAL_TECNICO.md, sección de simplificaciones).
 */
@Composable
fun RestorationScreen(userId: Long, missionId: Long, onBack: () -> Unit) {
    val viewModel = ecoLegalViewModel(key = "restoration_$missionId") { repo -> RestorationViewModel(repo, userId, missionId) }
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(CreamBg)) {
        TopAppBar(
            title = { Text(uiState.mission?.title ?: "", fontWeight = FontWeight.Bold) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBg)
        )

        val mission = uiState.mission
        if (uiState.loading || mission == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = ForestMid) }
            return@Column
        }

        if (uiState.missionComplete) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LumaCharacter(pose = LumaPose.CELEBRATING, modifier = Modifier.size(120.dp))
                Spacer(Modifier.height(16.dp))
                Text("¡Misión de reparación completa!", style = MaterialTheme.typography.headlineMedium, color = InkDark, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("Ganaste ${mission.xpReward} XP. El Valle Verde se ve un poco mejor gracias a ti.", style = MaterialTheme.typography.bodyLarge, color = InkDark.copy(alpha = 0.75f))
                Spacer(Modifier.height(20.dp))
                Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = ForestMid)) { Text("Volver a la zona") }
            }
            return@Column
        }

        val nextStep = mission.steps.sortedBy { it.order }.firstOrNull { it.id !in uiState.completedStepIds }

        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Text("${uiState.completedStepIds.size}/${mission.steps.size} pasos completados", style = MaterialTheme.typography.labelLarge, color = ForestMid)
            Spacer(Modifier.height(8.dp))
            ProgressBar(progress = uiState.completedStepIds.size.toFloat() / mission.steps.size, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(20.dp))

            if (nextStep != null) {
                StepChooser(
                    step = nextStep,
                    allSteps = mission.steps,
                    feedback = uiState.lastPlacementCorrect,
                    onPick = { itemKey -> viewModel.attemptPlacement(nextStep, itemKey, nextStep.targetSlotKey) },
                    onFeedbackShown = viewModel::clearPlacementFeedback
                )
            }
        }
    }
}

@Composable
private fun StepChooser(
    step: RestorationStepInfo,
    allSteps: List<RestorationStepInfo>,
    feedback: Boolean?,
    onPick: (String) -> Unit,
    onFeedbackShown: () -> Unit
) {
    val distractors = remember(step.id) {
        allSteps.filter { it.id != step.id }.map { it.itemKey }.distinct().shuffled().take(2)
    }
    val choices = remember(step.id) { (distractors + step.itemKey).shuffled() }

    LaunchedEffect(feedback) {
        if (feedback != null) {
            kotlinx.coroutines.delay(900)
            onFeedbackShown()
        }
    }

    Text(step.description, style = MaterialTheme.typography.titleMedium, color = InkDark, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(6.dp))
    Text("Toca el elemento correcto y luego el lugar señalado para colocarlo.", style = MaterialTheme.typography.bodyMedium, color = InkDark.copy(alpha = 0.7f))
    Spacer(Modifier.height(20.dp))

    // "Destino" resaltado donde se debe colocar el elemento correcto.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(ForestLight.copy(alpha = 0.2f))
            .border(2.dp, ForestMid, RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("Lugar de reparación", color = ForestMid, fontWeight = FontWeight.SemiBold)
    }

    Spacer(Modifier.height(24.dp))
    Text("Elige el elemento correcto:", style = MaterialTheme.typography.titleMedium, color = InkDark)
    Spacer(Modifier.height(12.dp))

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
        choices.forEach { itemKey ->
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(CardWhite)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(18.dp))
                    .clickable { onPick(itemKey) },
                contentAlignment = Alignment.Center
            ) {
                ItemIcon(key = itemKey, modifier = Modifier.size(56.dp))
            }
        }
    }

    Spacer(Modifier.height(20.dp))

    if (feedback == true) {
        FeedbackBanner(tone = FeedbackTone.SUCCESS, title = "¡Colocado correctamente!", explanation = "Ese elemento ayuda a reparar esta parte de la zona.")
    } else if (feedback == false) {
        FeedbackBanner(tone = FeedbackTone.INCORRECT, title = "Ese no es el elemento correcto", explanation = "Observa bien la descripción del paso e inténtalo de nuevo.")
    }
}
