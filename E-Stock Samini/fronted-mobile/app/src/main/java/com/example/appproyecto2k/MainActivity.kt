package com.example.appproyecto2k

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appproyecto2k.navigation.NavGraph
import com.example.appproyecto2k.ui.theme.AppProyecto2kTheme
import com.example.appproyecto2k.ui.theme.components.EstockBottomBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppProyecto2kTheme {
                val navController = rememberNavController()
                

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                

                val showBottomBar = currentRoute != null && 
                                   currentRoute != "login" && 
                                   currentRoute != "registro"

                Scaffold(
                    bottomBar = { 
                        if (showBottomBar) {
                            EstockBottomBar(navController) 
                        }
                    }
                ) { innerPadding ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}