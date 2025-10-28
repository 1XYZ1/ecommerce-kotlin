package com.gymnastic.ecommerceapp.di

import android.content.Context
import com.gymnastic.ecommerceapp.data.Repository
import com.gymnastic.ecommerceapp.data.local.AppDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Dagger Hilt simplificado para proporcionar dependencias relacionadas con datos
 *
 * Este módulo se encarga de:
 * - Proporcionar instancias de Repository
 * - Configurar el contexto de la aplicación para las dependencias
 *
 * NOTA: UserPreferences fue eliminado durante la simplificación.
 * Su funcionalidad ahora está directamente en AuthViewModel.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Proporciona una instancia de Repository
     * @param database instancia de la base de datos Room
     * @return instancia de Repository configurada
     */
    @Provides
    @Singleton
    fun provideRepository(database: AppDb): Repository {
        return Repository(database)
    }
}
