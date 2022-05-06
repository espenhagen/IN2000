package com.example.in2000team5.data_layer.repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.in2000team5.data_layer.datasource.Details
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
            weatherDataViewModel.postWeatherObj(it)
        }
        /*

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
        */
    }

    private fun getTimeSeries(forecast: LocationForecast): List<Timeseries> {

        val details = forecast.properties?.timeseries
        var startIndex = 0
        if (details != null) {
            details.forEachIndexed { index, elm ->
                if(MetUtils.isNowTime(elm.time.toString())){
                    startIndex = index
                }
            }
            return details.subList(startIndex, startIndex+12)
        }
        return emptyList()
    }
    /*

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
    */
}
// TODO: refaktorering etter ferdig implementert
class WeatherDetails(list: List<Timeseries>){
    private var completeTimeseries: List<Timeseries>? = list;

    val currentTemperature: MutableState<Double?> = mutableStateOf(getCurrentTemperature(completeTimeseries))
    val rainNextHour: MutableState<Double?> = mutableStateOf(getRainNextHour(completeTimeseries))
    val currentWeatherSymbol: MutableState<String?> = mutableStateOf(getCurrentWeatherSymbol(completeTimeseries))
    val lastIndexOfCompleteTimeseries: MutableState<Int> = mutableStateOf(getLastIndexOfCompleteTimeseries(completeTimeseries))



    //Timeintervall values
    val maxTemperature: MutableState<Double?> = mutableStateOf(null)
    val minTemperature: MutableState<Double> = mutableStateOf(0.0)
    val averageTemperature: MutableState<Double?> = mutableStateOf(null)
    val maxRain: MutableState<Double> = mutableStateOf(0.0)
    val totalRain: MutableState<Double> = mutableStateOf(0.0)
    val maxWind: MutableState<Double?> = mutableStateOf(null)
    val averageWind: MutableState<Double?> = mutableStateOf(null)
    val windDirection: MutableState<Float> = mutableStateOf(0f)
    val isSuncremRecomended: MutableState<Boolean> = mutableStateOf(false)
    val isDark: MutableState<Boolean> = mutableStateOf(false)
    val isSlippery: MutableState<Boolean> = mutableStateOf(false)
    val isRaining: MutableState<Boolean> = mutableStateOf(false)
    val numberOfHours: MutableState<Int> = mutableStateOf(0)

    fun addList(newList: List<Timeseries>){
        completeTimeseries = newList
    }


    fun update(start: Int, end: Int){
        val subList = completeTimeseries?.subList(start, end) as MutableList<Timeseries>
        maxTemperature.value        = getMaxTemperature(subList)
        minTemperature.value        = getMinTemperature(subList)
        averageTemperature.value    = getAverageTemperature(subList)
        maxRain.value               = getMaxRain(subList)
        totalRain.value             = getTotalRain(subList)
        maxWind.value               = getMaxWind(subList)
        averageWind.value           = getAverageWind(subList)
        windDirection.value         = getWindDirection(subList).toFloat()
        isSuncremRecomended.value   = findOutIfSuncreamIsRecomended(subList)
        isDark.value                = findOutIfItCouldBeDark(subList)
        isSlippery.value            = findOutIfSlippery(subList)
        isRaining.value             = findOutIfRaining(subList)
        numberOfHours.value         = findNumberOfHours(subList)

        //lastTi.value         = findNumberOfHours(subList)

    }

    private fun getLastIndexOfCompleteTimeseries(list: List<Timeseries>?): Int{
        if(list.isNullOrEmpty()) return 0;
        return list.lastIndex
    }

    private fun getCurrentTemperature(list: List<Timeseries>?): Double? {
        if(list.isNullOrEmpty()) return null
        return list.first().data?.instant?.details?.air_temperature?.toDouble()

    }

    private fun getRainNextHour(list: List<Timeseries>?): Double?{
        if(list.isNullOrEmpty()) return null
        return list.first().data?.next_1_hours?.details?.precipitation_amount?.toDouble()
    }

    private fun getCurrentWeatherSymbol(list: List<Timeseries>?): String? {
        if(list.isNullOrEmpty()) return null
        return list.first().data?.next_1_hours?.summary?.symbol_code
    }

    private fun findNumberOfHours(subList: MutableList<Timeseries>) : Int {
        return subList.size
    }

    private fun findOutIfRaining(subList: MutableList<Timeseries>): Boolean {
        val rain = getMaxRain(subList)
        if(rain > 0.2){
            return true
        }
        return false
    }

    private fun getMaxTemperature(subList: MutableList<Timeseries>): Double? {
        return subList.maxOf { it.data?.instant?.details?.air_temperature?.toDouble() ?: return null}
    }

    private fun getMinTemperature(subList: MutableList<Timeseries>): Double {
        return subList.minOf { it.data?.instant?.details?.air_temperature?.toDouble() ?: return 0.0}
    }

    private fun getAverageTemperature(subList: MutableList<Timeseries>): Double? {
        return subList.sumOf { it.data?.instant?.details?.air_temperature?.toDouble()  ?: return null}.div(subList.size).round(1)
    }

    private fun getMaxRain(subList: MutableList<Timeseries>): Double {
        return subList.maxOf { it.data?.next_1_hours?.details?.precipitation_amount?.toDouble() ?: return 0.0}.round(1)
    }

    private fun getTotalRain(subList: MutableList<Timeseries>): Double {
        return subList.sumOf { it.data?.next_1_hours?.details?.precipitation_amount?.toDouble() ?: return 0.0}.round(1)
    }

    private fun getMaxWind(subList: MutableList<Timeseries>): Double? {
        return subList.maxOf { it.data?.instant?.details?.wind_speed?.toDouble() ?: return null}.round(1)
    }

    private fun getAverageWind(subList: MutableList<Timeseries>): Double? {
        return subList.sumOf { it.data?.instant?.details?.wind_speed?.toDouble() ?: return null}.div(subList.size).round(1)
    }

    private fun getWindDirection(subList: MutableList<Timeseries>): Double {
        return subList.sumOf { it.data?.instant?.details?.wind_from_direction?.toDouble() ?: return 0.0}.div(subList.size).round(2)
    }

    private fun findOutIfSuncreamIsRecomended(subList: MutableList<Timeseries>): Boolean {
        val averageCloudFraction = subList.sumOf { it.data?.instant?.details?.cloud_area_fraction?.toDouble() ?: return false}.div(subList.size).round(2)
        if(averageCloudFraction < 30 && subList.size > 3 && !findOutIfItCouldBeDark(subList)){
            return true
        }
        return false
    }

    private fun findOutIfItCouldBeDark(subList: MutableList<Timeseries>): Boolean {
        subList.forEach {
            if(it.data?.next_1_hours?.summary?.symbol_code?.contains("night") == true)
                return true
        }
        return false
    }

    private fun findOutIfSlippery(subList: MutableList<Timeseries>): Boolean {
        val tempraturAtLowest = getMaxTemperature(subList)
        if(tempraturAtLowest!= null){
            if(tempraturAtLowest <= 4){
                return true
            }
        }
        return false
    }

    fun getTimeOfIndex(index: Int): String?{
        if(completeTimeseries.isNullOrEmpty()) return null
        return completeTimeseries!![index].time

    }
}