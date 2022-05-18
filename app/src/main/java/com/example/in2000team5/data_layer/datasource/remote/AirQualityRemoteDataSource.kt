package com.example.in2000team5.data_layer.datasource.remote

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

// The class AirQualityRemoteDataSource fetches data from the MET-api and returns the response.
class AirQualityRemoteDataSource {
    suspend fun fetchAirQualityAtPointDataSource(lat: String, lon: String): AirQualData?{
        val gson = Gson()
        // The request is filtered on the AQI index
        val path = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?lat=${lat}&lon=${lon}&filter_vars=AQI"

        return try {
            gson.fromJson(Fuel.get(path).awaitString(), AirQualData::class.java)

        } catch (exception: Exception) {
            Log.d("Response", "A network request exception was thrown in AirQualityDataSource: ${exception.message}")
            null
        }
    }
}

// data classes used to deserialize the response, based on the json-response
data class AirQualData(val meta: Meta?, val data: AirDataBody?)

data class AQI(val value: Number?, val units: String?)

data class AirDataBody(val time: List<DataOfTime>?)

data class Location(val name: String?, val path: String?, val areacode: String?, val longitude: String?, val latitude: String?, val areaclass: String?, val superareacode: String?)

data class Meta(val reftime: String?, val location: Location?, val superlocation: Location?, val sublocations: List<Any>?)

data class Reason(val variables: List<String>?, val sources: List<String>?)

data class DataOfTime(val from: String?, val to: String?, val variables: Variables?, val reason: Reason?)

data class Variables(val AQI: AQI?)