package com.imcys.bilibilias.home.ui.activity.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.imcys.bilibilias.R

private val DarkColorPalette = darkColors(
    primary = Color(0xFFFB7299),
    primaryVariant = Color(0xFFFFD2E0),
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Color(0xFFFB7299),
    primaryVariant = Color(0xFFFFD2E0),
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun BILIBILIASTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}