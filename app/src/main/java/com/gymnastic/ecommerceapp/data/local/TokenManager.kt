package com.gymnastic.ecommerceapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Extension para crear DataStore de tokens
 */
private val Context.tokenDataStore by preferencesDataStore(name = "auth_tokens")

/**
 * Gestor de tokens JWT
 *
 * Almacena y recupera el token de autenticación usando DataStore.
 * DataStore es la alternativa moderna a SharedPreferences.
 *
 * CONCEPTOS PARA ESTUDIANTES:
 * - DataStore es asíncrono y type-safe
 * - Usa Kotlin Coroutines (suspend functions)
 * - Más seguro que SharedPreferences
 */
@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.tokenDataStore

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    /**
     * Guarda el token JWT en DataStore
     *
     * @param token El token JWT recibido de la API
     */
    suspend fun guardarToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    /**
     * Obtiene el token JWT guardado
     *
     * @return El token si existe, null si no hay token guardado
     */
    suspend fun obtenerToken(): String? {
        return dataStore.data
            .map { preferences -> preferences[TOKEN_KEY] }
            .first()
    }

    /**
     * Elimina el token JWT (logout)
     */
    suspend fun eliminarToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    /**
     * Verifica si existe un token guardado
     *
     * @return true si hay un token, false si no
     */
    suspend fun tieneToken(): Boolean {
        return obtenerToken() != null
    }
}
