package com.example.in2000team5.domain_layer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in2000team5.data_layer.repository.WeatherDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class
WeatherDataViewModel: ViewModel() {

    val liveTemperature = MutableLiveData<Double?>()
    val liveSymbol = MutableLiveData<String?>()
    val liveWindSpeed = MutableLiveData<Double?>()
    val liveWindDirection = MutableLiveData<Double?>()

    val weatherRepository = WeatherDataRepository()


    fun fetchWeather(lat: String, lon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.processWeatherData(lat, lon, this@WeatherDataViewModel)
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