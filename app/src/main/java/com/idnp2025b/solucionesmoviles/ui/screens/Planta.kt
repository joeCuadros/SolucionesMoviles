package com.idnp2025b.solucionesmoviles.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.idnp2025b.solucionesmoviles.ui.components.planta.PlantaItem
import com.idnp2025b.solucionesmoviles.viewmodel.FiltroPlanta
import com.idnp2025b.solucionesmoviles.viewmodel.PlantaViewModel
import com.idnp2025b.solucionesmoviles.viewmodel.UiState

@Composable
fun Planta(
    navController: NavController, viewModel: PlantaViewModel = hiltViewModel()
) {
    val plantas by viewModel.plantas.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val filtroActual by viewModel.filtroActual.collectAsState()
    val context = LocalContext.current

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
        // Filtros
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                selected = filtroActual == FiltroPlanta.TODAS,
                onClick = { viewModel.cargarPlantas(FiltroPlanta.TODAS) },
                label = { Text("Todas") }
            )
            FilterChip(
                selected = filtroActual == FiltroPlanta.ACTIVAS,
                onClick = { viewModel.cargarPlantas(FiltroPlanta.ACTIVAS) },
                label = { Text("Activas") }
            )
            FilterChip(
                selected = filtroActual == FiltroPlanta.INACTIVAS,
                onClick = { viewModel.cargarPlantas(FiltroPlanta.INACTIVAS) },
                label = { Text("Inactivas") }
            )
        }

        // Lista de plantas
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(plantas) { planta ->
                PlantaItem (
                    planta = planta,
                    onActivar = { viewModel.activarPlanta(it) },
                    onInactivar = { viewModel.inactivarPlanta(it) },
                    onEliminar = { viewModel.eliminarLogico(it) },
                    onEliminarFisico = { viewModel.deletePlanta(it) }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Botón agregar
        Button(
            onClick = { navController.navigate("new_planta") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Agregar nueva planta")
        }
    }
}

