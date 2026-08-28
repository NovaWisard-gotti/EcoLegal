package com.educalab.verdelegal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.educalab.verdelegal.data.repository.VerdeLegalRepository
import com.educalab.verdelegal.ui.components.SectionHeader
import com.educalab.verdelegal.ui.components.XpChip
import com.educalab.verdelegal.ui.illustration.BadgeIllustration
import com.educalab.verdelegal.ui.theme.*
import com.educalab.verdelegal.ui.viewmodel.BadgesViewModel
import com.educalab.verdelegal.ui.viewmodel.verdeLegalViewModel

@Composable
fun BadgesScreen(userId: Long, onBack: () -> Unit) {
    val viewModel = verdeLegalViewModel(key = "badges_$userId") { repo -> BadgesViewModel(repo, userId) }
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(CreamBg)) {
        TopAppBar(
            title = { Text("Mis insignias", fontWeight = FontWeight.Bold) },
            navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Volver") } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = CreamBg)
        )

        if (uiState.loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = ForestMid) }
            return@Column
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            val earned = uiState.badges.count { it.earned }
            SectionHeader(title = "$earned de ${uiState.badges.size} insignias", subtitle = "Cada insignia representa una acción real que realizaste.")
            Spacer(Modifier.height(8.dp))
            XpChip(xp = uiState.totalXp)
        }

        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(20.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            items(uiState.badges) { overview ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    BadgeIllustration(iconKey = overview.badge.iconKey, locked = !overview.earned, modifier = Modifier.size(78.dp))
                    Spacer(Modifier.height(6.dp))
                    Text(
                        overview.badge.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = if (overview.earned) InkDark else InkDark.copy(alpha = 0.45f),
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
            }
        }
    }
}
