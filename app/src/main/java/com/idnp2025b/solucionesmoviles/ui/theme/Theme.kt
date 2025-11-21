package com.idnp2025b.solucionesmoviles.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val DarkColorScheme = darkColorScheme(
    primary = AzulPrimarioDark,
    secondary = AzulSecundarioDark,
    tertiary = CelesteTerciarioDark,
    background = FondoOscuro,
    surfaceVariant = SurfaceVariantOscuro
)

private val LightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    secondary = AzulSecundario,
    tertiary = CelesteTerciario,
    background = FondoClaro,
    surfaceVariant = SurfaceVariantClaro
)

@Composable
fun SolucionesMovilesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}