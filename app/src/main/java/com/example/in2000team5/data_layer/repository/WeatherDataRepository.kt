package com.example.in2000team5.data_layer.repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.in2000team5.data_layer.datasource.remote.WeatherForecastRemoteDataSource
import com.example.in2000team5.data_layer.datasource.remote.LocationForecast
import com.example.in2000team5.data_layer.datasource.remote.Timeseries
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel
import com.example.in2000team5.utils.GeneralUtils.Companion.round
import com.example.in2000team5.utils.MetUtils

/* Fetches weather data from the datasource. Processes the data and makes it ready for the
   viewmodel.
 */
class WeatherDataRepository {

    private val weatherForecastRemoteDataSource = WeatherForecastRemoteDataSource()
    suspend fun processWeatherData(
        lat: String,
        lon: String,
        weatherDataViewModel: WeatherDataViewModel
    ) {
        val weatherData = weatherForecastRemoteDataSource.fetchWeatherNow(lat, lon) ?: return

        getTimeSeries(weatherData).also {
            weatherDataViewModel.postWeatherTimeDetailsList(it)
        }
    }

    private fun getTimeSeries(forecast: LocationForecast): List<WeatherHourDetails> {
        val finishedWeatherHourDetailsList = mutableListOf<WeatherHourDetails>()
        val timeseriesList = forecast.properties?.timeseries

        //Find current time in timeseries to crop the list
        var startIndex = 0
        if (timeseriesList != null) {
            timeseriesList.forEachIndexed { index, elm ->
                if(MetUtils.isNowTime(elm.time.toString())){
                    startIndex = index
                }
            }

            //Size of list that is exspectet to be used in the application
            val croppedTimeseriesList = timeseriesList.subList(startIndex, startIndex+12)
            croppedTimeseriesList.forEach {
                finishedWeatherHourDetailsList.add(WeatherHourDetails(it))
            }

            return finishedWeatherHourDetailsList
        }
        return emptyList()
    }
}

//Class that contains useful data for specific time, is based on Timeseries
// from Api but is simplified and optimised for current usage
class WeatherHourDetails(timeData: Timeseries?){

    val temperature = getCurrentTemperature(timeData)
    val rainNextHour = getRainNextHour(timeData)
    val precipitationAmount= getPrecipitationAmount(timeData)
    val weatherSymbol = getWeatherSymbol(timeData)
    val windAmount = getWindAmount(timeData)
    val windDirection = getWindDirection(timeData)
    val timeAsString = getTime(timeData)

    private fun getCurrentTemperature(timeData: Timeseries?): Double? {
        return timeData?.data?.instant?.details?.air_temperature?.toDouble()
    }
    private fun getRainNextHour(timeData: Timeseries?): Double?{
        return timeData?.data?.next_1_hours?.details?.precipitation_amount?.toDouble()
    }
    private fun getWeatherSymbol(timeData: Timeseries?): String? {
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

//Class that contains all nesesary data for weatherinformation in the application
class WeatherTimesData(list: List<WeatherHourDetails>){

    private var weatherHourList = list

    // Values based on the current time
    private var current = weatherHourList.firstOrNull()
    val currentTemperature = mutableStateOf(current?.temperature ?: 0.0)
    val currentRainNextHour = mutableStateOf(current?.rainNextHour ?: 0.0)
    val currentWeatherSymbol = mutableStateOf(current?.weatherSymbol)

    // Values used for support in slider feature
    val maxTemperature: MutableState<Double?> = mutableStateOf(0.0)
    val minTemperature: MutableState<Double> = mutableStateOf(0.0)
    val averageTemperature: MutableState<Double?> = mutableStateOf(0.0)
    val maxRain: MutableState<Double> = mutableStateOf(0.0)
    val totalRain: MutableState<Double> = mutableStateOf(0.0)
    val maxWind: MutableState<Double?> = mutableStateOf(0.0)
    val averageWind: MutableState<Double?> = mutableStateOf(0.0)
    val windDirection: MutableState<Float> = mutableStateOf(0f)
    val isSuncreenRecommended: MutableState<Boolean> = mutableStateOf(false)
    val isDark: MutableState<Boolean> = mutableStateOf(false)
    val isSlippery: MutableState<Boolean> = mutableStateOf(false)
    val isRaining: MutableState<Boolean> = mutableStateOf(false)
    val numberOfHours: MutableState<Int> = mutableStateOf(0)

    //Makes an update for the values depending on the slider
    fun updateSliderData(start : Int, end : Int){
        if(weatherHourList.isEmpty())return
        if(weatherHourList.size < end)return

        val subList = weatherHourList.subList(start, end)
        maxTemperature.value        = getMaxTemperature(subList)
        minTemperature.value        = getMinTemperature(subList)
        averageTemperature.value    = getAverageTemperature(subList)
        maxRain.value               = getMaxRain(subList)
        totalRain.value             = getTotalRain(subList)
        maxWind.value               = getMaxWind(subList)
        averageWind.value           = getAverageWind(subList)
        windDirection.value         = getWindDirection(subList).toFloat()
        isSuncreenRecommended.value   = findOutIfSuncreamIsRecomended(subList)
        isDark.value                = findOutIfItCouldBeDark(subList)
        isSlippery.value            = findOutIfSlippery(subList)
        isRaining.value             = findOutIfRaining(subList)
        numberOfHours.value         = findNumberOfHours(subList)
    }

    fun updateList(list: List<WeatherHourDetails>){
        weatherHourList = list

        //Data for current time
        current = weatherHourList.firstOrNull()
        currentTemperature .value =  current?.temperature ?: 0.0
        currentRainNextHour.value =  current?.rainNextHour ?: 0.0
        currentWeatherSymbol.value = current?.weatherSymbol

        updateSliderData(0, 1)
    }

    private fun findNumberOfHours(subList: List<WeatherHourDetails>) : Int {
        return subList.size
    }

    private fun findOutIfRaining(subList: List<WeatherHourDetails>): Boolean {
        val rain = getMaxRain(subList)
        if(rain > 0.2){
            return true
        }
        return false
    }

    private fun getMaxTemperature(subList: List<WeatherHourDetails>): Double? {
        return subList.maxOf { it.temperature ?: return null}
    }

    private fun getMinTemperature(subList: List<WeatherHourDetails>): Double {
        return subList.minOf { it.temperature ?: return 0.0}
    }

    private fun getAverageTemperature(subList: List<WeatherHourDetails>): Double? {
        return subList.sumOf { it.temperature  ?: return null}.div(subList.size).round(1)
    }

    private fun getMaxRain(subList: List<WeatherHourDetails>): Double {
        return subList.maxOf { it.rainNextHour ?: return 0.0}.round(1)
    }

    private fun getTotalRain(subList: List<WeatherHourDetails>): Double {
        return subList.sumOf { it.rainNextHour ?: return 0.0}.round(1)
    }

    private fun getMaxWind(subList: List<WeatherHourDetails>): Double? {
        return subList.maxOf { it.windAmount ?: return null}.round(1)
    }

    private fun getAverageWind(subList: List<WeatherHourDetails>): Double? {
        return subList.sumOf { it.windAmount ?: return null}.div(subList.size).round(1)
    }

    private fun getWindDirection(subList: List<WeatherHourDetails>): Double {
        return subList.sumOf { it.windDirection ?: return 0.0}.div(subList.size).round(2)
    }

    private fun findOutIfSuncreamIsRecomended(subList: List<WeatherHourDetails>): Boolean {
        val averageCloudFraction = subList.sumOf { it.precipitationAmount ?: return false}.div(subList.size).round(2)
        if(averageCloudFraction < 30 && subList.size > 3 && !findOutIfItCouldBeDark(subList)){
            return true
        }
        return false
    }

    private fun findOutIfItCouldBeDark(subList: List<WeatherHourDetails>): Boolean {
        subList.forEach {
            if(it.weatherSymbol?.contains("night") == true)
                return true
        }
        return false
    }

    private fun findOutIfSlippery(subList: List<WeatherHourDetails>): Boolean {
        val temperatureAtLowest = getMaxTemperature(subList)
        if(temperatureAtLowest!= null){
            if(temperatureAtLowest <= 4){
                return true
            }
        }
        return false
    }

    fun getSizeOfList(): Int{
        return if(weatherHourList.isEmpty()){
            0
        } else{
            weatherHourList.size - 2
        }
    }

    fun getTimeOfIndex(index: Int): String?{
        if(weatherHourList.isEmpty()) return null
        return weatherHourList[index].timeAsString
    }
}
