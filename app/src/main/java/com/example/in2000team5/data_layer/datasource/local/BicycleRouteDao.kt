package com.example.in2000team5.data_layer.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.in2000team5.data_layer.repository.BicycleRoute

// The interface that connects the database with the application, through queries.
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