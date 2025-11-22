package com.idnp2025b.solucionesmoviles.ui.screens.departamento

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import com.idnp2025b.solucionesmoviles.ui.components.departamento.DepartamentoItem
import com.idnp2025b.solucionesmoviles.ui.components.general.BotonFlotante
import com.idnp2025b.solucionesmoviles.ui.components.general.Buscador
import com.idnp2025b.solucionesmoviles.ui.components.general.CriterioOrden
import com.idnp2025b.solucionesmoviles.viewmodel.DepartamentoViewModel
import com.idnp2025b.solucionesmoviles.viewmodel.FiltroDepartamento
import com.idnp2025b.solucionesmoviles.viewmodel.UiState

@Composable
fun Departamento(
    navController: NavController,
    viewModel: DepartamentoViewModel = hiltViewModel()
) {
    val departamentos by viewModel.departamentos.collectAsState()
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
                onClick = { navController.navigate("new_departamento") },
                contentDescription = "Nuevo Departamento"
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
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp), // Un poco más de aire
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val chips = listOf(
                    Pair(FiltroDepartamento.TODAS, "Todos"),
                    Pair(FiltroDepartamento.ACTIVAS, "Activos"),
                    Pair(FiltroDepartamento.INACTIVAS, "Inactivos"),
                    Pair(FiltroDepartamento.ELIMINADAS, "Papelera")
                )
                chips.forEach { (filtro, label) ->
                    FilterChip(
                        selected = filtroActual == filtro,
                        onClick = { viewModel.cargarDepartamentos(filtro) },
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
                placeHolder = "Buscar departamento..."
            )

            // Logica de ordenamiento y filtrado local
            val departamentosProcesados by remember(departamentos, searchQuery, ascendente, criterioOrden) {
                derivedStateOf {
                    departamentos
                        .filter { depto ->
                            depto.nomDep.contains(searchQuery, ignoreCase = true) ||
                                    depto.codDep.toString().contains(searchQuery)
                        }
                        .let { filtradas ->
                            // SOLUCIÓN: Separamos la lógica para que Kotlin sepa el tipo exacto
                            when (criterioOrden) {
                                CriterioOrden.NOMBRE -> {
                                    if (ascendente) filtradas.sortedBy { it.nomDep }
                                    else filtradas.sortedByDescending { it.nomDep }
                                }
                                CriterioOrden.CODIGO -> {
                                    if (ascendente) filtradas.sortedBy { it.codDep }
                                    else filtradas.sortedByDescending { it.codDep }
                                }
                            }
                        }
                }
            }

            // LISTA DE DEPARTAMENTOS
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // --- LÓGICA DE LISTA VACÍA ---
                if (departamentosProcesados.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier.fillParentMaxSize().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Un icono gris para ilustrar
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No se encontraron departamentos",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Intenta cambiar los filtros o la búsqueda",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                } else {
                    items(departamentosProcesados) { departamento ->
                        DepartamentoItem(
                            departamento = departamento,
                            onActivar = { if (!isProcessing) viewModel.activarDepartamento(it) },
                            onInactivar = { if (!isProcessing) viewModel.inactivarDepartamento(it) },
                            onEliminar = {
                                if (!isProcessing) viewModel.eliminarLogicoDepartamento(
                                    it
                                )
                            },
                            onEliminarFisico = {
                                if (!isProcessing) viewModel.deleteDepartamento(
                                    departamento
                                )
                            },
                            onEditar = { if (!isProcessing) navController.navigate("edit_departamento/${departamento.codDep}") }
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}