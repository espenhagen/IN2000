package com.example.in2000team5.domain_layer

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in2000team5.data_layer.BicycleRoute
import com.example.in2000team5.data_layer.BicycleRouteRemoteDataSource
import com.example.in2000team5.data_layer.BicycleRouteRepository
import com.example.in2000team5.data_layer.Features
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.locationtech.proj4j.CRSFactory
import org.locationtech.proj4j.CoordinateTransformFactory
import org.locationtech.proj4j.ProjCoordinate

class BicycleViewModel: ViewModel() {

    val repositoryRoutes = BicycleRouteRepository()
    private val datasource = BicycleRouteRemoteDataSource()
    val bicycleRoutes = MutableLiveData<List<BicycleRoute>>()

    private fun fetchAirQualForRouteOnAvg(b: BicycleRoute) {
        // Do an asynchronous operation to fetch users.
        viewModelScope.launch(Dispatchers.IO){
            b.coordinates?.let { list ->
                repositoryRoutes.fetchAirQualAtRoute(list).also {
                    b.AQI = it
                }
            }
        }
    }

    fun makeApiRequest(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val bicycleInformation = datasource.fetchRoutes()
            constructRoutesThreads(bicycleInformation, context)
        }
    }

    fun getRoutes(): LiveData<List<BicycleRoute>> {
        return bicycleRoutes
    }

    private fun makeEachRoute(bicycleFeature: Features, context: Context, routes: MutableList<BicycleRoute>) {
        val geocoder = Geocoder(context)
        viewModelScope.launch(Dispatchers.IO) {
            val id = bicycleFeature.properties?.objectid
            val routeNr = bicycleFeature.properties?.rute
            val latLngList = constructLatLngList(bicycleFeature.geometry?.coordinates)

            val firstLatLng = latLngList?.get(0)
            val lastLatLng = latLngList?.get(latLngList.lastIndex)

            val startAddress = getAddress(geocoder, firstLatLng)
            val endAddress = getAddress(geocoder, lastLatLng)

            val startDistrict = constructDistrict(startAddress)
            val endDistrict = constructDistrict(endAddress)
            val start = constructAddress(startAddress)
            val end = constructAddress(endAddress)

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
            val bicycleRoute = BicycleRoute(id, routeNr, latLngList, startDistrict, endDistrict, start, end, total, null)
            fetchAirQualForRouteOnAvg(bicycleRoute)
            routes.add(bicycleRoute)
        }
    }

    private fun constructRoutesThreads(features: List<Features>?, context: Context) {
        val routes = mutableListOf<BicycleRoute>()

        if (features != null) {
            for (bicycleFeature in features) {
                makeEachRoute(bicycleFeature, context, routes)
                bicycleRoutes.postValue(routes)
            }
        }
    }

    private fun getAddress(geocoder: Geocoder, latLng: LatLng?): Address? {
        if (latLng != null) {
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            return address[0]
        }
        return null
    }

    private fun constructAddress(address: Address?): String? {
        if (address != null) {
            if (address.thoroughfare == null) {
                return address.getAddressLine(0)
            }
            return address.thoroughfare
        }
        return null
    }

    private fun constructDistrict(address: Address?): String? {
        if (address != null) {
            return address.subLocality
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
