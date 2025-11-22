package com.idnp2025b.solucionesmoviles.ui.components.general

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.idnp2025b.solucionesmoviles.ui.theme.colorVerdeEstado
import com.idnp2025b.solucionesmoviles.ui.theme.colorAmarilloEstado
import com.idnp2025b.solucionesmoviles.ui.theme.colorRojoEstado

// El ladrillo fundamental
@Composable
fun RowScope.BotonAccion(
    texto: String,
    colorFondo: Color,
    onClick: () -> Unit
) {
    val colorTextoCalculado = if (colorFondo == MaterialTheme.colorScheme.primary) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        if (colorFondo.luminance() > 0.5f) Color.Black else Color.White
    }
    Button(
        onClick = onClick,
        modifier = Modifier.weight(1f), // Ocupa espacio equitativo
        colors = ButtonDefaults.buttonColors(
            containerColor = colorFondo,
            contentColor = colorTextoCalculado
        )
    ) {
        Text(texto)
    }
}

@Composable
fun AccionesActivo(
    onInactivar: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BotonAccion(texto = "Inactivar", colorFondo = colorAmarilloEstado, onClick = onInactivar)
        BotonAccion(texto = "Editar", colorFondo = MaterialTheme.colorScheme.primary, onClick = onEditar)
        BotonAccion(texto = "Eliminar", colorFondo = colorRojoEstado, onClick = onEliminar)
    }
}

@Composable
fun AccionesInactivo(
    onActivar: () -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BotonAccion(texto = "Activar", colorFondo = colorVerdeEstado, onClick = onActivar)
        BotonAccion(texto = "Eliminar", colorFondo = colorRojoEstado, onClick = onEliminar)
    }
}

@Composable
fun AccionesEliminado(
    onRestaurar: () -> Unit,
    onEliminarFisico: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BotonAccion(texto = "Restaurar", colorFondo = colorVerdeEstado, onClick = onRestaurar)
        BotonAccion(texto = "Eliminar", colorFondo = colorRojoEstado, onClick = onEliminarFisico)
    }
}