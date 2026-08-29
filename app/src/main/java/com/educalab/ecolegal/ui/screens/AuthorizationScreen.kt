package com.educalab.ecolegal.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ecolegal.data.local.entity.AuthorizationChoice
import com.educalab.ecolegal.data.local.entity.SeverityLevel
import com.educalab.ecolegal.domain.model.ImpactInfo
import com.educalab.ecolegal.domain.model.MeasureInfo
import com.educalab.ecolegal.ui.components.*
import com.educalab.ecolegal.ui.illustration.ItemIcon
import com.educalab.ecolegal.ui.theme.*
import com.educalab.ecolegal.ui.viewmodel.AuthorizationViewModel
import com.educalab.ecolegal.ui.viewmodel.ecoLegalViewModel

private val stepAccent = RiverBlue

@Composable
fun AuthorizationScreen(userId: Long, activityId: Long, onBack: () -> Unit) {
    val viewModel = ecoLegalViewModel(key = "auth_$activityId") { repo -> AuthorizationViewModel(repo, userId, activityId) }
    val uiState by viewModel.uiState.collectAsState()
    var step by remember(activityId) { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().background(CreamBg)) {
        TopAppBar(
            title = { Text("Ruta de la autorización", fontWeight = FontWeight.Bold) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBg)
        )

        val activity = uiState.activity
        if (uiState.loading || activity == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = ForestMid) }
            return@Column
        }

        val outcome = uiState.outcome
        if (outcome != null) {
            Column(modifier = Modifier.fillMaxSize().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (outcome.userChoiceIsCorrect) "🎉" else "🤔", style = MaterialTheme.typography.displayMedium)
                Spacer(Modifier.height(8.dp))
                FeedbackBanner(
                    tone = if (outcome.userChoiceIsCorrect) FeedbackTone.SUCCESS else FeedbackTone.PARTIAL,
                    title = if (outcome.userChoiceIsCorrect) "¡Buena decisión!" else "Revisemos juntos",
                    explanation = outcome.explanation,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (!outcome.userChoiceIsCorrect) {
                        OutlinedButton(onClick = { step = 0; viewModel.reset() }) { Text("Intentar de nuevo") }
                    }
                    Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = ForestMid)) { Text("Volver a la zona") }
                }
            }
            return@Column
        }

        Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ItemIcon(key = activity.iconKey, modifier = Modifier.size(52.dp))
                Spacer(Modifier.width(12.dp))
                Text(activity.activityName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = InkDark, modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(14.dp))
            StepDots(current = step, total = 3, labels = listOf("🔎 Impactos", "🛡️ Medidas", "✅ Decisión"))
            Spacer(Modifier.height(16.dp))

            Box(modifier = Modifier.weight(1f)) {
                AnimatedContent(
                    targetState = step,
                    label = "auth_step",
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally(tween(280)) { it } + fadeIn(tween(280))) togetherWith
                                (slideOutHorizontally(tween(280)) { -it } + fadeOut(tween(280)))
                        } else {
                            (slideInHorizontally(tween(280)) { -it } + fadeIn(tween(280))) togetherWith
                                (slideOutHorizontally(tween(280)) { it } + fadeOut(tween(280)))
                        }
                    }
                ) { s ->
                    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                        when (s) {
                            0 -> ImpactsStep(description = activity.description, impacts = activity.impacts)
                            1 -> MeasuresStep(measures = activity.measures, selectedIds = uiState.selectedMeasureIds, onToggle = viewModel::toggleMeasure)
                            else -> DecisionStep(onChoose = viewModel::decide)
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }
            }

            if (step < 2) {
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    if (step > 0) {
                        OutlinedButton(onClick = { step-- }) { Text("Atrás") }
                    } else {
                        Spacer(Modifier.width(1.dp))
                    }
                    Button(onClick = { step++ }, colors = ButtonDefaults.buttonColors(containerColor = stepAccent)) {
                        Text("Siguiente", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun StepDots(current: Int, total: Int, labels: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        for (i in 0 until total) {
            val done = i < current
            val active = i == current
            val color = when {
                active -> stepAccent
                done -> SuccessGreen
                else -> Color(0xFFE0E0E0)
            }
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(color.copy(alpha = if (active) 0.2f else 0.15f))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(if (active || done) color else Color(0xFFBFC7C2)))
                if (active) {
                    Spacer(Modifier.width(6.dp))
                    Text(labels[i], style = MaterialTheme.typography.labelLarge, color = InkDark, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ImpactsStep(description: String, impacts: List<ImpactInfo>) {
    Text(description, style = MaterialTheme.typography.bodyLarge, color = InkDark.copy(alpha = 0.85f))
    Spacer(Modifier.height(16.dp))
    SectionHeader(title = "🔎 Posibles impactos", subtitle = "Toca cada tarjeta para descubrir qué podría pasar.")
    Spacer(Modifier.height(10.dp))
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        impacts.forEach { impact -> ImpactFlipCard(impact) }
    }
}

@Composable
private fun ImpactFlipCard(impact: ImpactInfo) {
    var revealed by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(targetValue = if (revealed) 180f else 0f, animationSpec = tween(450), label = "flip")
    val (emoji, color) = when (impact.level) {
        SeverityLevel.HIGH -> "🔴" to AlertCoral
        SeverityLevel.MEDIUM -> "🟡" to SunGold
        SeverityLevel.LOW -> "🟢" to ForestMid
    }
    Surface(
        onClick = { revealed = !revealed },
        shape = RoundedCornerShape(18.dp),
        color = color.copy(alpha = 0.14f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 14f * density
            }
    ) {
        Box(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            if (rotation <= 90f) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.width(10.dp))
                    Text("Toca para descubrir qué podría pasar…", style = MaterialTheme.typography.bodyMedium, color = InkDark.copy(alpha = 0.65f))
                }
            } else {
                Row(
                    modifier = Modifier.graphicsLayer { rotationY = 180f },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(emoji, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.width(10.dp))
                    Text(impact.text, style = MaterialTheme.typography.bodyMedium, color = InkDark, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun MeasuresStep(measures: List<MeasureInfo>, selectedIds: Set<Long>, onToggle: (MeasureInfo) -> Unit) {
    SectionHeader(title = "🛡️ Elige medidas de cuidado", subtitle = "Selecciona las que ayudarían a proteger el lugar.")
    Spacer(Modifier.height(10.dp))
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        measures.forEach { measure ->
            val selected = measure.id in selectedIds
            MeasureChip(text = measure.text, selected = selected, onClick = { onToggle(measure) })
        }
    }
}

@Composable
private fun MeasureChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(targetValue = if (selected) 1.02f else 1f, animationSpec = tween(150), label = "measure_scale")
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) ForestMid.copy(alpha = 0.16f) else CardWhite,
        border = BorderStroke(if (selected) 2.dp else 1.dp, if (selected) ForestMid else Color(0xFFE0E0E0)),
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer { scaleX = scale; scaleY = scale }
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(if (selected) "✅" else "🛡️", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(10.dp))
            Text(
                text,
                style = MaterialTheme.typography.bodyLarge,
                color = InkDark,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DecisionStep(onChoose: (AuthorizationChoice) -> Unit) {
    SectionHeader(title = "✅ Tu decisión", subtitle = "¿Qué harías con lo que descubriste?")
    Spacer(Modifier.height(12.dp))
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        DecisionBigCard("✅", "Autorizar", "Dar luz verde a la actividad", ForestMid) { onChoose(AuthorizationChoice.AUTORIZAR) }
        DecisionBigCard("✋", "Solicitar cambios", "Pedir que se ajuste antes de aprobar", SunGold) { onChoose(AuthorizationChoice.SOLICITAR_CAMBIOS) }
        DecisionBigCard("🚫", "No autorizar", "Rechazar la actividad", AlertCoral) { onChoose(AuthorizationChoice.NO_AUTORIZAR) }
    }
}

@Composable
private fun DecisionBigCard(emoji: String, label: String, sublabel: String, color: Color, onClick: () -> Unit) {
    val textColor = textColorFor(color)
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = color,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(label, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = textColor)
                Text(sublabel, style = MaterialTheme.typography.bodySmall, color = textColor.copy(alpha = 0.85f))
            }
        }
    }
}
