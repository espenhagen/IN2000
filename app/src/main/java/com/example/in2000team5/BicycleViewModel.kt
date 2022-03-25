package com.example.in2000team5

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.locationtech.proj4j.CRSFactory
import org.locationtech.proj4j.CoordinateTransformFactory
import org.locationtech.proj4j.ProjCoordinate

class BicycleViewModel: ViewModel() {
    private val datasource = DataSource()
    val bicycleRoutes = MutableLiveData<List<BicycleRoute>>()

    fun makeApiRequest(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val bicycleInformation = datasource.fetchRoutes()
            constructRoutes(bicycleInformation, context)
        }
    }

    fun getRoutes(): LiveData<List<BicycleRoute>> {
        return bicycleRoutes
    }

    private fun constructRoutes(features: List<Features>?, context: Context) {
        val routes = mutableListOf<BicycleRoute>()
        val geocoder = Geocoder(context)

        if (features != null) {
            for (bicycleFeature in features) {

                val id = bicycleFeature.properties?.objectid
                val routeNr = bicycleFeature.properties?.rute
                val latLngList = constructLatLngList(bicycleFeature.geometry?.coordinates)

                val firstLatLng = latLngList?.get(0)
                val lastLatLng = latLngList?.get(latLngList.lastIndex)

                val startDistrict = constructDistrict(geocoder, firstLatLng)
                val endDistrict = constructDistrict(geocoder, lastLatLng)
                val start = constructAddress(geocoder, firstLatLng)
                val end = constructAddress(geocoder, lastLatLng)

                var total = 0.0
                if (latLngList != null) {
                    for (i in 1 until latLngList.size) {
                        val lengthResult = FloatArray(1)
                        Location.distanceBetween(
                            latLngList[i - 1].latitude,
                            latLngList[i - 1].longitude,
                            latLngList[i].latitude,
                            latLngList[i].longitude,
                            lengthResult
                        )
                        total += lengthResult[0]
                    }
                }
                val bicycleRoute = BicycleRoute(id, routeNr, latLngList, startDistrict, endDistrict, start, end, total)
                Log.d("Testing create routes: ", bicycleRoute.start + " to " + bicycleRoute.end)
                //Log.d("LATLNG list: ", latLngList.toString())
                routes.add(bicycleRoute)
                // To make the view update only one time, put the next line outside the loop
                bicycleRoutes.postValue(routes)
            }
        }
    }

    private fun constructAddress(geocoder: Geocoder, latLng: LatLng?): String? {
        if (latLng != null) {
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            //Log.d("Test creating address", address.toString())
            // fallbacks if the requests are null
            if (address.size > 0) {
                if (address[0].thoroughfare == null) {
                    return address[0].getAddressLine(0)
                }
                return address[0].getAddressLine(0)
            }
        }
        return null
    }

    private fun constructDistrict(geocoder: Geocoder, latLng: LatLng?): String? {
        if (latLng != null) {
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            //Log.d("Address geocoding", address.toString())
            if (address.size > 0)
                return address[0].subLocality
        }
        return null
    }

    private fun constructLatLngList(utmList: List<List<Number>>?): List<LatLng>? {
        if (utmList == null) return null

        val routes = mutableListOf<LatLng>()

        // Setting up transforming coordinates
        val ctFactory = CoordinateTransformFactory()
        val csFactory = CRSFactory()
        val csName1 = "EPSG:25832" // UTM
        val csName2 = "EPSG:4326"   // WSG84
        val crs1 = csFactory.createFromName(csName1)
        val crs2 = csFactory.createFromName(csName2)
        val trans = ctFactory.createTransform(crs1, crs2)   // Creating transform from UTM to WSG84

        // Input and output points.
        // Constructed once per thread and then reused.
        val point1 = ProjCoordinate()
        val point2 = ProjCoordinate()

        for (i in utmList.indices) {
            point1.x = utmList[i].component1().toDouble()
            point1.y = utmList[i].component2().toDouble()
            // Transform point
            trans.transform(point1, point2)
            // Adding list of transformed coordinates to the route
            routes.add(LatLng(point2.y, point2.x))
        }
        return routes
    }
}
