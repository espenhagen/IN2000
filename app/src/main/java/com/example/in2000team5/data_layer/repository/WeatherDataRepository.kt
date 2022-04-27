package com.example.in2000team5.data_layer.repository

import com.example.in2000team5.data_layer.datasource.Details
import com.example.in2000team5.data_layer.datasource.WeatherForecastRemoteDataSource
import com.example.in2000team5.data_layer.datasource.LocationForecast
import com.example.in2000team5.data_layer.datasource.Timeseries
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel
import com.example.in2000team5.utils.MetUtils

/* Fetches weather data from the datasource. Processes the data and makes it ready for the
   viewmodel.
 */
// TODO: endre til å lage objekter av klassene
class WeatherDataRepository {

    private val weatherForecastRemoteDataSource = WeatherForecastRemoteDataSource()
    suspend fun processWeatherData(
        lat: String,
        lon: String,
        weatherDataViewModel: WeatherDataViewModel
    ) {
        val weatherData = weatherForecastRemoteDataSource.fetchWeatherNow(lat, lon) ?: return
        getTimeSeries(weatherData).also {
            weatherDataViewModel.postWeatherObj(it)
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

    private fun getTimeSeries(forecast: LocationForecast): List<Timeseries>? {

        val details = forecast.properties?.timeseries
        var startIndex = 0
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

    private fun getDetails(forecast: LocationForecast): Details? {
        return forecast.properties?.timeseries?.find {
            it.time == MetUtils.getCurrentTimeAsString()
        }?.data?.instant?.details
    }

    private fun getTemperature(forecast: LocationForecast): Double? {
        return getDetails(forecast)?.air_temperature?.toDouble()
    }

    private fun getWindSpeed(forecast: LocationForecast): Double? {
        return getDetails(forecast)?.wind_speed?.toDouble()
    }

    private fun getWindDirection(forecast: LocationForecast): Double? {
        return getDetails(forecast)?.wind_from_direction?.toDouble()
    }

    private fun getWeatherSymbol(forecast: LocationForecast): String? {
        return forecast.properties?.timeseries?.find {
            it.time == MetUtils.getCurrentTimeAsString()
        }?.data?.next_1_hours?.summary?.symbol_code
    }
}