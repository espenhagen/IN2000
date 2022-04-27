package com.example.in2000team5.data_layer.datasource

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

class AirQualDataSource {
    suspend fun fetchAirQualAtPointDS(lat: String, lon: String): AirQualData?{
        val gson = Gson()
        val path = "https://in2000-apiproxy.ifi.uio.no/weatherapi/airqualityforecast/0.1/?lat=${lat}&lon=${lon}&filter_vars=AQI"

        try {
            return gson.fromJson(Fuel.get(path).awaitString(), AirQualData::class.java)

        } catch (exception: Exception) {
            Log.d("Response", "A network request exception was thrown: ${exception.message}")
            return null
        }
    }
}

//Fra luftkvalitetet api filtrert på AQI index

//result generated from /json
data class AirQualData(val meta: Meta?, val data: AirDataBody?)

data class AQI(val value: Number?, val units: String?)

data class AirDataBody(val time: List<DataOfTime>?)

data class Location(val name: String?, val path: String?, val areacode: String?, val longitude: String?, val latitude: String?, val areaclass: String?, val superareacode: String?)

data class Meta(val reftime: String?, val location: Location?, val superlocation: Location?, val sublocations: List<Any>?)

data class Reason(val variables: List<String>?, val sources: List<String>?)

data class DataOfTime(val from: String?, val to: String?, val variables: Variables?, val reason: Reason?)

data class Variables(val AQI: AQI?)