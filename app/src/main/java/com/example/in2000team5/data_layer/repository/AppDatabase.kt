package com.example.in2000team5.data_layer.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SimplifiedBicycleRoute::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun bicycleRouteDao(): BicycleRouteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java, "jetpack")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}