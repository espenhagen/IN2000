package com.example.in2000team5.ui_layer.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in2000team5.data_layer.repository.TimeSliderData
import com.example.in2000team5.data_layer.repository.WeatherTimeDetails
import com.example.in2000team5.data_layer.repository.WeatherDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Viewmodel for weather data. Offers getters and methods to post values.
class WeatherDataViewModel: ViewModel() {

    private val weatherRepository = WeatherDataRepository()
    val currentWeatherData = mutableStateOf(WeatherTimeDetails(null))
    var weatherTimes = mutableStateOf(TimeSliderData(emptyList()))

    init {
        // Hardkodet til midt i oslo
        fetchWeather("59.91370670", "10.7526291")
    }

    // Pattern to encapsulate the mutable state of the variable
    private val _isLoading: MutableState<Boolean> = mutableStateOf(true) // Used to decide when to close splash screen
    val isLoading: State<Boolean> = _isLoading

    // TODO: ikke private ennå siden vi potensielt tar inn brukerens lokasjon
    fun fetchWeather(lat: String, lon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.processWeatherData(lat, lon, this@WeatherDataViewModel)
        }
    }


    //Method called from weatherRepository to post list of WeatherTimeDetails used in Slider feature
    fun postWeatherTimeDetailsList(d : TimeSliderData){
        weatherTimes = mutableStateOf(d)
        _isLoading.value = false
    }

    //Method called from weatherRepository to post current weather-data
    fun postCurrentWeatherDetails(weatherTimeDetails : WeatherTimeDetails){
        currentWeatherData.value = weatherTimeDetails
    }

}
