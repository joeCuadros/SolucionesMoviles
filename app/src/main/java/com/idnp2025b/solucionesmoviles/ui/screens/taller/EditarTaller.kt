package com.idnp2025b.solucionesmoviles.ui.screens.taller

import android.widget.Toast
import androidx.compose.foundation.layout.*
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

    // Datos generales
    val plantas by plantaViewModel.plantas.collectAsState()
    val deptos by deptoViewModel.departamentos.collectAsState()
    val tipos by tipoViewModel.tipoTalleres.collectAsState()

    // Datos específicos del taller a editar
    val tallerDetalle by viewModel.obtenerTaller(codTal).collectAsState(initial = null)

    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 1. Cargar las listas de opciones
    LaunchedEffect(Unit) {
        plantaViewModel.cargarPlantas(FiltroPlanta.ACTIVAS)
        deptoViewModel.cargarDepartamentos(FiltroDepartamento.ACTIVAS)
        tipoViewModel.cargarTipoTalleres(FiltroTipoTaller.ACTIVAS)
    }

    // 2. Cuando tengamos el taller Y las listas, pre-llenamos los campos
    LaunchedEffect(tallerDetalle, plantas, deptos, tipos) {
        tallerDetalle?.let { detalle ->
            // Llenar nombre
            if (nombreTaller.isEmpty()) nombreTaller = detalle.taller.nomTal

            // Llenar Selectores (Buscar en las listas el objeto que coincida con el ID)
            if (selectedPlanta == null) {
                selectedPlanta = plantas.find { it.codPla == detalle.taller.codPla }
            }
            if (selectedDepto == null) {
                selectedDepto = deptos.find { it.codDep == detalle.taller.codDep }
            }
            if (selectedTipo == null) {
                selectedTipo = tipos.find { it.codTipTal == detalle.taller.codTipTal }
            }
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
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Editar Taller", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(24.dp))

            // ID (No editable)
            OutlinedTextField(
                value = codTal.toString(),
                onValueChange = {},
                label = { Text("Código") },
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Nombre
            OutlinedTextField(
                value = nombreTaller,
                onValueChange = { nombreTaller = it },
                label = { Text("Nombre del Taller") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            // Selectores
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

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombreTaller.isNotBlank() && selectedPlanta != null && selectedDepto != null && selectedTipo != null) {
                        // Creamos un objeto Taller simple actualizado (Mantenemos el ID original)
                        val tallerActualizado = tallerDetalle!!.taller.copy(
                            nomTal = nombreTaller,
                            codPla = selectedPlanta!!.codPla,
                            codDep = selectedDepto!!.codDep,
                            codTipTal = selectedTipo!!.codTipTal
                        )
                        viewModel.actualizarTaller(tallerActualizado)
                    } else {
                        Toast.makeText(context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is UiState.Loading
            ) {
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Actualizar Cambios")
                }
            }
        }
    }
}