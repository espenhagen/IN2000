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
            //if(it.isNotEmpty()) weatherDataViewModel.postCurrentWeatherDetails(it[0])
            //if(it.isNotEmpty()) weatherDataViewModel.postCurrentWeatherDetails(it[0])
            weatherDataViewModel.postWeatherTimeDetailsList(it)
        }
    }
    private fun getTimeSeries(forecast: LocationForecast): TimeSliderData {
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

            return TimeSliderData(finishedWeatherTimeDetailsList)
        }
        return TimeSliderData(emptyList())
    }

    /*
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
    */

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

class TimeSliderData(list: List<WeatherTimeDetails>){

    var timeList = list

    //Values used for support in slider feature
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

    //Makes an update on all values, is usually updated when slider is changing
    fun update(start : Int, end : Int){
        if(timeList.isEmpty())return
        if(timeList.size < end)return

        val subList = timeList.subList(start, end)
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
    }

    fun updateList(list: List<WeatherTimeDetails>){
        timeList = list
    }

    private fun findNumberOfHours(subList: List<WeatherTimeDetails>) : Int {
        return subList.size
    }

    private fun findOutIfRaining(subList: List<WeatherTimeDetails>): Boolean {
        val rain = getMaxRain(subList)
        if(rain > 0.2){
            return true
        }
        return false
    }

    private fun getMaxTemperature(subList: List<WeatherTimeDetails>): Double? {
        return subList.maxOf { it.currentTemperature.value ?: return null}
    }

    private fun getMinTemperature(subList: List<WeatherTimeDetails>): Double {
        return subList.minOf { it.currentTemperature.value ?: return 0.0}
    }

    private fun getAverageTemperature(subList: List<WeatherTimeDetails>): Double? {
        return subList.sumOf { it.currentTemperature.value  ?: return null}.div(subList.size).round(1)
    }

    private fun getMaxRain(subList: List<WeatherTimeDetails>): Double {
        return subList.maxOf { it.rainNextHour.value ?: return 0.0}.round(1)
    }

    private fun getTotalRain(subList: List<WeatherTimeDetails>): Double {
        return subList.sumOf { it.rainNextHour.value ?: return 0.0}.round(1)
    }

    private fun getMaxWind(subList: List<WeatherTimeDetails>): Double? {
        return subList.maxOf { it.windAmount.value ?: return null}.round(1)
    }

    private fun getAverageWind(subList: List<WeatherTimeDetails>): Double? {
        return subList.sumOf { it.windAmount.value ?: return null}.div(subList.size).round(1)
    }

    private fun getWindDirection(subList: List<WeatherTimeDetails>): Double {
        return subList.sumOf { it.windDirection.value ?: return 0.0}.div(subList.size).round(2)
    }

    private fun findOutIfSuncreamIsRecomended(subList: List<WeatherTimeDetails>): Boolean {
        val averageCloudFraction = subList.sumOf { it.precipitationAmount.value ?: return false}.div(subList.size).round(2)
        if(averageCloudFraction < 30 && subList.size > 3 && !findOutIfItCouldBeDark(subList)){
            return true
        }
        return false
    }

    private fun findOutIfItCouldBeDark(subList: List<WeatherTimeDetails>): Boolean {
        subList.forEach {
            if(it.currentWeatherSymbol.value?.contains("night") == true)
                return true
        }
        return false
    }

    private fun findOutIfSlippery(subList: List<WeatherTimeDetails>): Boolean {
        val tempraturAtLowest = getMaxTemperature(subList)
        if(tempraturAtLowest!= null){
            if(tempraturAtLowest <= 4){
                return true
            }
        }
        return false
    }

    fun getSizeOfList(): Int{
        return if(timeList.isEmpty()){
            0
        } else{
            timeList.size - 2
        }
    }

    fun getTimeOfIndex(index: Int): String?{
        if(timeList.isEmpty()) return null
        return timeList[index].timeAsString.value
    }
}
