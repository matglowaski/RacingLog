package com.example.racinglog.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val RacingColorScheme = darkColorScheme(
    primary = RacingRed,
    onPrimary = RacingWhite,

    primaryContainer = RacingRedDark,
    onPrimaryContainer = RacingWhite,

    secondary = RacingGray,
    onSecondary = RacingBackground,

    background = RacingBackground,
    onBackground = RacingWhite,

    surface = RacingSurface,
    onSurface = RacingWhite,

    surfaceVariant = RacingSurfaceVariant,
    onSurfaceVariant = RacingGray,

    outline = RacingOutline
)

@Composable
fun RacingLogTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RacingColorScheme,
        typography = Typography,
        content = content
    )
}
