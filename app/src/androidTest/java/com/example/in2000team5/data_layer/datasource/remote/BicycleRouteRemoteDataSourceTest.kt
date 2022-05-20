package com.example.in2000team5.data_layer.datasource.remote

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class BicycleRouteRemoteDataSourceTest {

    @Test
    fun fetchRoutesTest() {
        //Integration test of the fetchRoutes()- method and 'Oslo kommune sykkelruter API'.
        runBlocking {

            //Given
            val bicycleRouteRemoteDataSource = BicycleRouteRemoteDataSource()

            //When
            val ruter: List<Features> = bicycleRouteRemoteDataSource.fetchRoutes()!!
            val rute: Features = ruter.get(0)
            val ruteId: Number? = rute.properties?.rute?.toInt()

            //Then
            assertEquals("Rutenummer skal være 4",4,ruteId)
            assertNotEquals("Sjekker at rutenr ikke er 5",5,ruteId)
        }
    }
}
