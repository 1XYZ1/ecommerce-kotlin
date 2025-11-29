package com.gymnastic.ecommerceapp.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [CartItem::class, Direccion::class, ProductEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun direccionDao(): DireccionDao
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getDatabase(context: Context): AppDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "ecommerce_database_v3" // Nueva versión sin Usuario en Room
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
