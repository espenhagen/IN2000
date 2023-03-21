package com.example.in2000team5.data_layer.datasource

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

// The class fetches the bicycle routes from the API from Oslo kommune
class BicycleRouteRemoteDataSource {

    suspend fun fetchRoutes(): List<Features>? {
        val url = "https://geoserver.data.oslo.systems/geoserver/bym"
        val path = "/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=bym%3Abyruter&outputFormat=application/json"
        val gson = Gson()

        return try {
            val response = gson.fromJson(Fuel.get(url + path).awaitString(), Base::class.java)
            response.features
        } catch (exception: Exception) {
            Log.d("DATA FETCHING", "A network request exception was thrown in BicycleRouteRemoteDataSource: ${exception.message}")
            null
        }
    }
}

// data classes used to deserialize the response, based on the json-response
data class Base(val type: String?, val features: List<Features>?, val totalFeatures: Number?, val numberMatched: Number?, val numberReturned: Number?, val timeStamp: String?, val crs: CRS?)

data class CRS(val type: String?, val properties: Properties?)

data class Features(val type: String?, val id: String?, val geometry: Geometry?, val geometry_name: String?, val properties: Properties?)

data class Geometry(val type: String?, val coordinates: List<List<Number>>?)

data class Properties(val objectid: Number?, val id: Number?, val rute: Number?, val tillegg: String?, val tiltak: Any?, val tid: Any?, val gdb_geomattr_data: Any?)

