package com.example.in2000team5.ui_layer.theme

import android.hardware.lights.Light
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Red,
    primaryVariant = Red,
    secondary = Red
)

private val LightColorPalette = lightColors(
    primary = Red,
    primaryVariant = Red,
    secondary = Red,
    secondaryVariant = DarkBlue,
)

@Composable
fun IN2000Team5Theme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
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