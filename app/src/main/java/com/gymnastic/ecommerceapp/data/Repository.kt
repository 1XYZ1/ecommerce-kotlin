package com.gymnastic.ecommerceapp.data

import com.gymnastic.ecommerceapp.data.local.AppDb
import com.gymnastic.ecommerceapp.data.local.CartItem
import com.gymnastic.ecommerceapp.data.local.Direccion
import com.gymnastic.ecommerceapp.data.local.ProductEntity
import com.gymnastic.ecommerceapp.data.local.TokenManager
import com.gymnastic.ecommerceapp.data.local.toDomain
import com.gymnastic.ecommerceapp.data.local.toEntity
import com.gymnastic.ecommerceapp.data.remote.datasource.AuthRemoteDataSource
import com.gymnastic.ecommerceapp.data.remote.datasource.CartRemoteDataSource
import com.gymnastic.ecommerceapp.data.remote.datasource.ProductRemoteDataSource
import com.gymnastic.ecommerceapp.data.remote.dto.auth.toDomain
import com.gymnastic.ecommerceapp.data.remote.dto.cart.toLocal
import com.gymnastic.ecommerceapp.data.remote.dto.product.CreateProductRequest
import com.gymnastic.ecommerceapp.domain.Product
import com.gymnastic.ecommerceapp.domain.Result
import com.gymnastic.ecommerceapp.domain.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository para gestionar datos de la aplicación
 *
 * ACTUALIZADO: Ahora maneja tanto datos locales (Room) como remotos (API)
 * - Autenticación: API (AuthRemoteDataSource + TokenManager)
 * - Productos: API con cache en Room (próximamente)
 * - Carrito: Room con sync a API (próximamente)
 * - Direcciones: Room (mantiene como está)
 */
@Singleton
class Repository @Inject constructor(
    private val baseDeDatos: AppDb,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val productRemoteDataSource: ProductRemoteDataSource,
    private val cartRemoteDataSource: CartRemoteDataSource,
    private val tokenManager: TokenManager
) {

    // Acceso directo a los DAOs (Data Access Objects)
    private val daoCarrito = baseDeDatos.cartDao()
    private val daoDireccion = baseDeDatos.direccionDao()
    private val daoProducto = baseDeDatos.productDao()

    // ========== OPERACIONES DE PRODUCTOS ==========

    /**
     * Obtiene todos los productos desde la API
     *
     * Estrategia: API first con cache en Room como fallback
     * 1. Intenta obtener de API
     * 2. Si tiene éxito, guarda en cache y retorna
     * 3. Si falla, intenta leer del cache
     * 4. Si no hay cache, retorna error
     */
    suspend fun obtenerTodosLosProductos(): Result<List<Product>> {
        return when (val resultado = productRemoteDataSource.obtenerProductos()) {
            is Result.Exito -> {
                // Guardar en cache
                val productosDto: List<com.gymnastic.ecommerceapp.data.remote.dto.product.ProductDto> = resultado.datos.products
                val productosDomain: List<Product> = productosDto.map { dto ->
                    Product(
                        id = dto.id,
                        name = dto.title,
                        price = dto.price,
                        description = dto.description ?: "",
                        imageUrl = dto.images.firstOrNull() ?: "",
                        slug = dto.slug,
                        stock = dto.stock,
                        sizes = dto.sizes,
                        type = dto.type,
                        species = dto.species,
                        tags = dto.tags,
                        images = dto.images
                    )
                }
                daoProducto.insertAll(productosDomain.map { it.toEntity() })
                Result.Exito(productosDomain)
            }
            is Result.Error -> {
                // Fallback a cache si la API falla
                val cached: List<ProductEntity> = daoProducto.getAll()
                if (cached.isNotEmpty()) {
                    Result.Exito(cached.map { it.toDomain() })
                } else {
                    resultado // Retornar el error original
                }
            }
            is Result.Cargando -> resultado
        }
    }

    /**
     * Busca un producto por su ID desde la API
     */
    suspend fun obtenerProductoPorId(id: String): Result<Product> {
        return when (val resultado = productRemoteDataSource.obtenerProductoPorId(id)) {
            is Result.Exito -> {
                val dto = resultado.datos
                val productoDomain = Product(
                    id = dto.id,
                    name = dto.title,
                    price = dto.price,
                    description = dto.description ?: "",
                    imageUrl = dto.images.firstOrNull() ?: "",
                    slug = dto.slug,
                    stock = dto.stock,
                    sizes = dto.sizes,
                    type = dto.type,
                    species = dto.species,
                    tags = dto.tags,
                    images = dto.images
                )
                Result.Exito(productoDomain)
            }
            is Result.Error -> {
                // Fallback a cache
                val cached: ProductEntity? = daoProducto.getById(id)
                if (cached != null) {
                    Result.Exito(cached.toDomain())
                } else {
                    resultado
                }
            }
            is Result.Cargando -> resultado
        }
    }

    // ========== OPERACIONES DE ADMIN - PRODUCTOS ==========

    /**
     * Crea un nuevo producto (solo admin)
     */
    suspend fun crearProducto(
        nombre: String,
        descripcion: String,
        precio: Double,
        stock: Int
    ): Result<Unit> {
        val request = CreateProductRequest(
            title = nombre,
            price = precio,
            description = descripcion,
            stock = stock
        )

        return when (val resultado = productRemoteDataSource.crearProducto(request)) {
            is Result.Exito -> Result.Exito(Unit)
            is Result.Error -> resultado
            is Result.Cargando -> resultado
        }
    }

    /**
     * Actualiza un producto existente (solo admin)
     */
    suspend fun actualizarProducto(
        id: String,
        nombre: String,
        descripcion: String,
        precio: Double,
        stock: Int
    ): Result<Unit> {
        val request = com.gymnastic.ecommerceapp.data.remote.dto.product.UpdateProductRequest(
            title = nombre,
            description = descripcion,
            price = precio,
            stock = stock
        )

        return when (val resultado = productRemoteDataSource.actualizarProducto(id, request)) {
            is Result.Exito -> {
                // Actualizar cache también
                daoProducto.clearAll()
                Result.Exito(Unit)
            }
            is Result.Error -> resultado
            is Result.Cargando -> resultado
        }
    }

    /**
     * Elimina un producto (solo admin)
     */
    suspend fun eliminarProducto(id: String): Result<Unit> {
        return when (val resultado = productRemoteDataSource.eliminarProducto(id)) {
            is Result.Exito -> {
                // Limpiar cache
                daoProducto.clearAll()
                Result.Exito(Unit)
            }
            is Result.Error -> resultado
            is Result.Cargando -> resultado
        }
    }

    // ========== OPERACIONES DEL CARRITO ==========

    /**
     * Obtiene todos los items del carrito como Flow
     *
     * ACTUALIZADO: Ahora sincroniza con API si está autenticado
     */
    fun obtenerItemsDelCarrito(): Flow<List<CartItem>> = daoCarrito.getAllCartItems()

    /**
     * Obtiene el carrito actual directamente (sin Flow)
     * Útil para debugging
     */
    suspend fun obtenerCarritoActual(): List<CartItem> {
        val items = daoCarrito.getAllCartItems().firstOrNull() ?: emptyList()
        android.util.Log.d("Repository", "Carrito actual en Room: ${items.size} items")
        items.forEach {
            android.util.Log.d("Repository", "  - ${it.productName} (qty: ${it.quantity}, price: ${it.productPrice})")
        }
        return items
    }

    /**
     * Agrega un producto al carrito con talla y cantidad específicas
     *
     * Estrategia:
     * - Si está autenticado: Agregar via API y actualizar Room con respuesta
     * - Si no está autenticado: Agregar solo en Room local
     *
     * @param producto Producto a agregar
     * @param size Talla seleccionada (debe ser una talla válida del producto)
     * @param quantity Cantidad a agregar (debe ser <= stock)
     */
    suspend fun agregarAlCarrito(producto: Product, size: String, quantity: Int) {
        android.util.Log.d("Repository", "========== AGREGANDO AL CARRITO ==========")
        android.util.Log.d("Repository", "Producto: ${producto.name} (${producto.id})")
        android.util.Log.d("Repository", "Talla seleccionada: $size")
        android.util.Log.d("Repository", "Cantidad: $quantity")
        android.util.Log.d("Repository", "Stock disponible: ${producto.stock}")
        android.util.Log.d("Repository", "Usuario autenticado: ${tokenManager.tieneToken()}")

        if (tokenManager.tieneToken()) {
            // Usuario autenticado: agregar via API
            android.util.Log.d("Repository", "Agregando via API...")
            when (val resultado = cartRemoteDataSource.agregarItem(producto.id, quantity, size)) {
                is Result.Exito -> {
                    android.util.Log.d("Repository", "✓ Agregado en API exitosamente")
                    android.util.Log.d("Repository", "  Carrito ahora tiene ${resultado.datos.items.size} items")

                    // Actualizar Room con todo el carrito del servidor
                    daoCarrito.clearCart()
                    resultado.datos.items.forEach { itemDto ->
                        val itemLocal = itemDto.toLocal()
                        daoCarrito.insertCartItem(itemLocal)
                        android.util.Log.d("Repository", "  ✓ Insertado en Room: ${itemLocal.productName} (qty: ${itemLocal.quantity})")
                    }
                }
                is Result.Error -> {
                    android.util.Log.e("Repository", "✗ Error al agregar en API: ${resultado.mensaje}")
                    android.util.Log.d("Repository", "Agregando solo localmente como fallback...")
                    agregarSoloLocal(producto, quantity)
                }
                else -> {}
            }
        } else {
            // Usuario no autenticado: agregar solo en Room
            android.util.Log.d("Repository", "Agregando solo en Room (usuario no autenticado)...")
            agregarSoloLocal(producto, quantity)
        }
        android.util.Log.d("Repository", "========== FIN AGREGAR AL CARRITO ==========")
    }

    /**
     * Agrega un producto solo en Room (sin API)
     */
    private suspend fun agregarSoloLocal(producto: Product, quantity: Int) {
        val itemExistente = daoCarrito.getCartItem(producto.id)
        if (itemExistente != null) {
            val itemActualizado = itemExistente.copy(quantity = itemExistente.quantity + quantity)
            daoCarrito.updateCartItem(itemActualizado)
            android.util.Log.d("Repository", "  ✓ Cantidad actualizada: ${itemActualizado.quantity}")
        } else {
            val nuevoItem = CartItem(
                productId = producto.id,
                productName = producto.name,
                productPrice = producto.price,
                productImageUrl = producto.imageUrl,
                quantity = quantity
            )
            daoCarrito.insertCartItem(nuevoItem)
            android.util.Log.d("Repository", "  ✓ Nuevo item insertado en Room (qty: $quantity)")
        }
    }

    /**
     * Elimina un producto del carrito con sincronización a API
     *
     * Estrategia:
     * 1. Elimina localmente para feedback inmediato
     * 2. La sincronización completa ocurre en el próximo login/startup
     *
     * Nota: La API requiere itemId (no productId) para eliminar,
     * por lo que la sincronización completa se hace via sincronizarCarritoConServidor()
     */
    suspend fun eliminarDelCarrito(idProducto: String) {
        // Eliminar localmente
        val item = daoCarrito.getCartItem(idProducto)
        item?.let { daoCarrito.deleteCartItem(it) }

        // La sincronización completa con la API se hace automáticamente
        // en login/registro/verificación de estado
    }

    /**
     * Actualiza la cantidad de un producto en el carrito con sincronización
     *
     * Estrategia:
     * 1. Actualiza localmente para feedback inmediato
     * 2. La sincronización completa ocurre en el próximo login/startup
     *
     * Nota: La API requiere itemId (no productId) para actualizar,
     * por lo que la sincronización completa se hace via sincronizarCarritoConServidor()
     */
    suspend fun actualizarCantidadEnCarrito(idProducto: String, cantidad: Int) {
        // Actualizar localmente
        val item = daoCarrito.getCartItem(idProducto)
        item?.let {
            if (cantidad <= 0) {
                daoCarrito.deleteCartItem(it)
            } else {
                daoCarrito.updateCartItem(it.copy(quantity = cantidad))
            }
        }

        // La sincronización completa con la API se hace automáticamente
        // en login/registro/verificación de estado
    }

    /**
     * Vacía completamente el carrito con sincronización
     */
    suspend fun vaciarCarrito() {
        daoCarrito.clearCart()

        // Sincronizar con API si está autenticado
        if (tokenManager.tieneToken()) {
            try {
                cartRemoteDataSource.vaciarCarrito()
            } catch (e: Exception) {
                // Mantener vaciado local
            }
        }
    }

    /**
     * Sincroniza el carrito local con el servidor después del login
     *
     * Llamado automáticamente después de iniciarSesion() o registrar()
     */
    suspend fun sincronizarCarritoConServidor() {
        try {
            android.util.Log.d("Repository", "========== INICIANDO SINCRONIZACIÓN DE CARRITO ==========")

            // Obtener items locales
            val itemsLocales = daoCarrito.getAllCartItems().firstOrNull() ?: emptyList()
            android.util.Log.d("Repository", "Items locales encontrados: ${itemsLocales.size}")
            itemsLocales.forEach {
                android.util.Log.d("Repository", "  - ${it.productName} (qty: ${it.quantity})")
            }

            if (itemsLocales.isEmpty()) {
                android.util.Log.d("Repository", "No hay items locales, obteniendo del servidor...")
                // Si no hay items locales, obtener del servidor
                when (val resultado = cartRemoteDataSource.obtenerCarrito()) {
                    is Result.Exito -> {
                        android.util.Log.d("Repository", "Carrito del servidor recibido:")
                        android.util.Log.d("Repository", "  - Items: ${resultado.datos.items.size}")
                        android.util.Log.d("Repository", "  - Subtotal: ${resultado.datos.subtotal}")
                        android.util.Log.d("Repository", "  - Tax: ${resultado.datos.tax}")
                        android.util.Log.d("Repository", "  - Total: ${resultado.datos.total}")

                        val itemsServidor = resultado.datos.items.map { it.toLocal() }
                        android.util.Log.d("Repository", "Insertando ${itemsServidor.size} items en Room...")
                        itemsServidor.forEach {
                            daoCarrito.insertCartItem(it)
                            android.util.Log.d("Repository", "  ✓ Insertado: ${it.productName}")
                        }
                        android.util.Log.d("Repository", "Items del servidor guardados en Room")
                    }
                    is Result.Error -> {
                        android.util.Log.e("Repository", "Error al obtener carrito: ${resultado.mensaje}")
                    }
                    else -> {}
                }
                android.util.Log.d("Repository", "========== FIN SINCRONIZACIÓN (sin items locales) ==========")
                return
            }

            // Sincronizar items locales al servidor
            android.util.Log.d("Repository", "Sincronizando ${itemsLocales.size} items locales al servidor...")
            when (val resultado = cartRemoteDataSource.sincronizarCarrito(itemsLocales)) {
                is Result.Exito -> {
                    android.util.Log.d("Repository", "Sincronización exitosa!")
                    android.util.Log.d("Repository", "  - Sincronizados: ${resultado.datos.synced}")
                    android.util.Log.d("Repository", "  - Fallidos: ${resultado.datos.failed.size}")
                    resultado.datos.failed.forEach {
                        android.util.Log.w("Repository", "    x ${it.item.productId}: ${it.reason}")
                    }
                    android.util.Log.d("Repository", "  - Carrito final: ${resultado.datos.cart.items.size} items")

                    // Limpiar carrito local
                    daoCarrito.clearCart()
                    android.util.Log.d("Repository", "Carrito local limpiado")

                    // Insertar items mergeados del servidor
                    val itemsMergeados = resultado.datos.cart.items.map { it.toLocal() }
                    android.util.Log.d("Repository", "Insertando ${itemsMergeados.size} items mergeados...")
                    itemsMergeados.forEach {
                        daoCarrito.insertCartItem(it)
                        android.util.Log.d("Repository", "  ✓ Insertado: ${it.productName} (qty: ${it.quantity})")
                    }
                }
                is Result.Error -> {
                    android.util.Log.e("Repository", "Error al sincronizar: ${resultado.mensaje}")
                    android.util.Log.d("Repository", "Manteniendo items locales")
                }
                else -> {}
            }
            android.util.Log.d("Repository", "========== FIN SINCRONIZACIÓN DE CARRITO ==========")
        } catch (e: Exception) {
            android.util.Log.e("Repository", "Excepción al sincronizar carrito", e)
            android.util.Log.d("Repository", "Manteniendo carrito local")
        }
    }

    // ========== OPERACIONES DE AUTENTICACIÓN ==========

    /**
     * Inicia sesión con la API
     *
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Result.Exito con Usuario si funciona, Result.Error si falla
     */
    suspend fun iniciarSesion(email: String, password: String): Result<Usuario> {
        android.util.Log.d("Repository", "iniciarSesion llamado para: $email")

        return when (val resultado = authRemoteDataSource.iniciarSesion(email, password)) {
            is Result.Exito -> {
                android.util.Log.d("Repository", "========== RESULTADO DE DATA SOURCE ==========")
                android.util.Log.d("Repository", "AuthResponse recibido:")
                android.util.Log.d("Repository", "  user.id: ${resultado.datos.user?.id}")
                android.util.Log.d("Repository", "  user.fullName: ${resultado.datos.user?.fullName}")
                android.util.Log.d("Repository", "  user.email: ${resultado.datos.user?.email}")
                android.util.Log.d("Repository", "  user.roles: ${resultado.datos.user?.roles}")
                android.util.Log.d("Repository", "  token: ${resultado.datos.token?.take(20)}...")

                // Verificar que el token no sea null
                val token = resultado.datos.token
                if (token.isNullOrBlank()) {
                    android.util.Log.e("Repository", "ERROR: Token es null o vacío")
                    return Result.Error("Error: La API no devolvió un token válido")
                }

                // Guardar el token JWT
                tokenManager.guardarToken(token)
                android.util.Log.d("Repository", "Token guardado exitosamente")

                // Convertir a Usuario
                val usuario = resultado.datos.toDomain()

                android.util.Log.d("Repository", "Usuario convertido:")
                android.util.Log.d("Repository", "  nombre: ${usuario.nombre}")
                android.util.Log.d("Repository", "  email: ${usuario.email}")
                android.util.Log.d("Repository", "  esAdmin: ${usuario.esAdmin}")
                android.util.Log.d("Repository", "============================================")

                // Sincronizar carrito con el servidor después del login
                sincronizarCarritoConServidor()
                android.util.Log.d("Repository", "Carrito sincronizado después del login")

                // Retornar el usuario convertido a modelo de dominio
                Result.Exito(usuario)
            }
            is Result.Error -> {
                android.util.Log.e("Repository", "Error en iniciarSesion: ${resultado.mensaje}")
                resultado
            }
            is Result.Cargando -> resultado
        }
    }

    /**
     * Registra un nuevo usuario en la API
     *
     * @param nombre Nombre completo del usuario
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return Result.Exito con Usuario si funciona, Result.Error si falla
     */
    suspend fun registrar(
        nombre: String,
        email: String,
        password: String
    ): Result<Usuario> {
        return when (val resultado = authRemoteDataSource.registrar(nombre, email, password)) {
            is Result.Exito -> {
                // Verificar que el token no sea null
                val token = resultado.datos.token
                if (token.isNullOrBlank()) {
                    return Result.Error("Error: La API no devolvió un token válido")
                }

                // Guardar el token JWT
                tokenManager.guardarToken(token)

                // Sincronizar carrito con el servidor después del registro
                sincronizarCarritoConServidor()
                android.util.Log.d("Repository", "Carrito sincronizado después del registro")

                // Retornar el usuario convertido a modelo de dominio
                Result.Exito(resultado.datos.toDomain())
            }
            is Result.Error -> resultado
            is Result.Cargando -> resultado
        }
    }

    /**
     * Cierra sesión eliminando el token
     */
    suspend fun cerrarSesion() {
        tokenManager.eliminarToken()
    }

    /**
     * Verifica si el usuario está autenticado
     *
     * @return true si existe un token guardado
     */
    suspend fun estaAutenticado(): Boolean {
        return tokenManager.tieneToken()
    }

    /**
     * Verifica el estado de autenticación con la API
     *
     * Útil para validar que el token sigue siendo válido
     */
    suspend fun verificarEstadoAuth(): Result<Usuario> {
        return when (val resultado = authRemoteDataSource.verificarEstado()) {
            is Result.Exito -> {
                // Actualizar token si viene uno nuevo
                resultado.datos.token?.let { tokenManager.guardarToken(it) }

                // Sincronizar carrito al restaurar sesión
                sincronizarCarritoConServidor()
                android.util.Log.d("Repository", "Carrito sincronizado al restaurar sesión")

                Result.Exito(resultado.datos.toDomain())
            }
            is Result.Error -> {
                // Si el token expiró, cerrar sesión
                tokenManager.eliminarToken()
                resultado
            }
            is Result.Cargando -> resultado
        }
    }

    // ========== OPERACIONES DE DIRECCIONES ==========

    /**
     * Obtiene todas las direcciones del usuario principal
     */
    fun obtenerDireccionesUsuario(): Flow<List<Direccion>> {
        return daoDireccion.getDireccionesUsuarioPrincipal()
    }

    /**
     * Obtiene la dirección predeterminada del usuario
     */
    suspend fun obtenerDireccionPredeterminada(): Direccion? {
        return daoDireccion.getDireccionPredeterminadaUsuarioPrincipal()
    }

    /**
     * Busca una dirección por su ID
     */
    suspend fun obtenerDireccionPorId(id: String): Direccion? {
        return daoDireccion.getDireccionById(id)
    }

    /**
     * Guarda una nueva dirección
     *
     * Si se marca como predeterminada, quita el flag de las otras direcciones
     * para asegurar que solo haya una dirección predeterminada.
     */
    suspend fun guardarDireccion(direccion: Direccion) {
        if (direccion.esPredeterminada) {
            // Quitar el flag de predeterminada de todas las otras direcciones
            daoDireccion.quitarPredeterminadaDeTodas(direccion.usuarioId)
        }
        daoDireccion.insertDireccion(direccion)
    }

    /**
     * Actualiza una dirección existente
     */
    suspend fun actualizarDireccion(direccion: Direccion) {
        if (direccion.esPredeterminada) {
            // Quitar el flag de predeterminada de todas las otras direcciones
            daoDireccion.quitarPredeterminadaDeTodas(direccion.usuarioId)
        }
        daoDireccion.updateDireccion(direccion)
    }

    /**
     * Elimina una dirección por su ID
     */
    suspend fun eliminarDireccion(idDireccion: String) {
        daoDireccion.deleteDireccionById(idDireccion)
    }

    /**
     * Establece una dirección como predeterminada
     */
    suspend fun establecerDireccionPredeterminada(idDireccion: String) {
        daoDireccion.establecerDireccionPredeterminada("usuario_principal", idDireccion)
    }

    /**
     * Cuenta cuántas direcciones tiene el usuario
     */
    suspend fun contarDireccionesUsuario(): Int {
        return daoDireccion.contarDireccionesUsuario()
    }
}
