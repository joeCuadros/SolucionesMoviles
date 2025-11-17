package com.idnp2025b.solucionesmoviles.ui.components.departamento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idnp2025b.solucionesmoviles.data.entities.Departamento

@Composable
fun DepartamentoItem(
    departamento: Departamento,
    onActivar: (Int) -> Unit,
    onInactivar: (Int) -> Unit,
    onEliminar: (Int) -> Unit,
    onEliminarFisico: (Departamento) -> Unit,
    onEditar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = "CÓDIGO: ${departamento.codDep}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Departamento: ${departamento.nomDep}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Estado: ${when(departamento.estDep) {
                    "A" -> "Activo"
                    "I" -> "Inactivo"
                    "E" -> "Eliminado"
                    else -> "Desconocido"
                }}",
                style = MaterialTheme.typography.bodySmall,
                color = when(departamento.estDep) {
                    "A" -> MaterialTheme.colorScheme.primary
                    "I" -> MaterialTheme.colorScheme.secondary
                    "E" -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )

            Spacer(Modifier.height(8.dp))

            // Botones según estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (departamento.estDep) {
                    "A" -> {
                        OutlinedButton(
                            onClick = { onInactivar(departamento.codDep) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Inactivar") }

                        OutlinedButton(
                            onClick = { onEditar() },
                            modifier = Modifier.weight(1f)
                        ) { Text("Editar") }

                        OutlinedButton(
                            onClick = { onEliminar(departamento.codDep) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Eliminar") }
                    }
                    "I" -> {
                        OutlinedButton(
                            onClick = { onActivar(departamento.codDep) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Activar") }

                        OutlinedButton(
                            onClick = { onEliminar(departamento.codDep) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Eliminar") }
                    }
                    "E" -> {
                        OutlinedButton(
                            onClick = { onActivar(departamento.codDep) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Restaurar") }

                        OutlinedButton(
                            onClick = { onEliminarFisico(departamento) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Elim. Perm.") }
                    }
                }
            }
        }
    }
}