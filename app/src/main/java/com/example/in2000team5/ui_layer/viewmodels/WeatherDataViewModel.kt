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

class WeatherDataViewModel: ViewModel() {

    val liveTemperature = MutableLiveData<Double?>()
    val liveSymbol = MutableLiveData<String?>()
    val liveWindSpeed = MutableLiveData<Double?>()
    val liveWindDirection = MutableLiveData<Double?>()

    var weaterTimes = mutableStateListOf<Timeseries>()



    val weatherRepository = WeatherDataRepository()

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true) // Used to decide when to close splash screen
    val isLoading: State<Boolean> = _isLoading

    fun postWetherObj(data: List<Timeseries>?){
        weaterTimes.addAll(data as Collection<Timeseries>)
    }

    fun fetchWeather(lat: String, lon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.processWeatherData(lat, lon, this@WeatherDataViewModel)
            _isLoading.value = false
        }
    }


    fun getTemperatureLiveData() : MutableLiveData<Double?> {
        return liveTemperature
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