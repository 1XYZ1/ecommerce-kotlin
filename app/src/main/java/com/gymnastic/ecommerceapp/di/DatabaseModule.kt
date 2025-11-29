package com.gymnastic.ecommerceapp.di

import android.content.Context
import androidx.room.Room
import com.gymnastic.ecommerceapp.data.local.AppDb
import com.gymnastic.ecommerceapp.data.local.CartDao
import com.gymnastic.ecommerceapp.data.local.DireccionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para proporcionar dependencias de Room Database
 *
 * ACTUALIZADO: Eliminado UsuarioDao (ahora se usa API para autenticación)
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDb {
        return AppDb.getDatabase(context)
    }

    @Provides
    fun provideCartDao(database: AppDb): CartDao {
        return database.cartDao()
    }

    @Provides
    fun provideDireccionDao(database: AppDb): DireccionDao {
        return database.direccionDao()
    }

    @Provides
    fun provideProductDao(database: AppDb): com.gymnastic.ecommerceapp.data.local.ProductDao {
        return database.productDao()
    }
}
