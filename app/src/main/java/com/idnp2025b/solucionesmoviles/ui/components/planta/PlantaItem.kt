package com.idnp2025b.solucionesmoviles.ui.components.planta

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
import com.idnp2025b.solucionesmoviles.data.entities.Planta

@Composable
fun PlantaItem(
    planta: Planta,
    onActivar: (Int) -> Unit,
    onInactivar: (Int) -> Unit,
    onEliminar: (Int) -> Unit,
    onEliminarFisico: (Planta) -> Unit
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
                text = "Planta: ${planta.nomPla}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Estado: ${when(planta.estPla) {
                    "A" -> "Activa"
                    "I" -> "Inactiva"
                    "E" -> "Eliminada"
                    else -> "Desconocido"
                }}",
                style = MaterialTheme.typography.bodySmall,
                color = when(planta.estPla) {
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
                when (planta.estPla) {
                    "A" -> {
                        OutlinedButton(
                            onClick = { onInactivar(planta.codPla) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Inactivar")
                        }
                        OutlinedButton(
                            onClick = { onEliminar(planta.codPla) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Eliminar")
                        }
                    }
                    "I" -> {
                        OutlinedButton(
                            onClick = { onActivar(planta.codPla) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Activar")
                        }
                        OutlinedButton(
                            onClick = { onEliminar(planta.codPla) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Eliminar")
                        }
                    }
                    "E" -> {
                        OutlinedButton(
                            onClick = { onActivar(planta.codPla) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Restaurar")
                        }
                        OutlinedButton(
                            onClick = { onEliminarFisico(planta) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Eliminar permanente")
                        }
                    }
                }
            }
        }
    }
}