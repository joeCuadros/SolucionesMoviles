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
import com.idnp2025b.solucionesmoviles.data.entities.TallerConDetalles
import com.idnp2025b.solucionesmoviles.ui.components.general.BotonFlotante
import com.idnp2025b.solucionesmoviles.ui.components.taller.BuscadorTaller
import com.idnp2025b.solucionesmoviles.ui.components.taller.CriterioOrdenTaller
import com.idnp2025b.solucionesmoviles.ui.components.taller.TallerItem
import com.idnp2025b.solucionesmoviles.viewmodel.FiltroTaller
import com.idnp2025b.solucionesmoviles.viewmodel.TallerViewModel
import com.idnp2025b.solucionesmoviles.viewmodel.UiState

@Composable
fun Taller(
    navController: NavController,
    viewModel: TallerViewModel = hiltViewModel()
) {
    val talleres by viewModel.talleres.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val filtroActual by viewModel.filtroActual.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val context = LocalContext.current

    // --- ESTADOS DE BÚSQUEDA Y ORDEN ---
    var searchQuery by remember { mutableStateOf("") }
    var ascendente by remember { mutableStateOf(true) }
    var criterioOrden by remember { mutableStateOf(CriterioOrdenTaller.NOMBRE_TALLER) } // <--- Nuevo estado

    // Manejo de mensajes (Toasts)
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
                onClick = { navController.navigate("new_taller") },
                contentDescription = "Nuevo Taller"
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
            // --- 1. FILTROS (CHIPS) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val chips = listOf(
                    Pair(FiltroTaller.TODAS, "Todos"),
                    Pair(FiltroTaller.ACTIVAS, "Activos"),
                    Pair(FiltroTaller.INACTIVAS, "Inactivos"),
                    Pair(FiltroTaller.ELIMINADAS, "Papelera")
                )
                chips.forEach { (filtro, label) ->
                    FilterChip(
                        selected = filtroActual == filtro,
                        onClick = { viewModel.cargarTalleres(filtro) },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }

            // --- 2. BUSCADOR AVANZADO DE TALLER ---
            BuscadorTaller(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                ascendente = ascendente,
                onOrdenChange = { ascendente = it },
                criterioOrden = criterioOrden,
                onCriterioChange = { criterioOrden = it },
                placeHolder = "Buscar en talleres..."
            )

            // --- 3. LÓGICA DE FILTRADO Y ORDENAMIENTO (CORREGIDA) ---
            val talleresProcesados by remember(talleres, searchQuery, ascendente, criterioOrden) {
                derivedStateOf {
                    talleres
                        .filter { t ->
                            t.taller.nomTal.contains(searchQuery, ignoreCase = true) ||
                                    t.taller.codTal.toString().contains(searchQuery) ||
                                    (t.planta?.nomPla?.contains(searchQuery, ignoreCase = true) == true) ||
                                    (t.departamento?.nomDep?.contains(searchQuery, ignoreCase = true) == true) ||
                                    (t.tipoTaller?.nomTipTal?.contains(searchQuery, ignoreCase = true) == true)
                        }
                        .let { filtradas ->
                            when (criterioOrden) {
                                CriterioOrdenTaller.NOMBRE_TALLER -> {
                                    if (ascendente) filtradas.sortedBy { it.taller.nomTal }
                                    else filtradas.sortedByDescending { it.taller.nomTal }
                                }
                                CriterioOrdenTaller.CODIGO_TALLER -> {
                                    if (ascendente) filtradas.sortedBy { it.taller.codTal }
                                    else filtradas.sortedByDescending { it.taller.codTal }
                                }
                                CriterioOrdenTaller.NOMBRE_PLANTA -> {
                                    // Usamos ?: "" para manejar nulos de forma segura
                                    if (ascendente) filtradas.sortedBy { it.planta?.nomPla ?: "" }
                                    else filtradas.sortedByDescending { it.planta?.nomPla ?: "" }
                                }
                                CriterioOrdenTaller.NOMBRE_DEPARTAMENTO -> {
                                    if (ascendente) filtradas.sortedBy { it.departamento?.nomDep ?: "" }
                                    else filtradas.sortedByDescending { it.departamento?.nomDep ?: "" }
                                }
                                CriterioOrdenTaller.NOMBRE_TIPO -> {
                                    if (ascendente) filtradas.sortedBy { it.tipoTaller?.nomTipTal ?: "" }
                                    else filtradas.sortedByDescending { it.tipoTaller?.nomTipTal ?: "" }
                                }
                            }
                        }
                }
            }

            // --- 4. LISTA ---
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}