package com.idnp2025b.solucionesmoviles.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.idnp2025b.solucionesmoviles.ui.components.general.ItemHome


data class MenuItem(
    val titulo: String,
    val icono: ImageVector,
    val rutaNavegacion: String
)
@Composable
fun Home(navController: NavController) {
    // datos iniciales
    val opcionesMenu = listOf(
        MenuItem("Talleres", Icons.Filled.Build, "taller"),
        MenuItem("Departamentos", Icons.Default.AccountBox, "departamento"),
        MenuItem("Tipos de Taller", Icons.AutoMirrored.Filled.List, "tipo_taller"),
        MenuItem("Plantas", Icons.Default.HomeWork, "planta")
    )
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Título de la pantalla
        Text(
            text = "Panel de Gestión",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(opcionesMenu) { opcion ->
                ItemHome(
                    menuItem = opcion,
                    onClick = { navController.navigate(opcion.rutaNavegacion) }
                )
            }
        }
    }

}
