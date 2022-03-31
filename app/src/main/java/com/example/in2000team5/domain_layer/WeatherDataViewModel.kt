package com.example.in2000team5.domain_layer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in2000team5.data_layer.LocFore
import com.example.in2000team5.data_layer.WeatherDataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class WeatherDataViewModel: ViewModel() {

    val liveTemperature = MutableLiveData<Double?>()
    val weatherRepository = WeatherDataRepository()


    fun fetchWeather(lat: String, lon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.processWeatherData(lat, lon, this@WeatherDataViewModel)
        }
    }

    fun getTemperature() : MutableLiveData<Double?> {
        return liveTemperature
    }

    fun postTemperature(temp: Double?) {
        liveTemperature.postValue(temp)
    }


}