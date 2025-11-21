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
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place

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
import com.idnp2025b.solucionesmoviles.ui.screens.planta.EditarPlanta
import com.idnp2025b.solucionesmoviles.ui.screens.Home
import com.idnp2025b.solucionesmoviles.ui.screens.departamento.CrearDepartamento
import com.idnp2025b.solucionesmoviles.ui.screens.departamento.Departamento
import com.idnp2025b.solucionesmoviles.ui.screens.departamento.EditarDepartamento
import com.idnp2025b.solucionesmoviles.ui.screens.planta.CrearPlanta
import com.idnp2025b.solucionesmoviles.ui.screens.planta.Planta
import com.idnp2025b.solucionesmoviles.ui.screens.taller.CrearTaller
import com.idnp2025b.solucionesmoviles.ui.screens.taller.EditarTaller
import com.idnp2025b.solucionesmoviles.ui.screens.taller.Taller
import com.idnp2025b.solucionesmoviles.ui.screens.tipotaller.CrearTipoTaller
import com.idnp2025b.solucionesmoviles.ui.screens.tipotaller.EditarTipoTaller
import com.idnp2025b.solucionesmoviles.ui.screens.tipotaller.TipoTaller

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem("Taller", "taller", Icons.Default.Build),
        BottomNavItem("Departamento", "departamento", Icons.Default.Place),
        BottomNavItem("Tipo de Taller", "tipo_taller", Icons.AutoMirrored.Filled.List),
        BottomNavItem("Planta", "planta", Icons.Default.LocationOn),
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentScreenTitle = when { // 1. Quita la variable de aquí
        // 2. Ahora compara en cada línea
        currentDestination?.route == "home" -> "Inicio"
        // gestion de taller
        currentDestination?.route == "taller" -> "Talleres"
        currentDestination?.route == "new_taller" -> "Nuevo Taller"
        currentDestination?.route?.startsWith("edit_taller") == true -> "Editar Taller"
        // gestion de tipo de taller
        currentDestination?.route == "tipo_taller" -> "Tipos de Taller"
        currentDestination?.route == "new_tipo_taller" -> "Nuevo Tipo de Taller"
        currentDestination?.route?.startsWith("edit_tipo_taller") == true -> "Editar Tipo de Taller"
        // gestion de departamento
        currentDestination?.route == "departamento" -> "Departamentos"
        currentDestination?.route == "new_departamento" -> "Nuevo Departamento"
        currentDestination?.route?.startsWith("edit_departamento") == true -> "Editar Departamento"
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
            // Rutas de taller
            composable("taller") {
                Taller(navController = navController)
            }
            // Rutas de tipo de taller
            composable("tipo_taller") {
                TipoTaller(navController = navController)
            }
            composable("new_taller") {
                CrearTaller(navController = navController)
            }
            composable(
                route = "edit_taller/{codTal}",
                arguments = listOf(navArgument("codTal") { type = NavType.IntType })
            ) { backStackEntry ->
                val codTal = backStackEntry.arguments?.getInt("codTal")
                if (codTal != null) {
                    EditarTaller(navController = navController, codTal = codTal)
                } else {
                    navController.popBackStack()
                }
            }
            //Rutas de Departamento
            composable("departamento") {
                // Asumo que tu pantalla se llama 'Departamento'
                Departamento(navController = navController)
            }
            composable("new_departamento") {
                CrearDepartamento(navController = navController)
            }
            composable(
                route = "edit_departamento/{codDep}",
                arguments = listOf(navArgument("codDep") { type = NavType.IntType })
            ) { backStackEntry ->
                val codDep = backStackEntry.arguments?.getInt("codDep")
                if (codDep != null) {
                    EditarDepartamento(navController = navController, codDep = codDep)
                } else {
                    navController.popBackStack()
                }
            }
            //Rutas de Planta
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
        }
    }
}
data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)
