package com.example.appproyecto2k.ui.theme.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun EstockBottomBar(navController: NavHostController) {
    NavigationBar(containerColor = Color.White) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val items = listOf("inicio", "añadir", "stock", "menu", "perfil")

        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(imageVector = getIconForRoute(screen), contentDescription = screen)
                },
                label = {

                    Text(screen.replaceFirstChar { it.uppercase() })
                },
                selected = currentRoute == screen,
                onClick = {

                    navController.navigate(screen) {
                        // Al pulsar, limpia la pila hasta el inicio para no acumular pantallas
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        // Restaura el estado al volver
                        restoreState = true
                    }
                }
            )
        }
    }
}


fun getIconForRoute(route: String): ImageVector {
    return when (route) {
        "inicio" -> Icons.Default.Home
        "añadir" -> Icons.Default.Add
        "stock" -> Icons.Default.ShoppingCart
        "menu" -> Icons.Default.Menu
        "perfil" -> Icons.Default.Person
        else -> Icons.Default.Home
    }
}