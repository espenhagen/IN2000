package com.example.in2000team5.data_layer.repository

import com.example.in2000team5.data_layer.datasource.Details
import com.example.in2000team5.data_layer.datasource.WeatherForecastRemoteDataSource
import com.example.in2000team5.data_layer.datasource.LocationForecast
import com.example.in2000team5.data_layer.datasource.Timeseries
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel
import com.example.in2000team5.utils.MetUtils

class WeatherDataRepository {

    val source = WeatherForecastRemoteDataSource()
    suspend fun processWeatherData(
        lat: String,
        lon: String,
        weatherDataViewModel: WeatherDataViewModel
    ) {
            val weatherData = source.fetchWeatherNow(lat, lon) ?: return
            getTimeseries(weatherData).also {
                weatherDataViewModel.postWetherObj(it)
            }


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

    fun getTimeseries(forecast: LocationForecast): List<Timeseries>? {

        val details = forecast.properties?.timeseries
        var startIndex = 0;
        if (details != null) {
            details.forEachIndexed { index, elm ->
                if(MetUtils.isNowTime(elm.time.toString())){
                    startIndex = index
                }
            }
            return details.subList(startIndex, startIndex+24)
        }
        return null
    }

    fun getDetails(forecast: LocationForecast): Details? {

        val details = forecast.properties?.timeseries?.find {it.time == MetUtils.getCurrentTimeAsString()}?.data?.instant?.details
        return details
    }

    fun getTemperature(forecast: LocationForecast): Double? {
        return getDetails(forecast)?.air_temperature?.toDouble()
    }

    fun getWindSpeed(forecast: LocationForecast): Double? {
        return getDetails(forecast)?.wind_speed?.toDouble()
    }

    fun getWindDirection(forecast: LocationForecast): Double? {
        return getDetails(forecast)?.wind_from_direction?.toDouble()
    }

    fun getWeatherSymbol(forecast: LocationForecast): String? {
        return forecast.properties?.timeseries?.find {it.time == MetUtils.getCurrentTimeAsString()}?.data?.next_1_hours?.summary?.symbol_code
    }


}