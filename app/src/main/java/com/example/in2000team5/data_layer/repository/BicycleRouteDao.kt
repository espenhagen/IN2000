package com.example.in2000team5.data_layer.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BicycleRouteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBicycleRoute(bicycleRoute: BicycleRoute)

    @Query("SELECT * FROM bicycleroute")
    suspend fun getAll(): List<BicycleRoute>

    @Query("SELECT COUNT(id) FROM bicycleroute")
    suspend fun getCount(): Int

    @Query("DELETE FROM bicycleroute")
    suspend fun nukeTable()

    @Query("UPDATE bicycleroute SET AQI=:AQI WHERE id=:id")
    suspend fun updateAQI(id: Int, AQI: Double)
}