package com.idnp2025b.solucionesmoviles.ui.screens.planta

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Nueva Planta",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(32.dp))

        // imputs
        OutlinedTextField(
            value = nombrePlanta,
            onValueChange = { nombrePlanta = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            enabled = uiState !is UiState.Loading
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = "A",
            onValueChange = {},
            label = { Text("Estado") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = false
        )

        Spacer(Modifier.height(24.dp))
        // guardar
        Button(
            onClick = {
                val nombreLimpio = nombrePlanta.trim()
                if (nombreLimpio.isNotBlank()) {
                    viewModel.agregarPlanta(nombreLimpio)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = uiState !is UiState.Loading && nombrePlanta.isNotBlank()
        ) {
            if (uiState is UiState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("GUARDAR", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}