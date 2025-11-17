package com.idnp2025b.solucionesmoviles.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar // <-- No es BottomNavigation
import androidx.compose.material3.NavigationBarItem // <-- No es BottomNavigationItem
import androidx.compose.foundation.layout.padding

// Imports de Iconos
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn

// Imports de Runtime y Navegación
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.idnp2025b.solucionesmoviles.ui.screens.EditarPlanta
import com.idnp2025b.solucionesmoviles.ui.screens.Home
import com.idnp2025b.solucionesmoviles.ui.screens.CrearPlanta
import com.idnp2025b.solucionesmoviles.ui.screens.Planta
import com.idnp2025b.solucionesmoviles.ui.screens.Taller
import com.idnp2025b.solucionesmoviles.ui.screens.Tipo_taller

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem("Home", "home", Icons.Default.Home),
        BottomNavItem("Taller", "taller", Icons.Default.Build),
        BottomNavItem("Tipo de Taller", "tipo_taller", Icons.AutoMirrored.Filled.List),
        BottomNavItem("Planta", "planta", Icons.Default.LocationOn),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentScreenTitle = when { // 1. Quita la variable de aquí
        // 2. Ahora compara en cada línea
        currentDestination?.route == "home" -> "Inicio"
        currentDestination?.route == "taller" -> "Talleres"
        currentDestination?.route == "tipo_taller" -> "Tipos de taller"
        currentDestination?.route == "departamento" -> "Departamentos"

        // gestion de plantas
        currentDestination?.route == "planta" -> "Plantas"
        currentDestination?.route == "new_planta" -> "Nueva Planta"
        currentDestination?.route?.startsWith("edit_planta") == true -> "Editar Planta"
        else -> "App"
    }
    // validar si esta dentro de los principales o no
    val canPop = items.none { it.route == currentDestination?.route }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentScreenTitle) },
                navigationIcon = {
                    if (canPop) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                items.forEach { item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        // Aquí se definen TODAS las rutas de la App
        NavHost(
            navController = navController,
            startDestination = "home", //pantalla inicial
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                Home(navController = navController)
            }
            composable("taller") {
                Taller(navController = navController)
            }
            composable("tipo_taller") {
                Tipo_taller(navController = navController)
            }
            composable("planta") {
                Planta(navController = navController)
            }
            composable("new_planta") {
                CrearPlanta(navController = navController)
            }
            composable(
                route = "edit_planta/{codPla}", // El placeholder
                arguments = listOf(navArgument("codPla") { type = NavType.IntType }) // Define el argumento
            ) { backStackEntry ->
                val codPla = backStackEntry.arguments?.getInt("codPla")
                if (codPla != null) {
                    EditarPlanta(navController = navController, codPla = codPla)
                } else {
                    navController.popBackStack()
                }
            }
            /*
            composable(
                route = "detalle2/{itemId}", // El placeholder
                arguments = listOf(navArgument("itemId") { type = NavType.StringType }) // Define el argumento
            ) { backStackEntry ->
                // Extrae el ID de los argumentos de la ruta
                val itemId = backStackEntry.arguments?.getString("itemId")
                // Pasa el ID a la pantalla de Detalle
                DetalleScreen2(itemId = itemId)
            }*/
        }
    }
}
data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)


/*
@Composable
fun PerfilScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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

/*
@Composable
fun DetalleScreen2(itemId: String?) { // Recibe el ID
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Pantalla de DETALLE", style = MaterialTheme.typography.titleLarge)

        // Muestra el ID que recibió
        Text(
            "Has llegado con el ID: ${itemId ?: "NINGUNO"}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}*/