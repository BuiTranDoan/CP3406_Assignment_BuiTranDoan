package com.studyhelpers.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Use your custom warm colors
private val DarkColorScheme = darkColorScheme(
    primary = CocoaDark,
    secondary = AccentMint,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color(0xFFEFEFEF),
    onSurface = Color(0xFFEFEFEF)
)

private val LightColorScheme = lightColorScheme(
    primary = Cocoa,
    secondary = AccentMint,
    background = Sand,
    surface = Sand,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF2B2118),
    onSurface = Color(0xFF2B2118)
)

@Composable
fun StudyHelpersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // keep dynamic colors if you like, otherwise you can just ignore this branch
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}