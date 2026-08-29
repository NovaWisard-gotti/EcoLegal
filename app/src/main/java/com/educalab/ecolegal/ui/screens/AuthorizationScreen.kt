package com.educalab.ecolegal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.educalab.ecolegal.data.local.entity.AuthorizationChoice
import com.educalab.ecolegal.data.local.entity.SeverityLevel
import com.educalab.ecolegal.ui.components.*
import com.educalab.ecolegal.ui.illustration.ItemIcon
import com.educalab.ecolegal.ui.theme.*
import com.educalab.ecolegal.ui.viewmodel.AuthorizationViewModel
import com.educalab.ecolegal.ui.viewmodel.ecoLegalViewModel

@Composable
fun AuthorizationScreen(userId: Long, activityId: Long, onBack: () -> Unit) {
    val viewModel = ecoLegalViewModel(key = "auth_$activityId") { repo -> AuthorizationViewModel(repo, userId, activityId) }
    val uiState by viewModel.uiState.collectAsState()

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
            Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
                FeedbackBanner(
                    tone = if (outcome.userChoiceIsCorrect) FeedbackTone.SUCCESS else FeedbackTone.PARTIAL,
                    title = if (outcome.userChoiceIsCorrect) "¡Buena decisión!" else "Revisemos juntos",
                    explanation = outcome.explanation,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(20.dp))
                Button(onClick = onBack, colors = ButtonDefaults.buttonColors(containerColor = ForestMid)) { Text("Volver a la zona") }
            }
            return@Column
        }

        Column(modifier = Modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState())) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ItemIcon(key = activity.iconKey, modifier = Modifier.size(56.dp))
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(activity.activityName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = InkDark)
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(activity.description, style = MaterialTheme.typography.bodyLarge, color = InkDark.copy(alpha = 0.8f))

            Spacer(Modifier.height(20.dp))
            SectionHeader(title = "Posibles impactos")
            Spacer(Modifier.height(8.dp))
            activity.impacts.forEach { impact ->
                val color = when (impact.level) {
                    SeverityLevel.HIGH -> AlertCoral
                    SeverityLevel.MEDIUM -> SunGold
                    SeverityLevel.LOW -> ForestMid
                }
                Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(10.dp).background(color, androidx.compose.foundation.shape.CircleShape))
                    Spacer(Modifier.width(10.dp))
                    Text(impact.text, style = MaterialTheme.typography.bodyMedium, color = InkDark)
                }
            }

            Spacer(Modifier.height(20.dp))
            SectionHeader(title = "Elige medidas de cuidado", subtitle = "Selecciona las que ayudarían a proteger el lugar.")
            Spacer(Modifier.height(8.dp))
            activity.measures.forEach { measure ->
                val selected = measure.id in uiState.selectedMeasureIds
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (selected) ForestLight.copy(alpha = 0.25f) else CardWhite)
                        .border(1.dp, if (selected) ForestMid else Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                        .clickable { viewModel.toggleMeasure(measure) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(22.dp).clip(RoundedCornerShape(6.dp)).background(if (selected) ForestMid else Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) { if (selected) Icon(Icons.Filled.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp)) }
                    Spacer(Modifier.width(10.dp))
                    Text(measure.text, style = MaterialTheme.typography.bodyMedium, color = InkDark, modifier = Modifier.weight(1f))
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionHeader(title = "Tu decisión")
            Spacer(Modifier.height(10.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DecisionChoiceButton("Autorizar", ForestMid) { viewModel.decide(AuthorizationChoice.AUTORIZAR) }
                DecisionChoiceButton("Solicitar cambios", SunGold) { viewModel.decide(AuthorizationChoice.SOLICITAR_CAMBIOS) }
                DecisionChoiceButton("No autorizar", AlertCoral) { viewModel.decide(AuthorizationChoice.NO_AUTORIZAR) }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DecisionChoiceButton(label: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) { Text(label, fontWeight = FontWeight.Bold) }
}
