package com.idnp2025b.solucionesmoviles.ui.components.planta

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.idnp2025b.solucionesmoviles.data.entities.Planta
import com.idnp2025b.solucionesmoviles.ui.components.general.AccionesActivo
import com.idnp2025b.solucionesmoviles.ui.components.general.AccionesEliminado
import com.idnp2025b.solucionesmoviles.ui.components.general.AccionesInactivo
import com.idnp2025b.solucionesmoviles.ui.components.general.DialogoEliminar
import com.idnp2025b.solucionesmoviles.ui.theme.colorAmarilloEstado
import com.idnp2025b.solucionesmoviles.ui.theme.colorRojoEstado
import com.idnp2025b.solucionesmoviles.ui.theme.colorVerdeEstado

@Composable
fun PlantaItem(
    planta: Planta,
    onActivar: (Int) -> Unit,
    onInactivar: (Int) -> Unit,
    onEliminar: (Int) -> Unit,
    onEliminarFisico: (Planta) -> Unit,
    onEditar: (Int) -> Unit, // Ojo: Aquí recibes Int
) {
    var mostrarDialogoLogico by remember { mutableStateOf(false) }
    var mostrarDialogoFisico by remember { mutableStateOf(false) }

    // Lógica de colores inteligente
    val (colorEstado, textoEstado) = when (planta.estPla) {
        "A" -> Pair(colorVerdeEstado, "Activa")
        "I" -> Pair(colorAmarilloEstado, "Inactiva")
        "E" -> Pair(colorRojoEstado, "Eliminada")
        else -> Pair(Color.Gray, "Desconocido")
    }

    // 1. Diálogo Lógico (Papelera)
    if (mostrarDialogoLogico) {
        DialogoEliminar(
            onDismiss = { mostrarDialogoLogico = false },
            onConfirm = {
                onEliminar(planta.codPla)
                mostrarDialogoLogico = false
            },
            nombreItem = planta.nomPla,
            esPermanente = false
        )
    }

    // 2. Diálogo Físico (Permanente)
    if (mostrarDialogoFisico) {
        DialogoEliminar(
            onDismiss = { mostrarDialogoFisico = false },
            onConfirm = {
                onEliminarFisico(planta)
                mostrarDialogoFisico = false
            },
            nombreItem = planta.nomPla,
            esPermanente = true
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            // Cabecera con ID y Etiqueta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "CÓDIGO: ${planta.codPla}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Etiqueta de estado fuerte
                Surface(
                    color = colorEstado.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(2.dp, colorEstado)
                ) {
                    Text(
                        text = textoEstado,
                        style = MaterialTheme.typography.labelMedium,
                        color = colorEstado,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Nombre de la Planta
            Text(
                text = planta.nomPla,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            // Botonera modular
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (planta.estPla) {
                    "A" -> AccionesActivo(
                        onInactivar = { onInactivar(planta.codPla) },
                        onEditar = { onEditar(planta.codPla) },
                        onEliminar = { mostrarDialogoLogico = true }
                    )

                    "I" -> AccionesInactivo(
                        onActivar = { onActivar(planta.codPla) },
                        onEliminar = { mostrarDialogoLogico = true }
                    )

                    "E" -> AccionesEliminado(
                        onRestaurar = { onActivar(planta.codPla) },
                        onEliminarFisico = { mostrarDialogoFisico = true }
                    )
                }
            }
        }
    }
}