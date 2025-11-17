package com.idnp2025b.solucionesmoviles.ui.components.tipotaller

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
import com.idnp2025b.solucionesmoviles.data.entities.TipoTaller

@Composable
fun TipoTallerItem(
    tipoTaller: TipoTaller,
    onActivar: (Int) -> Unit,
    onInactivar: (Int) -> Unit,
    onEliminar: (Int) -> Unit,
    onEliminarFisico: (TipoTaller) -> Unit,
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
                text = "CÓDIGO: ${tipoTaller.codTipTal}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Tipo de Taller: ${tipoTaller.nomTipTal}",
                style = MaterialTheme.typography.titleMedium
            )

            val estadoTextoCompleto = when (tipoTaller.estTipTal) {
                "A" -> "Activo"
                "I" -> "Inactivo"
                "E" -> "Eliminado"
                else -> "Desconocido"
            }

            Text(
                text = "Estado: $estadoTextoCompleto",
                style = MaterialTheme.typography.bodySmall,
                color = when(tipoTaller.estTipTal) {
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
                when (tipoTaller.estTipTal) {
                    "A" -> {
                        OutlinedButton(
                            onClick = { onInactivar(tipoTaller.codTipTal) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Inactivar") }

                        OutlinedButton(
                            onClick = { onEditar() },
                            modifier = Modifier.weight(1f)
                        ) { Text("Editar") }

                        OutlinedButton(
                            onClick = { onEliminar(tipoTaller.codTipTal) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Eliminar") }
                    }
                    "I" -> {
                        OutlinedButton(
                            onClick = { onActivar(tipoTaller.codTipTal) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Activar") }


                        OutlinedButton(
                            onClick = { onEliminar(tipoTaller.codTipTal) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Eliminar") }
                    }
                    "E" -> {
                        OutlinedButton(
                            onClick = { onActivar(tipoTaller.codTipTal) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Restaurar") }

                        OutlinedButton(
                            onClick = { onEliminarFisico(tipoTaller) },
                            modifier = Modifier.weight(1f)
                        ) { Text("Elim. Perm.") }
                    }
                }
            }
        }
    }
}