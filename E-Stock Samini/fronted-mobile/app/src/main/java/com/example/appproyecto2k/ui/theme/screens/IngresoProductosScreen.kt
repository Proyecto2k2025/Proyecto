package com.example.appproyecto2k.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appproyecto2k.data.RetrofitClient
import com.example.appproyecto2k.domain.Producto
import com.example.appproyecto2k.domain.Variante
import com.example.appproyecto2k.ui.theme.components.CustomTextField
import kotlinx.coroutines.launch

@Composable
fun IngresoProductosScreen() {
    var nombreProducto by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var existencias by remember { mutableStateOf("") }
    var precioVenta by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ingreso de productos", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    CustomTextField(label = "Nombre del producto", value = nombreProducto, onValueChange = { nombreProducto = it })
                    CustomTextField(label = "Categoría", value = categoria, onValueChange = { categoria = it })
                    CustomTextField(label = "Número de existencias", value = existencias, onValueChange = { existencias = it })
                    CustomTextField(label = "Precio de venta", value = precioVenta, onValueChange = { precioVenta = it })

                    Spacer(modifier = Modifier.height(24.dp))

                    if (message != null) {
                        Text(text = message!!, color = if (message!!.contains("Error")) Color.Red else Color.Green)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Button(
                            onClick = { 
                                nombreProducto = ""; categoria = ""; existencias = ""; precioVenta = ""
                                message = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier.weight(1f).padding(end = 8.dp),
                            enabled = !isLoading
                        ) {
                            Text("Limpiar", color = Color.Black)
                        }
                        Button(
                            onClick = {
                                if (nombreProducto.isBlank() || existencias.isBlank() || precioVenta.isBlank()) {
                                    message = "Error: Rellena los campos obligatorios"
                                    return@Button
                                }
                                scope.launch {
                                    isLoading = true
                                    try {
                                        val nuevoProducto = Producto(
                                            nombre = nombreProducto,
                                            categoria = categoria.ifBlank { "General" },
                                            precio = precioVenta.toDoubleOrNull() ?: 0.0,
                                            variantes = listOf(Variante(talla = "Única", stock = existencias.toIntOrNull() ?: 0))
                                        )
                                        RetrofitClient.instance.registrarOActualizarProducto(nuevoProducto)
                                        message = "Producto agregado correctamente"
                                        nombreProducto = ""; categoria = ""; existencias = ""; precioVenta = ""
                                    } catch (e: Exception) {
                                        message = "Error al guardar producto"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            modifier = Modifier.weight(1f).padding(start = 8.dp),
                            enabled = !isLoading
                        ) {
                            if (isLoading) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                            else Text("Agregar producto", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}