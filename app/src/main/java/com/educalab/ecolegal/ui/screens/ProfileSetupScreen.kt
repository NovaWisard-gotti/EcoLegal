package com.educalab.ecolegal.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.educalab.ecolegal.data.local.seed.AvatarSeed
import com.educalab.ecolegal.ui.illustration.AvatarIllustration
import com.educalab.ecolegal.ui.theme.*

@Composable
fun ProfileSetupScreen(onCreate: (alias: String, avatarKey: String) -> Unit) {
    var alias by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf(AvatarSeed.avatars.first().key) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBg)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(24.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Text("Crea tu Guardián", style = MaterialTheme.typography.headlineMedium, color = InkDark, fontWeight = FontWeight.Bold)
        Text(
            "Elige un alias y un avatar. No necesitas tu nombre real.",
            style = MaterialTheme.typography.bodyLarge,
            color = InkDark.copy(alpha = 0.7f)
        )

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .size(110.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(CardWhite)
                .border(3.dp, ForestMid, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AvatarIllustration(key = selectedAvatar, modifier = Modifier.size(90.dp))
        }

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = alias,
            onValueChange = { if (it.length <= 16) alias = it },
            label = { Text("Tu alias de guardián") },
            placeholder = { Text("Ej. Guardián del Bosque") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))
        Text("Elige tu avatar", style = MaterialTheme.typography.titleMedium, color = InkDark, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(AvatarSeed.avatars) { avatar ->
                val selected = avatar.key == selectedAvatar
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (selected) ForestMid.copy(alpha = 0.18f) else CardWhite)
                        .border(
                            width = if (selected) 2.dp else 1.dp,
                            color = if (selected) ForestMid else Color(0xFFE0E0E0),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedAvatar = avatar.key }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AvatarIllustration(key = avatar.key, modifier = Modifier.fillMaxSize())
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onCreate(alias.trim(), selectedAvatar) },
            enabled = alias.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = ForestMid)
        ) {
            Text("Comenzar a explorar")
        }
    }
}
