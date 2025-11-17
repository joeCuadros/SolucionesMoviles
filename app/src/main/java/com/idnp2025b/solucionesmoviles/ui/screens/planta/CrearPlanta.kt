package com.idnp2025b.solucionesmoviles.ui.screens.planta

import androidx.compose.foundation.layout.Arrangement
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
import com.idnp2025b.solucionesmoviles.viewmodel.PlantaViewModel
import com.idnp2025b.solucionesmoviles.viewmodel.UiState

@Composable
fun CrearPlanta(
    navController: NavController,
    viewModel: PlantaViewModel = hiltViewModel()
) {
    var nombrePlanta by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    HandleUiStateEffects(
        uiState = uiState,
        onResetState = { viewModel.resetState() },
        onSuccess = { navController.popBackStack() }
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Formulario de nueva Planta", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = nombrePlanta,
            onValueChange = { nombrePlanta = it },
            label = { Text("Nombre de la planta") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = uiState !is UiState.Loading
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                if (nombrePlanta.isNotBlank()) {
                    viewModel.agregarPlanta(nombrePlanta)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is UiState.Loading && nombrePlanta.isNotBlank()
        ) {
            if (uiState is UiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Guardar planta")
            }
        }
    }
}