package com.example.appproyecto2k.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.appproyecto2k.data.RetrofitClient
import com.example.appproyecto2k.domain.Usuario
import com.example.appproyecto2k.ui.theme.EstockBlue
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineLarge,
            color = EstockBlue
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre de usuario") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            enabled = !isLoading
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onNavigateToRegister, enabled = !isLoading) {
                Text("Crear cuenta", color = EstockBlue)
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = EstockBlue,
                    strokeWidth = 2.dp
                )
            } else {
                IconButton(
                    onClick = {
                        if (nombre.isBlank() || password.isBlank()) {
                            errorMessage = "Por favor, completa todos los campos."
                            return@IconButton
                        }

                        scope.launch {
                            isLoading = true
                            errorMessage = null
                            try {
                                val loginRequest = Usuario(nombre = nombre, password = password)
                                val response = RetrofitClient.instance.login(loginRequest)
                                
                                if (response != null) {
                                    onLoginSuccess()
                                } else {
                                    errorMessage = "Usuario o contraseña incorrectos."
                                }
                            } catch (e: Exception) {
                                errorMessage = "Error: Usuario o contraseña incorrectos"
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    modifier = Modifier.background(EstockBlue, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Ingresar",
                        tint = Color.White
                    )
                }
            }
        }
    }
}