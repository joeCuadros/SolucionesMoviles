package com.idnp2025b.solucionesmoviles.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun Home(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("El punto de entrada", style = MaterialTheme.typography.titleLarge)
    }
}

/*
* @Composable
fun LibretaScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Pantalla de LIBRETA", style = MaterialTheme.typography.titleLarge)
    }
}
@Composable
fun PerfilScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 🔥 CAMBIO: 'titleLarge' es el estilo de M3
        Text("Pantalla de PERFIL", style = MaterialTheme.typography.titleLarge)

        Button(onClick = {
            navController.navigate("detalle") // Navega a la ruta "detalle"
        }) {
            Text("Ir a Detalle")
        }
        Button(onClick = {
            // Navega a la ruta "detalle" mandando un ID
            navController.navigate("detalle2/12345")
        }) {
            Text("Ir a Detalle con ID 12345")
        }
    }
}
* */