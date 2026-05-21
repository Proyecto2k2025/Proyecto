package com.example.appproyecto2k.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

// Imports explícitos
import com.example.appproyecto2k.domain.Usuario
import com.example.appproyecto2k.ui.theme.screens.*

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    // Estado local para el usuario (esto debería estar en un ViewModel en una app real)
    var currentUser by remember { 
        mutableStateOf(Usuario(id = 123L, nombre = "Abdel Samini", rol = "Admin")) 
    }

    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("inicio") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate("registro")
                }
            )
        }

        composable("registro") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("inicio") {
                        popUpTo("registro") { inclusive = true }
                        popUpTo("login") { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("inicio") { InicioScreen() }
        composable("añadir") { IngresoProductosScreen() }
        composable("stock") { InventarioScreen() }
        composable("menu") { MenuScreen(navController) }

        composable("perfil") {
            PerfilScreen(
                usuario = currentUser,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onEditClick = {
                    navController.navigate("editar_perfil")
                }
            )
        }

        composable("editar_perfil") {
            EditarPerfilScreen(
                usuario = currentUser,
                onSaveSuccess = { usuarioActualizado ->
                    currentUser = usuarioActualizado
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun PantallaConstruccion(nombrePantalla: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Pantalla: $nombrePantalla (En construcción)")
    }
}
