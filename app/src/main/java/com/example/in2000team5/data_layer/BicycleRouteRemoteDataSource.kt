package com.example.in2000team5.data_layer

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson

class BicycleRouteRemoteDataSource {

    suspend fun fetchRoutes(): List<Features>? {
        val url = "https://geoserver.data.oslo.systems/geoserver/bym"
        val path = "/ows?service=WFS&version=1.0.0&request=GetFeature&typeName=bym%3Abyruter&outputFormat=application/json"
        val gson = Gson()

         try {
            val response = gson.fromJson(Fuel.get(url + path).awaitString(), Base::class.java)
            return response.features
        } catch (exception: Exception) {
            Log.d("DATA FETCHING", "A network request exception was thrown: ${exception.message}")
            return null
        }
    }
}

// result generated from /json

data class Base(val type: String?, val features: List<Features>?, val totalFeatures: Number?, val numberMatched: Number?, val numberReturned: Number?, val timeStamp: String?, val crs: CRS?)

data class CRS(val type: String?, val properties: Properties?)

data class Features(val type: String?, val id: String?, val geometry: Geometry?, val geometry_name: String?, val properties: Properties?)

data class Geometry(val type: String?, val coordinates: List<List<Number>>?)

data class Properties(val objectid: Number?, val id: Number?, val rute: Number?, val tillegg: String?, val tiltak: Any?, val tid: Any?, val gdb_geomattr_data: Any?)

