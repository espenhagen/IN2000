package com.example.in2000team5.ui_layer.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in2000team5.data_layer.repository.WeatherTimeDetails
import com.example.in2000team5.data_layer.repository.WeatherDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Viewmodel for weather data. Offers getters and methods to post values.
class WeatherDataViewModel: ViewModel() {
    /*
    private val liveTemperature = MutableLiveData<Double?>()
    private val liveSymbol = MutableLiveData<String?>()
    private val liveWindSpeed = MutableLiveData<Double?>()
    private val liveWindDirection = MutableLiveData<Double?>()
    */
    private val weatherRepository = WeatherDataRepository()
    val currentWeatherData = mutableStateOf(WeatherTimeDetails(null))
    var weatherTimes = mutableListOf<WeatherTimeDetails>()

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


    // metoder under blir kalt fra WeatherDataRepository for å oppdatere liveDataobjekter
    fun postWeatherTimeDetailsList(list : List<WeatherTimeDetails>){
        weatherTimes.addAll(list as Collection<WeatherTimeDetails>)
        _isLoading.value = false
    }

    // metoder under blir kalt fra WeatherDataRepository for å oppdatere liveDataobjekter
    fun postCurrentWeatherDetails(weatherTimeDetails : WeatherTimeDetails){
        currentWeatherData.value = weatherTimeDetails
    }

}
