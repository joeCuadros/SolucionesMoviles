package com.idnp2025b.solucionesmoviles.ui.screens.taller

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.idnp2025b.solucionesmoviles.viewmodel.TallerViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.idnp2025b.solucionesmoviles.ui.components.taller.TallerItem
import com.idnp2025b.solucionesmoviles.viewmodel.FiltroTaller
import com.idnp2025b.solucionesmoviles.viewmodel.UiState

@Composable
fun Taller(navController: NavController, viewModel: TallerViewModel = hiltViewModel()) {
    val talleres by viewModel.talleres.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val filtroActual by viewModel.filtroActual.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var ascendente by remember { mutableStateOf(true) }

    // Manejo de mensajes (Toasts)
    LaunchedEffect (uiState) {
        when (val state = uiState) {
            is UiState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            is UiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> { }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- FILTROS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = filtroActual == FiltroTaller.TODAS,
                onClick = { viewModel.cargarTalleres(FiltroTaller.TODAS) },
                label = { Text("Todos") }
            )
            FilterChip(
                selected = filtroActual == FiltroTaller.ACTIVAS,
                onClick = { viewModel.cargarTalleres(FiltroTaller.ACTIVAS) },
                label = { Text("Activos") }
            )
            FilterChip(
                selected = filtroActual == FiltroTaller.INACTIVAS,
                onClick = { viewModel.cargarTalleres(FiltroTaller.INACTIVAS) },
                label = { Text("Inactivos") }
            )
            FilterChip(
                selected = filtroActual == FiltroTaller.ELIMINADAS,
                onClick = { viewModel.cargarTalleres(FiltroTaller.ELIMINADAS) },
                label = { Text("Eliminados") }
            )
        }

        // --- BUSCADOR Y ORDEN ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar taller...") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") }
            )
            IconToggleButton(
                checked = ascendente,
                onCheckedChange = { ascendente = !ascendente }
            ) {
                if (ascendente) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Ascendente")
                } else {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Descendente")
                }
            }
        }

        // Lógica de filtrado y ordenamiento en local
        val talleresProcesados by remember {
            derivedStateOf {
                talleres
                    .filter { t ->
                        t.taller.nomTal.contains(searchQuery, ignoreCase = true) ||
                                (t.planta?.nomPla?.contains(searchQuery, ignoreCase = true) == true)
                    }
                    .let { filtradas ->
                        if (ascendente) {
                            filtradas.sortedBy { it.taller.nomTal }
                        } else {
                            filtradas.sortedByDescending { it.taller.nomTal }
                        }
                    }
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(talleresProcesados) { item ->
                TallerItem(
                    tallerConDetalles = item,
                    onActivar = { if (!isProcessing) viewModel.activarTaller(it) },
                    onInactivar = { if (!isProcessing) viewModel.inactivarTaller(it) },
                    onEliminar = { if (!isProcessing) viewModel.eliminarLogicoTaller(it) },
                    onEliminarFisico = { if (!isProcessing) viewModel.deleteTaller(item.taller) },
                    onEditar = { if (!isProcessing) navController.navigate("edit_taller/${item.taller.codTal}") }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { navController.navigate("new_taller") },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Agregar nuevo Taller")
        }
    }
}
