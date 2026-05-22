package com.example.appproyecto2k.data

import com.example.appproyecto2k.domain.Producto
import com.example.appproyecto2k.domain.Usuario

data class OrderUiState(
    val ventasHoy: String = "€0.0",
    val ventasSemana: String = "€0.0",
    val productosBajoStock: Int = 0,
    val listaProductos: List<Producto> = listOf(),
    val usuarioActual: Usuario? = null,
    val isLoading: Boolean = false
)