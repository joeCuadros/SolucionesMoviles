package com.idnp2025b.solucionesmoviles.ui.components.general

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort // Icono para el menú de orden
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Enum para definir las opciones de ordenamiento disponibles
enum class CriterioOrden { NOMBRE, CODIGO }

@Composable
fun Buscador(
    query: String,
    onQueryChange: (String) -> Unit,
    ascendente: Boolean,
    onOrdenChange: (Boolean) -> Unit,
    criterioOrden: CriterioOrden,       // <--- Nuevo parámetro
    onCriterioChange: (CriterioOrden) -> Unit, // <--- Nuevo evento
    placeHolder: String = "Buscar..."
) {
    var menuExpandido by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. BUSCADOR (Igual que antes)
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text(placeHolder) },
            modifier = Modifier.weight(1f),
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

        // 2. BOTÓN DE MENÚ PARA ELEGIR CRITERIO (Nombre vs Código)
        Box {
            FilledTonalIconButton(
                onClick = { menuExpandido = true },
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Filtrar por")
            }

            DropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = { menuExpandido = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Ordenar por Nombre") },
                    onClick = {
                        onCriterioChange(CriterioOrden.NOMBRE)
                        menuExpandido = false
                    },
                    leadingIcon = {
                        if (criterioOrden == CriterioOrden.NOMBRE) Icon(Icons.Default.KeyboardArrowUp, null)
                    }
                )
                DropdownMenuItem(
                    text = { Text("Ordenar por Código") },
                    onClick = {
                        onCriterioChange(CriterioOrden.CODIGO)
                        menuExpandido = false
                    },
                    leadingIcon = {
                        if (criterioOrden == CriterioOrden.CODIGO) Icon(Icons.Default.KeyboardArrowUp, null)
                    }
                )
            }
        }

        // 3. BOTÓN ASCENDENTE/DESCENDENTE (Igual que antes)
        FilledIconToggleButton(
            checked = ascendente,
            onCheckedChange = onOrdenChange,
            colors = IconButtonDefaults.filledIconToggleButtonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
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