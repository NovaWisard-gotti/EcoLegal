package com.educalab.verdelegal.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.educalab.verdelegal.ui.illustration.LumaCharacter
import com.educalab.verdelegal.ui.illustration.LumaPose
import com.educalab.verdelegal.ui.illustration.ValleyBackdrop
import com.educalab.verdelegal.ui.illustration.ZoneIllustration
import com.educalab.verdelegal.ui.theme.*

private data class OnboardingPage(val title: String, val body: String, val pose: LumaPose)

private val pages = listOf(
    OnboardingPage(
        "¡Bienvenido al Valle Verde!",
        "Soy Luma, exploradora ambiental. Juntos vamos a descubrir cómo cuidar este lugar tan especial.",
        LumaPose.NEUTRAL
    ),
    OnboardingPage(
        "Explora, decide y repara",
        "En cada zona vas a observar situaciones, tomar decisiones responsables y ayudar a reparar lo que lo necesite.",
        LumaPose.POINTING
    ),
    OnboardingPage(
        "Todo queda guardado aquí",
        "Tu progreso se guarda en este dispositivo. No necesitas nombre real, correo ni conexión a internet.",
        LumaPose.CELEBRATING
    )
)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    var pageIndex by remember { mutableStateOf(0) }
    val page = pages[pageIndex]

    Box(modifier = Modifier.fillMaxSize().background(CreamBg)) {
        ValleyBackdrop(modifier = Modifier.fillMaxWidth().height(260.dp).align(Alignment.TopCenter))

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(48.dp))
            Text("VerdeLegal", style = MaterialTheme.typography.headlineLarge, color = CardWhite, fontWeight = FontWeight.ExtraBold)
            Text("Guardianes del Valle Verde", style = MaterialTheme.typography.titleMedium, color = SunLight)

            Spacer(Modifier.weight(1f))

            AnimatedContent(targetState = pageIndex, label = "onboarding_page") { idx ->
                val p = pages[idx]
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LumaCharacter(pose = p.pose, modifier = Modifier.size(140.dp))
                    Spacer(Modifier.height(20.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(p.title, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, color = InkDark)
                            Spacer(Modifier.height(8.dp))
                            Text(p.body, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, color = InkDark.copy(alpha = 0.85f))
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                pages.indices.forEach { i ->
                    Box(
                        modifier = Modifier
                            .size(if (i == pageIndex) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(if (i == pageIndex) ForestMid else ForestMid.copy(alpha = 0.3f))
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = {
                    if (pageIndex < pages.lastIndex) pageIndex++ else onFinished()
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ForestMid)
            ) {
                Text(if (pageIndex < pages.lastIndex) "Siguiente" else "Empezar mi aventura")
            }
        }
    }
}
