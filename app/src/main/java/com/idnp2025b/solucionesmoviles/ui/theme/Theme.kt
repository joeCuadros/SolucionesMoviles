package com.idnp2025b.solucionesmoviles.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AzulPrimarioDark,
    secondary = AzulSecundarioDark,
    tertiary = CelesteTerciarioDark,
    background = FondoOscuro,
    surfaceVariant = SurfaceVariantOscuro,
    error = RojoEstado
)

private val LightColorScheme = lightColorScheme(
    primary = AzulPrimario,
    secondary = AzulSecundario,
    tertiary = CelesteTerciario,
    background = FondoClaro,
    surfaceVariant = SurfaceVariantClaro,
    error = RojoEstado
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

val colorVerdeEstado: Color
    @Composable
    get() = if (isSystemInDarkTheme()) VerdeEstadoDark else VerdeEstado

val colorAmarilloEstado: Color
    @Composable
    get() = if (isSystemInDarkTheme()) AmarilloEstadoDark else AmarilloEstado

val colorRojoEstado: Color
    @Composable
    get() = if (isSystemInDarkTheme()) RojoEstadoDark else RojoEstado