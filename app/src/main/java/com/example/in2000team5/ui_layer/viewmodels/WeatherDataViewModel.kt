package com.example.in2000team5.ui_layer.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in2000team5.data_layer.datasource.Timeseries
import com.example.in2000team5.data_layer.repository.WeatherDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Viewmodel for weather data. Offers getters and methods to post values.
class WeatherDataViewModel: ViewModel() {

    private val liveTemperature = MutableLiveData<Double?>()
    private val liveSymbol = MutableLiveData<String?>()
    private val liveWindSpeed = MutableLiveData<Double?>()
    private val liveWindDirection = MutableLiveData<Double?>()
    private val weatherRepository = WeatherDataRepository()
    var weatherTimes = mutableStateListOf<Timeseries>()

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
            _isLoading.value = false
        }
    }

    fun getTemperature(): Double? {
        return liveTemperature.value
    }

    fun getSymbolName(): String? {
        return liveSymbol.value
    }

    fun getWindSpeed(): Double?{
        return liveWindSpeed.value
    }

    fun getWindDirection(): Double? {
        return liveWindDirection.value
    }

    // metoder under blir kalt fra WeatherDataRepository for å oppdatere liveDataobjekter
    fun postWeatherObj(data: List<Timeseries>?){
        weatherTimes.addAll(data as Collection<Timeseries>)
        _isLoading.value = false
    }
    fun postTemperature(temp: Double?) {
        liveTemperature.postValue(temp)
    }

    fun postSymbol(symbol: String?) {
        liveSymbol.postValue(symbol)
    }

    fun postWindSpeed(speed: Double?) {
        liveWindSpeed.postValue(speed)
    }

    fun postWindDirection(dir: Double?) {
        liveWindDirection.postValue(dir)
    }

}
