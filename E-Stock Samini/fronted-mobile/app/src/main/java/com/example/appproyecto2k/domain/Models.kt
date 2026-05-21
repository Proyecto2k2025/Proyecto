package com.example.appproyecto2k.domain

data class Usuario(
    val id: Long? = null,
    val nombre: String,
    val password: String? = null,
    val rol: String? = null
)

data class Producto(
    val id: Long? = null,
    val nombre: String,
    val categoria: String,
    val precio: Double,
    val variantes: List<Variante> = emptyList()
)

data class Variante(
    val id: Long? = null,
    val talla: String,
    val stock: Int
)
