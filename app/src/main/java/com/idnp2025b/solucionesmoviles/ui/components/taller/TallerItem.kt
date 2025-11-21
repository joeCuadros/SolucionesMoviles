package com.idnp2025b.solucionesmoviles.ui.components.taller

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idnp2025b.solucionesmoviles.data.entities.TallerConDetalles
import com.idnp2025b.solucionesmoviles.data.entities.Taller

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

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "CÓDIGO: ${taller.codTal}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // Mostramos la Planta como etiqueta
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = tallerConDetalles.planta?.nomPla ?: "Sin Planta",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            Text(
                text = taller.nomTal,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            // Detalles adicionales (Depto y Tipo)
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = "Departamento: ${tallerConDetalles.departamento?.nomDep ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Tipo: ${tallerConDetalles.tipoTaller?.nomTipTal ?: "N/A"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Estado: ${if(taller.estTal == "A") "Activo" else if(taller.estTal == "I") "Inactivo" else "Eliminado"}",
                style = MaterialTheme.typography.bodySmall,
                color = if(taller.estTal == "A") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )

            Spacer(Modifier.height(8.dp))

            // Botones de acción (Lógica idéntica a TipoTaller)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                when (taller.estTal) {
                    "A" -> {
                        OutlinedButton(onClick = { onInactivar(taller.codTal) }, modifier = Modifier.weight(1f)) { Text("Inactivar") }
                        OutlinedButton(onClick = { onEditar() }, modifier = Modifier.weight(1f)) { Text("Editar") }
                        OutlinedButton(onClick = { onEliminar(taller.codTal) }, modifier = Modifier.weight(1f)) { Text("Eliminar") }
                    }
                    "I" -> {
                        OutlinedButton(onClick = { onActivar(taller.codTal) }, modifier = Modifier.weight(1f)) { Text("Activar") }
                        OutlinedButton(onClick = { onEliminar(taller.codTal) }, modifier = Modifier.weight(1f)) { Text("Eliminar") }
                    }
                    "E" -> {
                        OutlinedButton(onClick = { onActivar(taller.codTal) }, modifier = Modifier.weight(1f)) { Text("Restaurar") }
                        OutlinedButton(onClick = { onEliminarFisico(taller) }, modifier = Modifier.weight(1f)) { Text("Restaurar") }
                    }
                }
            }
        }
    }
}