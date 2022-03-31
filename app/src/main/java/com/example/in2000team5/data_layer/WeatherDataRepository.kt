package com.example.in2000team5.data_layer

import com.example.in2000team5.domain_layer.WeatherDataViewModel
import java.util.*

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
        }


    fun getDetails(forecast: LocFore): Details? {

        val details = forecast.properties?.timeseries?.find {it.time == getCurrentTimeAsString()}?.data?.instant?.details
        return details
    }

    fun getTemperature(forecast: LocFore): Double? {
        return getDetails(forecast)?.air_temperature?.toDouble()
    }

    fun getWindSpeed(forecast: LocFore) {
        val windSpeed = getDetails(forecast)?.wind_speed
    }

    fun getCurrentTimeAsString() : String{

        //paddStart sørger for at verdier får 0 foran seg selvom verdien er under 10
        //Calender.XXXX gir en index hvor verdien finnes i listen

        val year = Calendar.getInstance()[Calendar.YEAR]
        //Verdien fra måender starter på 0 (Januar er 0)
        val month = Calendar.getInstance()[Calendar.MONTH].plus(1).toString().padStart(2, '0')
        val date = Calendar.getInstance()[Calendar.DAY_OF_MONTH].toString().padStart(2, '0')
        val hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY].toString().padStart(2, '0')

        //Standard reftime for luftkvalitets-API
        return  "${year}-${month}-${date}T${hour}:00:00Z"
    }

}