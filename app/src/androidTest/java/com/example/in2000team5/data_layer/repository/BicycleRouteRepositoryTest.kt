package com.example.in2000team5.data_layer.repository

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.in2000team5.data_layer.datasource.remote.BicycleRouteRemoteDataSource
import com.example.in2000team5.data_layer.datasource.remote.Features
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BicycleRouteRepositoryTest {

    private val listOfDataPoints = mutableListOf<List<Number>>()

    @Before
    fun fillTestData() {
        listOfDataPoints.addAll(
            listOf(listOf(
                // UTM data points from BYM Byruter API
                599841.5,
                6643669
            ))
        )
    }

    @Test
    fun getOsloGPSfromOsloUTM() {
        val repo = BicycleRouteRepository(application = ApplicationProvider.getApplicationContext())
        val result = repo.constructLatLngList(listOfDataPoints)
        val x = result?.get(0)?.latitude
        val y = result?.get(0)?.longitude

        // X and Y coordinates from coordinates-converter.com
        if (x != null) {
            assertEquals(59.91839, x, 0.00001)
        }
        if (y != null) {
            assertEquals(10.785738, y, 0.000001)
        }
    }

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