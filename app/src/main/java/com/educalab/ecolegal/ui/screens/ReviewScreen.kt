package com.educalab.ecolegal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ecolegal.ui.components.LumaSpeechBubble
import com.educalab.ecolegal.ui.components.SectionHeader
import com.educalab.ecolegal.ui.illustration.LumaCharacter
import com.educalab.ecolegal.ui.illustration.LumaPose
import com.educalab.ecolegal.ui.theme.*
import com.educalab.ecolegal.ui.viewmodel.ReviewViewModel
import com.educalab.ecolegal.ui.viewmodel.ecoLegalViewModel

/** Sección 20: repaso basado en retos donde el niño falló recientemente (sin diagnóstico). */
@Composable
fun ReviewScreen(userId: Long, onBack: () -> Unit, onOpenChallenge: (Long) -> Unit) {
    val viewModel = ecoLegalViewModel(key = "review_$userId") { repo -> ReviewViewModel(repo, userId) }
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(CreamBg)) {
        TopAppBar(
            title = { Text("Practicar otra vez", fontWeight = FontWeight.Bold) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBg)
        )

        if (uiState.loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = ForestMid) }
            return@Column
        }

        if (uiState.challenges.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LumaCharacter(pose = LumaPose.CELEBRATING, modifier = Modifier.size(120.dp))
                Spacer(Modifier.height(12.dp))
                Text("¡No tienes retos pendientes de repaso!", style = MaterialTheme.typography.titleLarge, color = InkDark, fontWeight = FontWeight.Bold)
            }
            return@Column
        }

        Column(modifier = Modifier.padding(20.dp)) {
            LumaSpeechBubble(text = "Aquí tienes algunos retos para practicar de nuevo. ¡Cada intento cuenta!")
        }

        LazyColumn(contentPadding = PaddingValues(horizontal = 20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(uiState.challenges) { challenge ->
                Surface(
                    onClick = { onOpenChallenge(challenge.id) },
                    shape = RoundedCornerShape(14.dp),
                    color = CardWhite,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Replay, contentDescription = null, tint = ForestMid)
                        Spacer(Modifier.width(10.dp))
                        Text(challenge.title, modifier = Modifier.weight(1f), color = InkDark, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}
