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
import com.idnp2025b.solucionesmoviles.viewmodel.FiltroDepartamento
import com.idnp2025b.solucionesmoviles.viewmodel.DepartamentoViewModel
import com.idnp2025b.solucionesmoviles.viewmodel.UiState

@Composable
fun Departamento(
    navController: NavController, viewModel: DepartamentoViewModel = hiltViewModel()
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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Filtros de estado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = filtroActual == FiltroDepartamento.TODAS,
                onClick = { viewModel.cargarDepartamentos(FiltroDepartamento.TODAS) },
                label = { Text("Todos") }
            )
            FilterChip(
                selected = filtroActual == FiltroDepartamento.ACTIVAS,
                onClick = { viewModel.cargarDepartamentos(FiltroDepartamento.ACTIVAS) },
                label = { Text("Activos") }
            )
            FilterChip(
                selected = filtroActual == FiltroDepartamento.INACTIVAS,
                onClick = { viewModel.cargarDepartamentos(FiltroDepartamento.INACTIVAS) },
                label = { Text("Inactivos") }
            )
            FilterChip(
                selected = filtroActual == FiltroDepartamento.ELIMINADAS,
                onClick = { viewModel.cargarDepartamentos(FiltroDepartamento.ELIMINADAS) },
                label = { Text("Eliminados") }
            )
        }

        // Fila para Buscador y Orden
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
                label = { Text("Buscar por nombre...") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") }
            )
            IconToggleButton(
                checked = ascendente,
                onCheckedChange = { ascendente = !ascendente }
            ) {
                if (ascendente) {
                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Orden Ascendente")
                } else {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Orden Descendente")
                }
            }
        }

        val departamentosProcesados = remember(departamentos, searchQuery, ascendente) {
            departamentos
                .filter { depto ->
                    depto.nomDep.contains(searchQuery, ignoreCase = true)
                }
                .let { filtradas ->
                    if (ascendente) {
                        filtradas.sortedBy { it.nomDep }
                    } else {
                        filtradas.sortedByDescending { it.nomDep }
                    }
                }
        }

        // Lista de departamentos
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(departamentosProcesados) { departamento ->
                DepartamentoItem (
                    departamento = departamento,
                    onActivar = { viewModel.activarDepartamento(it) },
                    onInactivar = { viewModel.inactivarDepartamento(it) },
                    onEliminar = { viewModel.eliminarLogicoDepartamento(it) },
                    onEliminarFisico = { viewModel.deleteDepartamento(departamento) }, // Pasas el objeto
                    onEditar = { navController.navigate("edit_departamento/${departamento.codDep}")}
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Botón agregar
        Button(
            onClick = { navController.navigate("new_departamento") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Agregar nuevo departamento")
        }
    }
}