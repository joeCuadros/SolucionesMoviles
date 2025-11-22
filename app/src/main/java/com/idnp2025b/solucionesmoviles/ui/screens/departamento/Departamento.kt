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
import com.idnp2025b.solucionesmoviles.ui.components.departamento.DepartamentoItem
import com.idnp2025b.solucionesmoviles.ui.components.general.BotonFlotante
import com.idnp2025b.solucionesmoviles.ui.components.general.Buscador
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
                placeHolder = "Buscar departamento..."
            )

            // Logica de ordenamiento y filtrado local
            val departamentosProcesados = remember(departamentos, searchQuery, ascendente) {
                departamentos
                    .filter { depto ->
                        depto.nomDep.contains(searchQuery, ignoreCase = true)
                    }
                    .let { filtradas ->
                        if (ascendente) filtradas.sortedBy { it.nomDep }
                        else filtradas.sortedByDescending { it.nomDep }
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
                items(departamentosProcesados) { departamento ->
                    DepartamentoItem(
                        departamento = departamento,
                        onActivar = { viewModel.activarDepartamento(it) },
                        onInactivar = { viewModel.inactivarDepartamento(it) },
                        onEliminar = { viewModel.eliminarLogicoDepartamento(it) },
                        onEliminarFisico = { viewModel.deleteDepartamento(departamento) },
                        onEditar = { navController.navigate("edit_departamento/${departamento.codDep}") }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}