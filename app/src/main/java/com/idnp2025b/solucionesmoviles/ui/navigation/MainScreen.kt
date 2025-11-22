package com.idnp2025b.solucionesmoviles.ui.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

// Imports de Iconos
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle

// Imports de Runtime y Navegación
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.idnp2025b.solucionesmoviles.data.SessionManager
import com.idnp2025b.solucionesmoviles.ui.screens.planta.EditarPlanta
import com.idnp2025b.solucionesmoviles.ui.screens.Home
import com.idnp2025b.solucionesmoviles.ui.screens.departamento.CrearDepartamento
import com.idnp2025b.solucionesmoviles.ui.screens.departamento.Departamento
import com.idnp2025b.solucionesmoviles.ui.screens.departamento.EditarDepartamento
import com.idnp2025b.solucionesmoviles.ui.screens.login.Login
import com.idnp2025b.solucionesmoviles.ui.screens.planta.CrearPlanta
import com.idnp2025b.solucionesmoviles.ui.screens.planta.Planta
import com.idnp2025b.solucionesmoviles.ui.screens.taller.CrearTaller
import com.idnp2025b.solucionesmoviles.ui.screens.taller.EditarTaller
import com.idnp2025b.solucionesmoviles.ui.screens.taller.Taller
import com.idnp2025b.solucionesmoviles.ui.screens.tipotaller.CrearTipoTaller
import com.idnp2025b.solucionesmoviles.ui.screens.tipotaller.EditarTipoTaller
import com.idnp2025b.solucionesmoviles.ui.screens.tipotaller.TipoTaller
import com.idnp2025b.solucionesmoviles.ui.theme.miEstiloTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    var usuarioActual by remember { mutableStateOf(sessionManager.obtenerUsuario()) }
    val startRoute = if (usuarioActual != null) "home" else "login"

    val currentScreenTitle = when {
        currentDestination?.route == "login" -> "Inicio de sesion"
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentScreenTitle) },
                colors = miEstiloTopBar(),
                navigationIcon = {
                    if (currentDestination?.route != "home" && currentDestination?.route != "login") {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                        }
                    }
                },
                actions = {
                    if (usuarioActual != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 16.dp) // Un poco de margen a la derecha
                        ) {
                            // Nombre del usuario
                            Text(
                                text = usuarioActual ?: "",
                                style = MaterialTheme.typography.labelLarge,
                                // Importante: Usar onPrimary para que se vea blanco sobre la barra azul
                                color = MaterialTheme.colorScheme.onPrimary
                            )

                            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre nombre e icono

                            // Icono de persona
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Usuario",
                                modifier = Modifier.size(30.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        // Aquí se definen TODAS las rutas de la App
        NavHost(
            navController = navController,
            startDestination = startRoute, //pantalla inicial
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("login") {
                Login(
                    navController = navController,
                    onLoginSuccess = { nuevoUsuario ->
                        sessionManager.guardarUsuario(nuevoUsuario)
                        usuarioActual = nuevoUsuario
                    }
                )
            }
            composable("home") {
                // Verificamos si hay usuario (Protección extra)
                if (usuarioActual != null) {
                    Home(
                        navController = navController,
                        onLogout = {
                            sessionManager.borrarSesion()
                            usuarioActual = null
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
            // Rutas de taller
            composable("taller") {
                Taller(navController = navController)
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
            // Rutas de tipo de taller
            composable("tipo_taller") {
                TipoTaller(navController = navController)
            }
            composable("new_tipo_taller") {
                CrearTipoTaller(navController = navController)
            }
            composable(
                route = "edit_tipo_taller/{codTipTal}",
                arguments = listOf(navArgument("codTipTal") { type = NavType.IntType })
            ) { backStackEntry ->
                val codTipTal = backStackEntry.arguments?.getInt("codTipTal")
                if (codTipTal != null) {
                    EditarTipoTaller(navController = navController, codTipTal = codTipTal)
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
