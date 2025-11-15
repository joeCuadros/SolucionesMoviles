package com.idnp2025b.solucionesmoviles.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.idnp2025b.solucionesmoviles.viewmodel.PlantaViewModel

@Composable
fun Planta(navController: NavController, viewModel: PlantaViewModel = hiltViewModel()) {
    val plantas = viewModel.plantas.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(plantas) { planta ->
                Text(
                    "🌱 ${planta.nomPla}",
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        Button(onClick = { viewModel.agregar("Nueva Planta") }) {
            Text("Agregar planta")
        }
    }
}