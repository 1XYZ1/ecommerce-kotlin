package com.gymnastic.ecommerceapp.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [CartItem::class, Usuario::class, Direccion::class],
    version = 1,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun direccionDao(): DireccionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getDatabase(context: Context): AppDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "ecommerce_database_v2" // Cambiar nombre para evitar conflictos
                )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries() // Permitir queries en main thread temporalmente
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
