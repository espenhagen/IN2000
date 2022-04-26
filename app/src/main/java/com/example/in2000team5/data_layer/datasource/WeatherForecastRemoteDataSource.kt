package com.example.in2000team5.data_layer

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

class ForecastDataSource {

    val gson = Gson()
    suspend fun fetchWeatherNow(lat: String, lon: String): LocFore? {
        try {
            val path = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/2.0/compact?lat=$lat&lon=$lon"
            val result = Fuel.get(path).awaitString()
            val respons = gson.fromJson(result, LocFore::class.java)
            Log.d("result", result)
            Log.d("respons", respons.toString())
            return respons
        } catch (exception: Exception) {
            Log.d("weather ikke fetched", exception.message.toString())
            return null
        }
    }
}

//result generated from /json
data class LocFore(val type: String?, val geometry: ForecastGeometry?, val properties: ForecastProperties?)

data class Data(val instant: Instant?, val next_12_hours: Next_12_hours?, val next_1_hours: Next_1_hours?, val next_6_hours: Next_6_hours?)

data class Details(val air_pressure_at_sea_level: Number?, val air_temperature: Number?, val cloud_area_fraction: Number?, val relative_humidity: Number?, val wind_from_direction: Number?, val wind_speed: Number?, val precipitation_amount: Number?)

data class ForecastGeometry(val type: String?, val coordinates: List<Number>?)

data class Instant(val details: Details?)

data class ForecastMeta(val updated_at: String?, val units: Units?)

data class Next_12_hours(val summary: Summary?)

data class Next_1_hours(val summary: Summary?, val details: Details?)

data class Next_6_hours(val summary: Summary?, val details: Details?)

data class ForecastProperties(val meta: ForecastMeta?, val timeseries: List<Timeseries>?)

data class Summary(val symbol_code: String?)

data class Timeseries(val time: String?, val data: Data?)

data class Units(val air_pressure_at_sea_level: String?, val air_temperature: String?, val cloud_area_fraction: String?, val precipitation_amount: String?, val relative_humidity: String?, val wind_from_direction: String?, val wind_speed: String?)
