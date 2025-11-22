package com.idnp2025b.solucionesmoviles.ui.components.taller

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BuscadorTaller(
    query: String,
    onQueryChange: (String) -> Unit,
    ascendente: Boolean,
    onOrdenChange: (Boolean) -> Unit,
    criterioOrden: CriterioOrdenTaller,
    onCriterioChange: (CriterioOrdenTaller) -> Unit,
    placeHolder: String = "Buscar taller..."
) {
    var menuExpandido by remember { mutableStateOf(false) }

    // CAMBIO PRINCIPAL: Usamos Column para apilar elementos
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // 1. CAMPO DE TEXTO (Arriba, ancho completo)
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text(placeHolder) },
            modifier = Modifier.fillMaxWidth(), // Ocupa todo el ancho
            singleLine = true,
            shape = CircleShape,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 2. FILA DE CONTROLES (Abajo: Filtro + Flecha)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // A. BOTÓN DE CRITERIO (Ocupa el espacio sobrante para que se vea bien el texto)
            Box(modifier = Modifier.weight(1f)) {
                FilledTonalButton(
                    onClick = { menuExpandido = true },
                    modifier = Modifier.fillMaxWidth(), // Rellena el espacio
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = criterioOrden.label,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                DropdownMenu(
                    expanded = menuExpandido,
                    onDismissRequest = { menuExpandido = false }
                ) {
                    CriterioOrdenTaller.entries.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion.label) },
                            onClick = {
                                onCriterioChange(opcion)
                                menuExpandido = false
                            },
                            leadingIcon = {
                                if (criterioOrden == opcion) {
                                    Icon(Icons.Default.KeyboardArrowUp, null)
                                }
                            }
                        )
                    }
                }
            }

            // B. BOTÓN ASCENDENTE/DESCENDENTE (Cuadrado/Fijo a la derecha)
            FilledIconToggleButton(
                checked = ascendente,
                onCheckedChange = onOrdenChange,
                colors = IconButtonDefaults.filledIconToggleButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    checkedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    checkedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                if (ascendente) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Ascendente")
                } else {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Descendente")
                }
            }
        }
    }
}