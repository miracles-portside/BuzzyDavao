package com.davao.buzzy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = Mint,
    onPrimary = SurfaceCard,
    primaryContainer = MintSoft,
    onPrimaryContainer = MintDark,

    secondary = Coral,
    onSecondary = SurfaceCard,

    tertiary = Sun,
    onTertiary = Ink,

    background = Canvas,
    onBackground = Ink,

    surface = SurfaceCard,
    onSurface = Ink,
    surfaceVariant = SurfaceSoft,
    onSurfaceVariant = InkMid,

    outline = Line
)

private val DarkColors = darkColorScheme(
    primary = Mint,
    onPrimary = Ink,
    primaryContainer = MintDark,
    onPrimaryContainer = SurfaceCard,

    secondary = Coral,
    onSecondary = Ink,

    tertiary = Sun,
    onTertiary = Ink,

    background = DarkPaper,
    onBackground = DarkInk,

    surface = DarkCard,
    onSurface = DarkInk,
    surfaceVariant = DarkCard,
    onSurfaceVariant = DarkMuted,

    outline = DarkLine
)

@Composable
fun BuzzyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = BuzzyTypography,
        content = content
    )
}
