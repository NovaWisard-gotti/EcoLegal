package com.educalab.ecolegal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = ForestMid,
    onPrimary = CreamBg,
    secondary = RiverBlue,
    onSecondary = CreamBg,
    tertiary = SunGold,
    onTertiary = InkDark,
    background = CreamBg,
    onBackground = InkDark,
    surface = CardWhite,
    onSurface = InkDark,
    error = AlertCoral
)

private val DarkColors = darkColorScheme(
    primary = ForestLight,
    onPrimary = ForestDeep,
    secondary = RiverLight,
    onSecondary = ForestDeep,
    tertiary = SunGold,
    onTertiary = InkDark,
    background = ForestDeep,
    onBackground = CreamBg,
    surface = Color(0xFF234A3B),
    onSurface = CreamBg,
    error = AlertCoral
)

@Composable
fun EcoLegalTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, typography = EcoLegalTypography, content = content)
}
