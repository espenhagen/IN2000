package com.example.in2000team5.data_layer.repository

import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.in2000team5.data_layer.datasource.remote.BicycleRouteRemoteDataSource
import com.example.in2000team5.data_layer.datasource.remote.Features
import com.example.in2000team5.data_layer.datasource.local.AppDatabase
import com.example.in2000team5.data_layer.datasource.local.BicycleRouteDao
import com.example.in2000team5.ui_layer.viewmodels.BicycleInformationViewModel
import com.example.in2000team5.utils.GeneralUtils.Companion.isInternetAvailable
import com.example.in2000team5.utils.RouteUtils.Companion.routeNames
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.locationtech.proj4j.CRSFactory
import org.locationtech.proj4j.CoordinateTransformFactory
import org.locationtech.proj4j.ProjCoordinate

/* This repository offers a methods to create the bigger routes, consisting of the smaller routes
   received from the API-request from the datasource. It also has functionality to parse coordinates
   and create routes, based on user input. The repository also connects with the database.
   The database stores the routes created from the users, in the entity being a simplified
   bicycle route.
 */
class BicycleRouteRepository(application: Application) {

    private val bicycleRouteRemoteDataSource = BicycleRouteRemoteDataSource()
    private val bigRouteMap: HashMap<Int, MutableList<List<LatLng>?>> = HashMap()
    private var existingRoutes = routeNames().size
    private var numOfRoutesInDatabase = 0
    private var bicycleRouteDao: BicycleRouteDao

    init {
        val database = AppDatabase.getDatabase(application)
        bicycleRouteDao = database.bicycleRouteDao()
        runBlocking {
            launch {
                numOfRoutesInDatabase = bicycleRouteDao.getCount()
            }
        }
    }

    suspend fun insertBicycleRoute(bicycleRoute: BicycleRoute) {
        bicycleRouteDao.insertBicycleRoute(bicycleRoute)
    }

    // Maps the smaller routes to larger routes, with the route-number being the joining variable.
    suspend fun makeBigRoutes(bicycleInformationViewModel: BicycleInformationViewModel) {
        bicycleRouteRemoteDataSource.fetchRoutes()?.forEach {
            addCords(it)
        }

        // Now I have a HashMap with a list of all the smaller routes
        val routeNames = routeNames()
        bigRouteMap.forEach {

            val bigBikeRoute = mutableStateOf(
                BicycleRoute(
                it.key,
                it.value,
                routeNames[it.key]?.get(0)!!,
                routeNames[it.key]?.get(1)!!,
                calculateRouteLength(it.value), mutableStateOf(null)
                )
            )
            bicycleInformationViewModel.getAirQualityAvgForRoute(bigBikeRoute)
            bicycleInformationViewModel.postRoutes(bigBikeRoute as SnapshotMutableState<BicycleRoute>)
        }
    }

    private fun addCords(bicycleFeature: Features) {
        val latLngList = constructLatLngList(bicycleFeature.geometry?.coordinates)
        val id: Int = if (bicycleFeature.properties?.rute == null) {
            0
        } else {
            bicycleFeature.properties.rute.toInt()
        }

        if (!bigRouteMap.containsKey(id)) {
            bigRouteMap[id] = mutableListOf(latLngList)
        } else {
            bigRouteMap[id]?.add(latLngList)
        }
    }

    // Calculates the length of the route fragments
    private fun latLngListLength(latLngList: List<LatLng>): Double {
        var total = 0.0
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
        return total
    }

    // Aggregates length of route fragments
    private fun calculateRouteLength(routeList: MutableList<List<LatLng>?>) : Double {
        return routeList.fold(0.0) { total, next -> total + latLngListLength(next!!) }
    }

    // Creates the LatLng-list based on the utmList
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun constructLatLngList(utmList: List<List<Number>>?): List<LatLng>? {
        if (utmList == null) return null

        val routes = mutableListOf<LatLng>()

        // Setting up transforming coordinates
        val ctFactory = CoordinateTransformFactory()
        val csFactory = CRSFactory()
        val csName1 = "EPSG:25832" // UTM
        val csName2 = "EPSG:4326"   // WSG84
        val crs1 = csFactory.createFromName(csName1)
        val crs2 = csFactory.createFromName(csName2)
        val trans = ctFactory.createTransform(crs1, crs2) // Creating transform from UTM to WSG84

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

    private fun getCoordinatesFromName(geocoder: Geocoder, name: String): LatLng? {
        if (name.isEmpty()) return null
        val response = geocoder.getFromLocationName(name, 1)
        if (response.size < 1) return null
        if (response[0].hasLatitude() && response[0].hasLongitude()) {
            return LatLng(response[0].latitude, response[0].longitude)
        }
        return null
    }

    // Reverses the order of the latitude and longitude based on the format from the geocoder
    private fun userInputRouteLength(latLngList: List<LatLng>): Double {
        var total = 0.0
        for (i in 1 until latLngList.size) {
            val lengthResult = FloatArray(1)
            Location.distanceBetween(
                latLngList[i - 1].longitude,
                latLngList[i - 1].latitude,
                latLngList[i].longitude,
                latLngList[i].latitude,
                lengthResult
            )
            total += lengthResult[0]
        }
        return total
    }

    /* Takes inn start and end, and creates a bicycleroutes based on these values, and posts it to
       the viewmodel.
     */
    fun addRouteFromUser(bicycleInformationViewModel: BicycleInformationViewModel, context: Context, start: String, end: String): Boolean {
        if (! isInternetAvailable(context)) return false

        val geocoder = Geocoder(context)
        val startLatLng = getCoordinatesFromName(geocoder, start)
        val endLatLng = getCoordinatesFromName(geocoder, end)

        val latLngList: MutableList<List<LatLng>?>
        var isAdded = false

        if (startLatLng != null && endLatLng != null) {
            val startUpper = start[0].uppercase() + start.subSequence(1, start.length)
            val endUpper = end[0].uppercase() + end.subSequence(1, end.length)
            latLngList = mutableListOf(listOf(startLatLng, endLatLng))
            val length = userInputRouteLength(latLngList[0]!!)
            val totalRoutes = ++numOfRoutesInDatabase + existingRoutes
            val nyRute = mutableStateOf(BicycleRoute(
                totalRoutes,
                latLngList,
                startUpper,
                endUpper,
                length,
                mutableStateOf(null)
            ))

            bicycleInformationViewModel.getAirQualityAvgForRoute(nyRute)
            bicycleInformationViewModel.postRoutes(nyRute as SnapshotMutableState<BicycleRoute>)
            bicycleInformationViewModel.insertBicycleRoute(nyRute)

            isAdded = true
        }
        return isAdded
    }

    fun updateAQI(id: Int, AQI: Double) {
        runBlocking {
            launch {
                bicycleRouteDao.updateAQI(id, AQI)
            }
        }
    }

    // Adds all the routes from the database, into the bicycle route list used in the viewmodel
    fun addRoutesFromDatabase(bicycleRoutes: SnapshotStateList<SnapshotMutableState<BicycleRoute>>) {
        runBlocking {
            launch {
                for (bicycleRoute in bicycleRouteDao.getAll()) {
                    bicycleRoutes.add(mutableStateOf(bicycleRoute) as SnapshotMutableState<BicycleRoute>)
                }
            }
        }
    }
}

// Model-class for the bicycle routes.
@Entity
data class BicycleRoute (
    @PrimaryKey val id: Int,
    val fragmentList: MutableList<List<LatLng>?>,
    val start: String,
    val end: String,
    val length: Double,
    var AQI: MutableState<Double?>
)

