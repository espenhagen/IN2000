package com.example.in2000team5.data_layer.repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.in2000team5.data_layer.datasource.WeatherForecastRemoteDataSource
import com.example.in2000team5.data_layer.datasource.LocationForecast
import com.example.in2000team5.data_layer.datasource.Timeseries
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel
import com.example.in2000team5.utils.GeneralUtils.Companion.round
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
            if(it.isNotEmpty()) weatherDataViewModel.postCurrentWeatherDetails(it[0])
            weatherDataViewModel.postWeatherTimeDetailsList(it)
        }
    }

    private fun getTimeSeries(forecast: LocationForecast): List<WeatherTimeDetails> {
        val finishedWeatherTimeDetailsList = mutableListOf<WeatherTimeDetails>()
        val timeseriesList = forecast.properties?.timeseries
        var startIndex = 0
        if (timeseriesList != null) {
            timeseriesList.forEachIndexed { index, elm ->
                if(MetUtils.isNowTime(elm.time.toString())){
                    startIndex = index

                }
            }

            val croppedTimeseriesList = timeseriesList.subList(startIndex, startIndex+12)
            croppedTimeseriesList.forEach {
                finishedWeatherTimeDetailsList.add(WeatherTimeDetails(it))
            }
            return finishedWeatherTimeDetailsList
        }
        return emptyList()
    }
}

//Class that contains useful data for specific time, is based on Timeseries
// from Api but is simplified and optimised for current usage
class WeatherTimeDetails(timeData: Timeseries?){

    val currentTemperature: MutableState<Double?> = mutableStateOf(getCurrentTemperature(timeData))
    val rainNextHour: MutableState<Double?> = mutableStateOf(getRainNextHour(timeData))
    val precipitationAmount: MutableState<Double?> = mutableStateOf(getPrecipitationAmount(timeData))
    val currentWeatherSymbol: MutableState<String?> = mutableStateOf(getCurrentWeatherSymbol(timeData))
    val windAmount: MutableState<Double?> = mutableStateOf(getWindAmount(timeData))
    val windDirection: MutableState<Double?> = mutableStateOf(getWindDirection(timeData))
    val timeAsString: MutableState<String?> = mutableStateOf(getTime(timeData))

    private fun getCurrentTemperature(timeData: Timeseries?): Double? {
        return timeData?.data?.instant?.details?.air_temperature?.toDouble()
    }

    private fun getRainNextHour(timeData: Timeseries?): Double?{
        return timeData?.data?.next_1_hours?.details?.precipitation_amount?.toDouble()
    }

    private fun getCurrentWeatherSymbol(timeData: Timeseries?): String? {
        return timeData?.data?.next_1_hours?.summary?.symbol_code
    }
    private fun getWindAmount(timeData: Timeseries?): Double? {
        return timeData?.data?.instant?.details?.wind_speed?.toDouble()?.round(1)
    }

    private fun getWindDirection(timeData: Timeseries?): Double? {
        return timeData?.data?.instant?.details?.wind_from_direction?.toDouble()?.round(2)
    }
    private fun getPrecipitationAmount(timeData: Timeseries?): Double? {
        return timeData?.data?.instant?.details?.precipitation_amount?.toDouble()
    }
    private fun getTime(timeData: Timeseries?): String?{
        return timeData?.time
    }
}
