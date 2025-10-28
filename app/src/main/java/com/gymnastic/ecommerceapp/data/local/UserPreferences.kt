package com.gymnastic.ecommerceapp.data.local

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Clase para manejar las preferencias del usuario usando Room Database
 *
 * Migrada de SharedPreferences a Room Database para mejor gestión de datos.
 * Mantiene la misma interfaz pública para no romper código existente.
 */
@Singleton
class UserPreferences @Inject constructor(
    private val contexto: Context,
    private val database: AppDb
) {

    private val usuarioDao = database.usuarioDao()
    private val direccionDao = database.direccionDao()

    /**
     * Verifica si el usuario está logueado
     */
    suspend fun isLoggedIn(): Boolean {
        return try {
            val usuario = usuarioDao.getUsuarioPrincipal()
            usuario?.estaLogueado ?: false
        } catch (e: Exception) {
            false // En caso de error, asumir que no está logueado
        }
    }

    /**
     * Registra un nuevo usuario
     * @param name nombre del usuario
     * @param email email del usuario
     * @param password contraseña del usuario
     * @return true si el registro fue exitoso, false si el email ya existe
     */
    suspend fun registerUser(name: String, email: String, password: String): Boolean {
        val usuarioExistente = usuarioDao.getUsuarioPrincipal()
        if (usuarioExistente != null) return false

        val nuevoUsuario = Usuario(
            id = "usuario_principal",
            nombre = name,
            email = email,
            password = password,
            estaLogueado = true
        )
        usuarioDao.insertUsuario(nuevoUsuario)
        return true
    }

    /**
     * Realiza el login del usuario
     * @param email email del usuario
     * @param password contraseña del usuario
     * @return true si el login fue exitoso, false en caso contrario
     */
    suspend fun loginUser(email: String, password: String): Boolean {
        val usuario = usuarioDao.getUsuarioPrincipal()

        if (usuario != null && usuario.email == email && usuario.password == password) {
            usuarioDao.updateLoginStatus(true)
            return true
        }
        return false
    }

    /**
     * Cierra la sesión del usuario
     */
    suspend fun logout() {
        usuarioDao.updateLoginStatus(false)
    }

    /**
     * Obtiene toda la información del usuario
     */
    suspend fun getUserInfo(): UserInfo {
        return try {
            val usuario = usuarioDao.getUsuarioPrincipal()
            if (usuario != null) {
                UserInfo(
                    name = usuario.nombre,
                    email = usuario.email,
                    isLoggedIn = usuario.estaLogueado
                )
            } else {
                UserInfo("", "", false)
            }
        } catch (e: Exception) {
            UserInfo("", "", false) // En caso de error, retornar valores por defecto
        }
    }

    /**
     * Obtiene la información del usuario como Flow para observación reactiva
     */
    fun getUserInfoFlow(): Flow<UserInfo> {
        return usuarioDao.getUsuarioPrincipalFlow().map { usuario ->
            if (usuario != null) {
                UserInfo(
                    name = usuario.nombre,
                    email = usuario.email,
                    isLoggedIn = usuario.estaLogueado
                )
            } else {
                UserInfo("", "", false)
            }
        }
    }

    // ========== MÉTODOS PARA DIRECCIONES ==========

    /**
     * Obtiene todas las direcciones guardadas del usuario
     */
    fun getDireccionesGuardadas(): Flow<List<Direccion>> {
        return direccionDao.getDireccionesUsuarioPrincipal()
    }

    /**
     * Obtiene la dirección predeterminada del usuario
     */
    suspend fun getDireccionPredeterminada(): Direccion? {
        return direccionDao.getDireccionPredeterminadaUsuarioPrincipal()
    }

    /**
     * Guarda una nueva dirección
     */
    suspend fun guardarDireccion(
        nombreCompleto: String,
        telefono: String,
        direccionCompleta: String,
        esPredeterminada: Boolean = false
    ): String {
        val nuevaDireccion = Direccion(
            usuarioId = "usuario_principal",
            nombreCompleto = nombreCompleto,
            telefono = telefono,
            direccionCompleta = direccionCompleta,
            esPredeterminada = esPredeterminada
        )

        // Si se marca como predeterminada, quitar el flag de las otras direcciones
        if (esPredeterminada) {
            direccionDao.quitarPredeterminadaDeTodas("usuario_principal")
        }

        direccionDao.insertDireccion(nuevaDireccion)
        return nuevaDireccion.id
    }

    /**
     * Actualiza una dirección existente
     */
    suspend fun actualizarDireccion(direccion: Direccion) {
        direccionDao.updateDireccion(direccion)
    }

    /**
     * Elimina una dirección
     */
    suspend fun eliminarDireccion(direccionId: String) {
        direccionDao.deleteDireccionById(direccionId)
    }

    /**
     * Establece una dirección como predeterminada
     */
    suspend fun establecerDireccionPredeterminada(direccionId: String) {
        direccionDao.establecerDireccionPredeterminada("usuario_principal", direccionId)
    }
}

/**
 * Clase de datos para representar la información del usuario
 * @param name nombre del usuario
 * @param email email del usuario
 * @param isLoggedIn estado de autenticación
 */
data class UserInfo(
    val name: String,
    val email: String,
    val isLoggedIn: Boolean
)
