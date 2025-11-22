package com.idnp2025b.solucionesmoviles.ui.components.taller

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
import com.idnp2025b.solucionesmoviles.data.entities.Taller
import com.idnp2025b.solucionesmoviles.data.entities.TallerConDetalles
import com.idnp2025b.solucionesmoviles.ui.components.general.AccionesActivo
import com.idnp2025b.solucionesmoviles.ui.components.general.AccionesEliminado
import com.idnp2025b.solucionesmoviles.ui.components.general.AccionesInactivo
import com.idnp2025b.solucionesmoviles.ui.components.general.DialogoEliminar
import com.idnp2025b.solucionesmoviles.ui.theme.colorAmarilloEstado
import com.idnp2025b.solucionesmoviles.ui.theme.colorRojoEstado
import com.idnp2025b.solucionesmoviles.ui.theme.colorVerdeEstado

@Composable
fun TallerItem(
    tallerConDetalles: TallerConDetalles,
    onActivar: (Int) -> Unit,
    onInactivar: (Int) -> Unit,
    onEliminar: (Int) -> Unit,
    onEliminarFisico: (Taller) -> Unit,
    onEditar: () -> Unit
) {
    val taller = tallerConDetalles.taller
    var mostrarDialogoLogico by remember { mutableStateOf(false) }
    var mostrarDialogoFisico by remember { mutableStateOf(false) }

    // Lógica de colores (Día/Noche)
    val (colorEstado, textoEstado) = when (taller.estTal) {
        "A" -> Pair(colorVerdeEstado, "Activo")
        "I" -> Pair(colorAmarilloEstado, "Inactivo")
        "E" -> Pair(colorRojoEstado, "Eliminado")
        else -> Pair(Color.Gray, "Desconocido")
    }

    // 1. Diálogo para mover a papelera (Lógico)
    if (mostrarDialogoLogico) {
        DialogoEliminar(
            onDismiss = { mostrarDialogoLogico = false },
            onConfirm = {
                onEliminar(taller.codTal)
                mostrarDialogoLogico = false
            },
            nombreItem = taller.nomTal,
            esPermanente = false
        )
    }

    // 2. Diálogo para borrar de BD (Físico)
    if (mostrarDialogoFisico) {
        DialogoEliminar(
            onDismiss = { mostrarDialogoFisico = false },
            onConfirm = {
                onEliminarFisico(taller)
                mostrarDialogoFisico = false
            },
            nombreItem = taller.nomTal,
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
            // Cabecera: Código + Estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "CÓDIGO: ${taller.codTal}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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

            Spacer(modifier = Modifier.height(8.dp))

            // Título Principal
            Text(
                text = taller.nomTal,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            // Información Extra (Planta, Departamento, Tipo)
            // La mantenemos aquí en el cuerpo para que se vea ordenado
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Planta: ${tallerConDetalles.planta?.nomPla ?: "Sin Planta"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Departamento: ${tallerConDetalles.departamento?.nomDep ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Tipo: ${tallerConDetalles.tipoTaller?.nomTipTal ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(12.dp))

            // Botonera modular
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (taller.estTal) {
                    "A" -> AccionesActivo(
                        onInactivar = { onInactivar(taller.codTal) },
                        onEditar = { onEditar() },
                        onEliminar = { mostrarDialogoLogico = true }
                    )

                    "I" -> AccionesInactivo(
                        onActivar = { onActivar(taller.codTal) },
                        onEliminar = { mostrarDialogoLogico = true }
                    )

                    "E" -> AccionesEliminado(
                        onRestaurar = { onActivar(taller.codTal) },
                        onEliminarFisico = { mostrarDialogoFisico = true }
                    )
                }
            }
        }
    }
}