package com.example.in2000team5.ui_layer.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in2000team5.data_layer.repository.WeatherTimesData
import com.example.in2000team5.data_layer.repository.WeatherHourDetails
import com.example.in2000team5.data_layer.repository.WeatherDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Viewmodel for weather data. Offers getters and methods to post values.
class WeatherDataViewModel: ViewModel() {

    private val weatherRepository = WeatherDataRepository()
    // Hardcoded to the middle of Oslo
    private val osloLatitude = "59.91370670"
    private val osloLongitude = "10.7526291"
    var weatherTimes = mutableStateOf(WeatherTimesData(emptyList()))

    init {
        fetchWeather()
    }

    private fun fetchWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.processWeatherData(osloLatitude, osloLongitude, this@WeatherDataViewModel)
        }
    }

    //Method called from weatherRepository to post list of WeatherTimeDetails used in Slider feature
    fun postWeatherTimeDetailsList(weatherHourDetailsList : List<WeatherHourDetails>){
        weatherTimes.value.updateList(weatherHourDetailsList)
    }
}
