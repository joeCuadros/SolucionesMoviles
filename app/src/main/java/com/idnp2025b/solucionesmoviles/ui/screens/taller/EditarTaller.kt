package com.idnp2025b.solucionesmoviles.ui.screens.taller

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.idnp2025b.solucionesmoviles.data.entities.Departamento
import com.idnp2025b.solucionesmoviles.data.entities.Planta
import com.idnp2025b.solucionesmoviles.data.entities.TipoTaller
import com.idnp2025b.solucionesmoviles.ui.components.general.Selector
import com.idnp2025b.solucionesmoviles.viewmodel.*

@Composable
fun EditarTaller(
    navController: NavController,
    codTal: Int,
    viewModel: TallerViewModel = hiltViewModel(),
    plantaViewModel: PlantaViewModel = hiltViewModel(),
    deptoViewModel: DepartamentoViewModel = hiltViewModel(),
    tipoViewModel: TipoTallerViewModel = hiltViewModel()
) {
    var nombreTaller by remember { mutableStateOf("") }
    var selectedPlanta by remember { mutableStateOf<Planta?>(null) }
    var selectedDepto by remember { mutableStateOf<Departamento?>(null) }
    var selectedTipo by remember { mutableStateOf<TipoTaller?>(null) }
    // Variable para mostrar el estado actual en el campo simple
    var estadoActual by remember { mutableStateOf("") }

    val plantas by plantaViewModel.plantas.collectAsState()
    val deptos by deptoViewModel.departamentos.collectAsState()
    val tipos by tipoViewModel.tipoTalleres.collectAsState()

    val tallerDetalle by viewModel.obtenerTaller(codTal).collectAsState(initial = null)
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        plantaViewModel.cargarPlantas(FiltroPlanta.ACTIVAS)
        deptoViewModel.cargarDepartamentos(FiltroDepartamento.ACTIVAS)
        tipoViewModel.cargarTipoTalleres(FiltroTipoTaller.ACTIVAS)
    }

    LaunchedEffect(tallerDetalle, plantas, deptos, tipos) {
        tallerDetalle?.let { detalle ->
            if (nombreTaller.isEmpty()) nombreTaller = detalle.taller.nomTal
            estadoActual = detalle.taller.estTal // Capturamos el estado

            if (selectedPlanta == null) selectedPlanta = plantas.find { it.codPla == detalle.taller.codPla }
            if (selectedDepto == null) selectedDepto = deptos.find { it.codDep == detalle.taller.codDep }
            if (selectedTipo == null) selectedTipo = tipos.find { it.codTipTal == detalle.taller.codTipTal }
        }
    }

    LaunchedEffect(uiState) {
        when(val state = uiState) {
            is UiState.Success -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
                navController.popBackStack()
            }
            is UiState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    if (tallerDetalle == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Editar Taller",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = codTal.toString(),
                onValueChange = {},
                label = { Text("Código") },
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = nombreTaller,
                onValueChange = { nombreTaller = it },
                label = { Text("Nombre del Taller") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                enabled = uiState !is UiState.Loading
            )

            Spacer(Modifier.height(16.dp))

            Selector(
                label = "Planta",
                options = plantas,
                selectedOption = selectedPlanta,
                onOptionSelected = { selectedPlanta = it },
                itemLabel = { it.nomPla }
            )

            Spacer(Modifier.height(16.dp))

            Selector(
                label = "Departamento",
                options = deptos,
                selectedOption = selectedDepto,
                onOptionSelected = { selectedDepto = it },
                itemLabel = { it.nomDep }
            )

            Spacer(Modifier.height(16.dp))

            Selector(
                label = "Tipo de Taller",
                options = tipos,
                selectedOption = selectedTipo,
                onOptionSelected = { selectedTipo = it },
                itemLabel = { it.nomTipTal }
            )

            Spacer(Modifier.height(16.dp))

            // CAMPO DE ESTADO SIMPLE
            OutlinedTextField(
                value = estadoActual,
                onValueChange = {},
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    val nombreLimpio = nombreTaller.trim()
                    if (nombreLimpio.isNotBlank() && selectedPlanta != null && selectedDepto != null && selectedTipo != null) {
                        val tallerActualizado = tallerDetalle!!.taller.copy(
                            nomTal = nombreLimpio,
                            codPla = selectedPlanta!!.codPla,
                            codDep = selectedDepto!!.codDep,
                            codTipTal = selectedTipo!!.codTipTal
                        )
                        viewModel.actualizarTaller(tallerActualizado)
                    } else {
                        Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = uiState !is UiState.Loading
            ) {
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ACTUALIZAR CAMBIOS", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}