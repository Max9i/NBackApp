package com.emmajson.nbackapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


private val DarkColorScheme = darkColorScheme(
    primary = DarkButtonColors,        // Darker blue for primary elements
    secondary = DarkBackgroundColor,       // Soft blue background

    primaryContainer = DarkBlockColorActive,        // Active block color
    onPrimaryContainer = DarkBlockColorInactive,     // Light inactive blocks

    background = DarkBackgroundColor,      // Background color
    onPrimary = Color.Black,           // Text on primary
    onSecondary = DarkTextColor,           // Text on secondary
    onBackground = DarkTextColor,          // Text on background
    onSurface = LightTextColor,             // Text on surface
    tertiaryContainer = DarkProgressBarColor
)

private val LightColorScheme = lightColorScheme(
    primary = ButtonColors,        // Darker blue for primary elements
    secondary = BackgroundColor,       // Soft blue background

    primaryContainer = BlockColorInactive,     // Light inactive blocks
    onPrimaryContainer = BlockColorActive,        // Active block color

    background = BackgroundColor,      // Background color
    onPrimary = Color.White,           // Text on primary
    onSecondary = TextColor,           // Text on secondary
    onBackground = TextColor,          // Text on background
    onSurface = DarkerTextColor,              // Text on surface
    tertiaryContainer = ProgressBarColor

)

@Composable
fun NBack_CImplTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}