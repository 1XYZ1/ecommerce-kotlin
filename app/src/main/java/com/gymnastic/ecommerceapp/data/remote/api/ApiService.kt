package com.gymnastic.ecommerceapp.data.remote.api

import com.gymnastic.ecommerceapp.data.remote.dto.auth.*
import com.gymnastic.ecommerceapp.data.remote.dto.product.*
import com.gymnastic.ecommerceapp.data.remote.dto.cart.*
import retrofit2.http.*

/**
 * Interfaz de Retrofit que define todos los endpoints de la API
 *
 * La URL base se configura en NetworkModule
 * Base URL: https://pet-shop-back-production.up.railway.app/api/
 */
interface ApiService {

    // ========== AUTENTICACIÓN ==========

    /**
     * POST /auth/register
     * Registra un nuevo usuario
     */
    @POST("auth/register")
    suspend fun registrar(@Body request: RegisterRequest): AuthResponse

    /**
     * POST /auth/login
     * Inicia sesión con credenciales
     */
    @POST("auth/login")
    suspend fun iniciarSesion(@Body request: LoginRequest): AuthResponse

    /**
     * GET /auth/check-status
     * Verifica el estado de autenticación del usuario
     * Requiere token JWT en header
     */
    @GET("auth/check-status")
    suspend fun verificarEstado(): AuthResponse

    // ========== PRODUCTOS ==========

    /**
     * GET /products
     * Obtiene lista de productos con filtros opcionales
     */
    @GET("products")
    suspend fun obtenerProductos(
        @Query("limit") limite: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("q") busqueda: String? = null,
        @Query("type") tipo: String? = null,
        @Query("species") especie: String? = null
    ): ProductListResponse

    /**
     * GET /products/:term
     * Obtiene un producto por ID, título o slug
     */
    @GET("products/{term}")
    suspend fun obtenerProductoPorId(@Path("term") term: String): ProductDto

    /**
     * POST /products
     * Crea un nuevo producto (solo admin)
     * Requiere token JWT en header
     */
    @POST("products")
    suspend fun crearProducto(@Body request: CreateProductRequest): ProductDto

    /**
     * PATCH /products/:id
     * Actualiza un producto existente (solo admin)
     * Requiere token JWT en header
     */
    @PATCH("products/{id}")
    suspend fun actualizarProducto(
        @Path("id") id: String,
        @Body request: com.gymnastic.ecommerceapp.data.remote.dto.product.UpdateProductRequest
    ): ProductDto

    /**
     * DELETE /products/:id
     * Elimina un producto (solo admin)
     * Requiere token JWT en header
     */
    @DELETE("products/{id}")
    suspend fun eliminarProducto(@Path("id") id: String): Unit

    // ========== CARRITO ==========

    /**
     * GET /cart
     * Obtiene el carrito del usuario autenticado
     * Requiere token JWT en header
     */
    @GET("cart")
    suspend fun obtenerCarrito(): CartDto

    /**
     * POST /cart/items
     * Agrega un item al carrito
     * Requiere token JWT en header
     */
    @POST("cart/items")
    suspend fun agregarAlCarrito(@Body request: AddCartItemRequest): CartDto

    /**
     * PATCH /cart/items/:itemId
     * Actualiza la cantidad de un item
     * Requiere token JWT en header
     */
    @PATCH("cart/items/{itemId}")
    suspend fun actualizarCantidad(
        @Path("itemId") itemId: String,
        @Body request: UpdateCartItemRequest
    ): CartDto

    /**
     * DELETE /cart/items/:itemId
     * Elimina un item del carrito
     * Requiere token JWT en header
     */
    @DELETE("cart/items/{itemId}")
    suspend fun eliminarDelCarrito(@Path("itemId") itemId: String): CartDto

    /**
     * DELETE /cart
     * Vacía completamente el carrito
     * Requiere token JWT en header
     */
    @DELETE("cart")
    suspend fun vaciarCarrito(): CartDto

    /**
     * POST /cart/sync
     * Sincroniza items de carrito de invitado
     * Requiere token JWT en header
     */
    @POST("cart/sync")
    suspend fun sincronizarCarrito(@Body request: SyncCartRequest): SyncCartResponse

}
