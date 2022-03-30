package com.example.in2000team5.data_layer

import android.content.Context
import android.util.Log
import com.example.in2000team5.domain_layer.BicycleViewModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class BicycleRouteRemoteDataSource {

    suspend fun fetchRoutes(
        context: Context,
        bicycleViewModel: BicycleViewModel,
        repositoryRoutes: BicycleRouteRepository
    ) {
        val url = "https://geoserver.data.oslo.systems/geoserver/bym"
        val path = "/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=bym%3Abyruter&outputFormat=application/json"
        val gson = Gson()

        try {
            val response = gson.fromJson(Fuel.get(url + path).awaitString(), Base::class.java)
            repositoryRoutes.constructRoutesThreads(response.features, context, bicycleViewModel)
        } catch (exception: Exception) {
            Log.d("DATA FETCHING", "A network request exception was thrown: ${exception.message}")
        }
    }
}

// result generated from /json

data class Base(val type: String?, val features: List<Features>?, val totalFeatures: Number?, val numberMatched: Number?, val numberReturned: Number?, val timeStamp: String?, val crs: CRS?)

data class CRS(val type: String?, val properties: Properties?)

data class Features(val type: String?, val id: String?, val geometry: Geometry?, val geometry_name: String?, val properties: Properties?)

data class Geometry(val type: String?, val coordinates: List<List<Number>>?)

data class Properties(val objectid: Number?, val id: Number?, val rute: Number?, val tillegg: String?, val tiltak: Any?, val tid: Any?, val gdb_geomattr_data: Any?)

data class BicycleRoute(
    val id: Number?,
    val routeNr: Number?,
    val coordinates: List<LatLng>?,
    val startDistrict: String?,
    val endDistrict: String?,
    val start: String?,
    val end: String?,
    val distance: Double,
    var AQI: Double?
)
