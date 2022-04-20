package com.example.in2000team5.data_layer.repository

import com.example.in2000team5.data_layer.Details
import com.example.in2000team5.data_layer.ForecastDataSource
import com.example.in2000team5.data_layer.LocFore
import com.example.in2000team5.domain_layer.WeatherDataViewModel
import com.example.in2000team5.utils.metUtils

class WeatherDataRepository {

    val source = ForecastDataSource()
    suspend fun processWeatherData(
        lat: String,
        lon: String,
        weatherDataViewModel: WeatherDataViewModel
    ) {
            val weatherData = source.fetchWeatherNow(lat, lon) ?: return

            getTemperature(weatherData).also {
                weatherDataViewModel.postTemperature(it)
            }

            getWeatherSymbol(weatherData).also {
                weatherDataViewModel.postSymbol(it)
            }

            getWindSpeed(weatherData).also {
                weatherDataViewModel.postWindSpeed(it)
            }

            getWindDirection(weatherData).also {
                weatherDataViewModel.postWindDirection(it)
            }
        }


    fun getDetails(forecast: LocFore): Details? {

        val details = forecast.properties?.timeseries?.find {it.time == metUtils.getCurrentTimeAsString()}?.data?.instant?.details
        return details
    }

    fun getTemperature(forecast: LocFore): Double? {
        return getDetails(forecast)?.air_temperature?.toDouble()
    }

    fun getWindSpeed(forecast: LocFore): Double? {
        return getDetails(forecast)?.wind_speed?.toDouble()
    }

    fun getWindDirection(forecast: LocFore): Double? {
        return getDetails(forecast)?.wind_from_direction?.toDouble()
    }

    fun getWeatherSymbol(forecast: LocFore): String? {
        return forecast.properties?.timeseries?.find {it.time == metUtils.getCurrentTimeAsString()}?.data?.next_1_hours?.summary?.symbol_code
    }


}