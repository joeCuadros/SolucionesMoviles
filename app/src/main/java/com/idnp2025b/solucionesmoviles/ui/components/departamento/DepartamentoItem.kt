package com.idnp2025b.solucionesmoviles.ui.components.departamento

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
import com.idnp2025b.solucionesmoviles.data.entities.Departamento
import com.idnp2025b.solucionesmoviles.ui.components.general.AccionesActivo
import com.idnp2025b.solucionesmoviles.ui.components.general.AccionesEliminado
import com.idnp2025b.solucionesmoviles.ui.components.general.AccionesInactivo
import com.idnp2025b.solucionesmoviles.ui.components.general.DialogoEliminar
import com.idnp2025b.solucionesmoviles.ui.theme.colorVerdeEstado
import com.idnp2025b.solucionesmoviles.ui.theme.colorAmarilloEstado
import com.idnp2025b.solucionesmoviles.ui.theme.colorRojoEstado

@Composable
fun DepartamentoItem(
    departamento: Departamento,
    onActivar: (Int) -> Unit,
    onInactivar: (Int) -> Unit,
    onEliminar: (Int) -> Unit,
    onEliminarFisico: (Departamento) -> Unit,
    onEditar: () -> Unit
) {
    var mostrarDialogoLogico by remember { mutableStateOf(false) } //onClick = { mostrarDialogoEliminar = true },
    var mostrarDialogoFisico by remember { mutableStateOf(false) }

    val (colorEstado, textoEstado) = when (departamento.estDep) {
        "A" -> Pair(colorVerdeEstado, "Activo")
        "I" -> Pair(colorAmarilloEstado, "Inactivo")
        "E" -> Pair(colorRojoEstado, "Eliminado")
        else -> Pair(Color.Gray, "Desconocido")
    }
    // eliminar logicamente
    if (mostrarDialogoLogico) {
        DialogoEliminar(
            onDismiss = { mostrarDialogoLogico = false },
            onConfirm = {
                onEliminar(departamento.codDep)
                mostrarDialogoLogico = false
            },
            nombreItem = departamento.nomDep,
            esPermanente = false
        )
    }

    // Eliminacion permanente
    if (mostrarDialogoFisico) {
        DialogoEliminar(
            onDismiss = { mostrarDialogoFisico = false },
            onConfirm = {
                onEliminarFisico(departamento)
                mostrarDialogoFisico = false
            },
            nombreItem = departamento.nomDep,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = "CODIGO: ${departamento.codDep}",
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
            Text(
                text = departamento.nomDep,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (departamento.estDep) {
                    "A" -> AccionesActivo(
                        onInactivar = { onInactivar(departamento.codDep) },
                        onEditar = { onEditar() },
                        onEliminar = { mostrarDialogoLogico = true }
                    )

                    "I" -> AccionesInactivo(
                        onActivar = { onActivar(departamento.codDep) },
                        onEliminar = { mostrarDialogoLogico = true }
                    )

                    "E" -> AccionesEliminado(
                        onRestaurar = { onActivar(departamento.codDep) },
                        onEliminarFisico = { mostrarDialogoFisico = true }
                    )
                }
            }
        }
    }
}