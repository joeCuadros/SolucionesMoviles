package com.idnp2025b.solucionesmoviles.ui.screens.planta

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
import androidx.compose.runtime.derivedStateOf
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
import com.idnp2025b.solucionesmoviles.ui.components.general.CriterioOrden
import com.idnp2025b.solucionesmoviles.ui.components.planta.PlantaItem
import com.idnp2025b.solucionesmoviles.viewmodel.FiltroPlanta
import com.idnp2025b.solucionesmoviles.viewmodel.PlantaViewModel
import com.idnp2025b.solucionesmoviles.viewmodel.UiState

@Composable
fun Planta(
    navController: NavController,
    viewModel: PlantaViewModel = hiltViewModel()
) {
    val plantas by viewModel.plantas.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val filtroActual by viewModel.filtroActual.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var ascendente by remember { mutableStateOf(true) }
    var criterioOrden by remember { mutableStateOf(CriterioOrden.NOMBRE) }

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
                onClick = { navController.navigate("new_planta") },
                contentDescription = "Nueva Planta"
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
                    Pair(FiltroPlanta.TODAS, "Todas"),
                    Pair(FiltroPlanta.ACTIVAS, "Activas"),
                    Pair(FiltroPlanta.INACTIVAS, "Inactivas"),
                    Pair(FiltroPlanta.ELIMINADAS, "Papelera")
                )
                chips.forEach { (filtro, label) ->
                    FilterChip(
                        selected = filtroActual == filtro,
                        onClick = { viewModel.cargarPlantas(filtro) },
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
                criterioOrden = criterioOrden,
                onCriterioChange = { criterioOrden = it },
                placeHolder = "Buscar planta..."
            )

            // Lógica de ordenamiento y filtrado local
            val plantasProcesadas by remember(plantas, searchQuery, ascendente, criterioOrden) {
                derivedStateOf {
                    plantas
                        .filter { planta ->
                            planta.nomPla.contains(searchQuery, ignoreCase = true) ||
                                    planta.codPla.toString().contains(searchQuery)
                        }
                        .let { filtradas ->
                            when (criterioOrden) {
                                CriterioOrden.NOMBRE -> {
                                    if (ascendente) filtradas.sortedBy { it.nomPla }
                                    else filtradas.sortedByDescending { it.nomPla }
                                }
                                CriterioOrden.CODIGO -> {
                                    if (ascendente) filtradas.sortedBy { it.codPla }
                                    else filtradas.sortedByDescending { it.codPla }
                                }
                            }
                        }
                }
            }

            // LISTA DE PLANTAS
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(plantasProcesadas) { planta ->
                    PlantaItem(
                        planta = planta,
                        onActivar = { if (!isProcessing) viewModel.activarPlanta(it) },
                        onInactivar = { if (!isProcessing) viewModel.inactivarPlanta(it) },
                        onEliminar = { if (!isProcessing) viewModel.eliminarLogicoPlanta(it) },
                        onEliminarFisico = { if (!isProcessing) viewModel.deletePlanta(it) },
                        onEditar = { if (!isProcessing) navController.navigate("edit_planta/${planta.codPla}")  }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}