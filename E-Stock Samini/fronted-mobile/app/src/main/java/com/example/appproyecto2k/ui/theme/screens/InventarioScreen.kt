package com.example.appproyecto2k.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.appproyecto2k.data.RetrofitClient
import com.example.appproyecto2k.domain.Producto
import kotlinx.coroutines.launch

@Composable
fun InventarioScreen() {
    var productos by remember { mutableStateOf<List<Producto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    // Cargar datos al iniciar
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                isLoading = true
                productos = RetrofitClient.instance.getProductos()
            } catch (e: Exception) {
                errorMessage = "Error al cargar inventario: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Encabezado de la tabla
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF004D71))
                .padding(8.dp)
        ) {
            val headers = listOf("S.NO", "PROD. ID", "NOMBRE", "TALLA", "STOCK")
            headers.forEach { text ->
                Text(
                    text = text,
                    modifier = Modifier.weight(1f),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF004D71))
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage!!, color = Color.Red)
            }
        } else {

            val rows = productos.flatMap { prod ->
                prod.variantes.map { variante ->
                    Triple(prod.id, prod.nombre, variante)
                }
            }

            LazyColumn {
                itemsIndexed(rows) { index, item ->
                    val (prodId, prodNombre, variante) = item
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (index % 2 == 0) Color.White else Color(0xFFE3F2FD))
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(String.format("%02d", index + 1), Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                        Text(prodId.toString(), Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                        Text(prodNombre, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                        Text(variante.talla, Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                        Text(variante.stock.toString(), Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
