package com.gymnastic.ecommerceapp.di

import android.content.Context
import com.gymnastic.ecommerceapp.data.Repository
import com.gymnastic.ecommerceapp.data.local.AppDb
import com.gymnastic.ecommerceapp.data.local.TokenManager
import com.gymnastic.ecommerceapp.data.remote.api.ApiService
import com.gymnastic.ecommerceapp.data.remote.datasource.AuthRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para proporcionar dependencias relacionadas con datos
 *
 * ACTUALIZADO: Ahora incluye dependencias para API y autenticación
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Proporciona AuthRemoteDataSource
     */
    @Provides
    @Singleton
    fun provideAuthRemoteDataSource(apiService: ApiService): AuthRemoteDataSource {
        return AuthRemoteDataSource(apiService)
    }

    /**
     * Proporciona ProductRemoteDataSource
     */
    @Provides
    @Singleton
    fun provideProductRemoteDataSource(apiService: ApiService): com.gymnastic.ecommerceapp.data.remote.datasource.ProductRemoteDataSource {
        return com.gymnastic.ecommerceapp.data.remote.datasource.ProductRemoteDataSource(apiService)
    }

    /**
     * Proporciona CartRemoteDataSource
     */
    @Provides
    @Singleton
    fun provideCartRemoteDataSource(apiService: ApiService): com.gymnastic.ecommerceapp.data.remote.datasource.CartRemoteDataSource {
        return com.gymnastic.ecommerceapp.data.remote.datasource.CartRemoteDataSource(apiService)
    }

    /**
     * Proporciona una instancia de Repository con todas sus dependencias
     */
    @Provides
    @Singleton
    fun provideRepository(
        database: AppDb,
        authRemoteDataSource: AuthRemoteDataSource,
        productRemoteDataSource: com.gymnastic.ecommerceapp.data.remote.datasource.ProductRemoteDataSource,
        cartRemoteDataSource: com.gymnastic.ecommerceapp.data.remote.datasource.CartRemoteDataSource,
        tokenManager: TokenManager
    ): Repository {
        return Repository(database, authRemoteDataSource, productRemoteDataSource, cartRemoteDataSource, tokenManager)
    }
}
