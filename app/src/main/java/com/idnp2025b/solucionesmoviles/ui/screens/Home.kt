package com.idnp2025b.solucionesmoviles.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.filled.DeleteForever // Icono para borrar
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Storage // Icono para cargar datos
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.idnp2025b.solucionesmoviles.ui.components.general.DialogoEliminar
import com.idnp2025b.solucionesmoviles.ui.components.general.ItemHome
import com.idnp2025b.solucionesmoviles.viewmodel.ConfiguracionViewModel
import com.idnp2025b.solucionesmoviles.viewmodel.UiState

data class MenuItem(
    val titulo: String,
    val icono: ImageVector,
    val accion: () -> Unit
)

@Composable
fun Home(
    navController: NavController,
    viewModel: ConfiguracionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isDbEmpty by viewModel.isDbEmpty.collectAsState()
    val context = LocalContext.current

    var mostrarAlertaBorrar by remember { mutableStateOf(false) }

    // Manejo de respuestas (Toast)
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

    // Alerta de seguridad antes de borrar TODO
    if (mostrarAlertaBorrar) {
        DialogoEliminar(
            onDismiss = { mostrarAlertaBorrar = false },
            onConfirm = {
                viewModel.borrarTodo()
                mostrarAlertaBorrar = false
            },
            nombreItem = "TODA LA BASE DE DATOS",
            esPermanente = true
        )
    }

    val opcionesMenu = remember(isDbEmpty) {
        buildList {
            add(MenuItem("Gestión de\nTalleres", Icons.Filled.Build) { navController.navigate("taller") })
            add(MenuItem("Gestión de\nDepartamentos", Icons.Default.AccountBox) { navController.navigate("departamento") })
            add(MenuItem("Gestión de\nTipos de Taller", Icons.AutoMirrored.Filled.List) { navController.navigate("tipo_taller") })
            add(MenuItem("Gestión de\nPlantas", Icons.Default.HomeWork) { navController.navigate("planta") })

            if (isDbEmpty) {
                add(MenuItem("Cargar Datos", Icons.Default.Storage) {
                    viewModel.inicializarDatosDePrueba()
                })
            }

            add(MenuItem("Borrar BD", Icons.Default.DeleteForever) {
                mostrarAlertaBorrar = true
            })
        }
    }

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
                    onClick = opcion.accion
                )
            }
        }
    }
}