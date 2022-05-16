package com.example.in2000team5

import android.app.Application
import android.content.Context
import android.location.Geocoder
import androidx.room.Database
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.in2000team5.data_layer.repository.AppDatabase
import com.example.in2000team5.data_layer.repository.BicycleRouteRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BicycleRouteRepositoryTest {

    lateinit var appContext: Context
    lateinit var geocoder: Geocoder
    lateinit var testDB: AppDatabase
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

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().context
        geocoder = Geocoder(appContext)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getXXFromOslo() {
        val repo = BicycleRouteRepository(application = ApplicationProvider.getApplicationContext())
        runTest {
            val result = repo.getCoordinatesFromName(geocoder, "Oslo")

            assertEquals(LatLng(59.91370670, 10.7526291), result)
        }
    }

}