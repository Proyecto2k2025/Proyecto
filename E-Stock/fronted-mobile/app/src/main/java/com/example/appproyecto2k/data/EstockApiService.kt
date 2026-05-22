package com.example.appproyecto2k.data

import com.example.appproyecto2k.domain.Producto
import com.example.appproyecto2k.domain.Usuario
import retrofit2.http.*

interface EstockApiService {
    // Autenticación y Perfil
    @POST("api/v1/auth/login")
    suspend fun login(@Body usuario: Usuario): Usuario

    @POST("api/v1/auth/register")
    suspend fun register(@Body usuario: Usuario): Usuario

    @PUT("api/v1/auth/perfil")
    suspend fun actualizarPerfil(@Body usuario: Usuario): Usuario

    // Inventario
    @GET("api/v1/productos")
    suspend fun getProductos(@Query("categoria") categoria: String? = null): List<Producto>

    @POST("api/v1/productos")
    suspend fun registrarOActualizarProducto(@Body producto: Producto): Producto

    @DELETE("api/v1/productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Long)

    // Reportes y Resumen
    @GET("api/v1/pedidos/resumen/{date}")
    suspend fun getResumenDiario(@Path("date") date: String): Map<String, Any>

    @GET("api/v1/reportes/ventas/anual")
    suspend fun getAnnualSales(): List<Map<String, Any>>
}