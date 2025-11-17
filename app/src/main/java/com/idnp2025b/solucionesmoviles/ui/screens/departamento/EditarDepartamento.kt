package com.idnp2025b.solucionesmoviles.ui.screens.departamento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.idnp2025b.solucionesmoviles.ui.effects.HandleUiStateEffects
import com.idnp2025b.solucionesmoviles.viewmodel.DepartamentoViewModel
import com.idnp2025b.solucionesmoviles.viewmodel.UiState

@Composable
fun EditarDepartamento(
    navController: NavController,
    codDep: Int,
    viewModel: DepartamentoViewModel = hiltViewModel()
) {
    var nombreDepartamento by remember { mutableStateOf("") }
        val uiState by viewModel.uiState.collectAsState()
    val departamento by viewModel.obtenerDepartamento(codDep).collectAsState(initial = null)

    LaunchedEffect(departamento) {
        departamento?.let {
            nombreDepartamento = it.nomDep // Rellena el TextField con el nombre actual
        }
    }

    HandleUiStateEffects(
        uiState = uiState,
        onResetState = { viewModel.resetState() },
        onSuccess = { navController.popBackStack() }
    )

    if (departamento == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Editar Departamento", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(24.dp))

            // Campo de Código (no editable)
            OutlinedTextField(
                value = codDep.toString(),
                onValueChange = {},
                label = { Text("Código de Departamento") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                enabled = false
            )

            Spacer(Modifier.height(16.dp))

            // Campo de Nombre (editable)
            OutlinedTextField(
                value = nombreDepartamento,
                onValueChange = { nombreDepartamento = it },
                label = { Text("Nombre del departamento") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = uiState !is UiState.Loading
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    if (nombreDepartamento.isNotBlank()) {
                        departamento?.let { deptoOriginal ->
                            val deptoActualizado = deptoOriginal.copy(
                                nomDep = nombreDepartamento
                            )
                            viewModel.actualizarDepartamento(deptoActualizado)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is UiState.Loading && nombreDepartamento.isNotBlank()
            ) {
                if (uiState is UiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Actualizar Cambios")
                }
            }
        }
    }
}