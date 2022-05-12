package com.example.in2000team5

import android.app.Application
import android.content.Context
import android.location.Geocoder
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.in2000team5.data_layer.repository.BicycleRouteRepository
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BicycleRouteRepositoryTest {

    lateinit var appContext: Context
    lateinit var geocoder: Geocoder

    @Before
    fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().context
        geocoder = Geocoder(appContext)
    }

    @Test
    fun getXXFromOslo() {
        val repo = BicycleRouteRepository(application = Application())
        val result = repo.getCoordinatesFromName(geocoder, "Oslo")

        assertEquals(LatLng(59.91370670, 10.7526291), result)
    }

}