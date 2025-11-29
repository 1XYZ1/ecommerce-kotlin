package com.gymnastic.ecommerceapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gymnastic.ecommerceapp.data.remote.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Módulo de Hilt para proporcionar dependencias de red
 *
 * Configura Retrofit, OkHttp y todos los servicios de API
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * URL base de la API en producción
     */
    private const val BASE_URL = "https://pet-shop-back-production.up.railway.app/api/"

    /**
     * Proporciona una instancia de Gson configurada
     */
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    /**
     * Proporciona el interceptor de logging para debug
     *
     * Muestra en Logcat todas las peticiones y respuestas HTTP
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    /**
     * Proporciona OkHttpClient configurado con AuthInterceptor
     *
     * El AuthInterceptor agrega automáticamente el token JWT a todas las peticiones
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: com.gymnastic.ecommerceapp.data.remote.api.AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Proporciona instancia de Retrofit
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * Proporciona el servicio de API principal
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
