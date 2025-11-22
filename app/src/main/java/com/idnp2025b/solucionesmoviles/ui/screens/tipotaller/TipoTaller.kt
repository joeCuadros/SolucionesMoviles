package com.idnp2025b.solucionesmoviles.ui.screens.tipotaller

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.idnp2025b.solucionesmoviles.ui.components.general.BotonFlotante
import com.idnp2025b.solucionesmoviles.ui.components.general.Buscador
import com.idnp2025b.solucionesmoviles.ui.components.tipotaller.TipoTallerItem
import com.idnp2025b.solucionesmoviles.viewmodel.FiltroTipoTaller
import com.idnp2025b.solucionesmoviles.viewmodel.TipoTallerViewModel
import com.idnp2025b.solucionesmoviles.viewmodel.UiState

@Composable
fun TipoTaller(
    navController: NavController,
    viewModel: TipoTallerViewModel = hiltViewModel()
) {
    val tipoTalleres by viewModel.tipoTalleres.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val filtroActual by viewModel.filtroActual.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var ascendente by remember { mutableStateOf(true) }

    LaunchedEffect(uiState) {
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

    Scaffold(
        floatingActionButton = {
            BotonFlotante(
                onClick = { navController.navigate("new_tipo_taller") },
                contentDescription = "Nuevo Tipo de Taller"
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // FILTROS DE ESTADO
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val chips = listOf(
                    Pair(FiltroTipoTaller.TODAS, "Todos"),
                    Pair(FiltroTipoTaller.ACTIVAS, "Activos"),
                    Pair(FiltroTipoTaller.INACTIVAS, "Inactivos"),
                    Pair(FiltroTipoTaller.ELIMINADAS, "Papelera")
                )
                chips.forEach { (filtro, label) ->
                    FilterChip(
                        selected = filtroActual == filtro,
                        onClick = { viewModel.cargarTipoTalleres(filtro) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            // BUSCADOR REUTILIZABLE
            Buscador(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                ascendente = ascendente,
                onOrdenChange = { ascendente = it },
                placeHolder = "Buscar tipo de taller..."
            )

            // Lógica de ordenamiento y filtrado local
            val tipoTalleresProcesados = remember(tipoTalleres, searchQuery, ascendente) {
                tipoTalleres
                    .filter { tt ->
                        tt.nomTipTal.contains(searchQuery, ignoreCase = true)
                    }
                    .let { filtradas ->
                        if (ascendente) filtradas.sortedBy { it.nomTipTal }
                        else filtradas.sortedByDescending { it.nomTipTal }
                    }
            }

            // LISTA DE TIPO DE TALLERES
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tipoTalleresProcesados) { tipoTaller ->
                    TipoTallerItem(
                        tipoTaller = tipoTaller,
                        onActivar = { viewModel.activarTipoTaller(it) },
                        onInactivar = { viewModel.inactivarTipoTaller(it) },
                        onEliminar = { viewModel.eliminarLogicoTipoTaller(it) },
                        onEliminarFisico = { viewModel.deleteTipoTaller(tipoTaller) },
                        onEditar = { navController.navigate("edit_tipo_taller/${tipoTaller.codTipTal}") }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}