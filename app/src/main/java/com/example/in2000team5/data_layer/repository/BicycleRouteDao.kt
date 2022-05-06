package com.example.in2000team5.data_layer.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BicycleRouteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBicycleRoute(bicycleRoute: SimplifiedBicycleRoute)

    @Query("SELECT * FROM simplifiedbicycleroute")
    suspend fun getAll(): List<SimplifiedBicycleRoute>

    @Query("SELECT COUNT(id) FROM simplifiedbicycleroute")
    suspend fun getCount(): Int
}