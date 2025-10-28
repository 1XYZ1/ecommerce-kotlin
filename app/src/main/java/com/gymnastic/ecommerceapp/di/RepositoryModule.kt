package com.gymnastic.ecommerceapp.di

import android.content.Context
import com.gymnastic.ecommerceapp.data.Repository
import com.gymnastic.ecommerceapp.data.local.AppDb
import com.gymnastic.ecommerceapp.data.local.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Dagger Hilt para proporcionar dependencias relacionadas con datos
 *
 * Este módulo se encarga de:
 * - Proporcionar instancias de Repository
 * - Proporcionar instancias de UserPreferences
 * - Configurar el contexto de la aplicación para las dependencias
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

    /**
     * Proporciona una instancia de UserPreferences
     * @param context contexto de la aplicación Android
     * @param database instancia de la base de datos Room
     * @return instancia de UserPreferences configurada
     */
    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context, database: AppDb): UserPreferences {
        return UserPreferences(context, database)
    }
}
