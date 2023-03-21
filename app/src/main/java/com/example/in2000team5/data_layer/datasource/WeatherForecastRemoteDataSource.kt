package com.example.in2000team5.data_layer.datasource

import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitString
import com.google.gson.Gson

// The class WeatherForecastRemoteDataSource fetches data from the MET-api and returns the response.
class WeatherForecastRemoteDataSource {

    suspend fun fetchWeatherNow(lat: String, lon: String): LocationForecast? {
        val gson = Gson()
        val path = "https://in2000-apiproxy.ifi.uio.no/weatherapi/locationforecast/2.0/compact?lat=$lat&lon=$lon"
        return try {
            gson.fromJson(Fuel.get(path).awaitString(), LocationForecast::class.java)
        } catch (exception: Exception) {
            Log.d("Weather not fetched", "A network request exception was thrown in AirQualityDataSource: ${exception.message}")
            null
        }
    }
}

// data classes used to deserialize the response, based on the json-response
data class LocationForecast(val type: String?, val geometry: ForecastGeometry?, val properties: ForecastProperties?)

data class Data(val instant: Instant?, val next_12_hours: Next12Hours?, val next_1_hours: Next1Hours?, val next_6_hours: Next6Hours?)

data class Details(val air_pressure_at_sea_level: Number?, val air_temperature: Number?, val cloud_area_fraction: Number?, val relative_humidity: Number?, val wind_from_direction: Number?, val wind_speed: Number?, val precipitation_amount: Number?)

data class ForecastGeometry(val type: String?, val coordinates: List<Number>?)

data class Instant(val details: Details?)

data class ForecastMeta(val updated_at: String?, val units: Units?)

data class Next12Hours(val summary: Summary?)

data class Next1Hours(val summary: Summary?, val details: Details?)

data class Next6Hours(val summary: Summary?, val details: Details?)

data class ForecastProperties(val meta: ForecastMeta?, val timeseries: List<Timeseries>?)

data class Summary(val symbol_code: String?)

data class Timeseries(val time: String?, val data: Data?)

data class Units(val air_pressure_at_sea_level: String?, val air_temperature: String?, val cloud_area_fraction: String?, val precipitation_amount: String?, val relative_humidity: String?, val wind_from_direction: String?, val wind_speed: String?)
