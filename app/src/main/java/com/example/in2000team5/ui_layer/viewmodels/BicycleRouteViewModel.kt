package com.example.in2000team5.ui_layer.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.*
import com.example.in2000team5.data_layer.datasource.AirQualityRemoteDataSource
import com.example.in2000team5.data_layer.repository.BicycleRouteRepository
import com.example.in2000team5.data_layer.repository.BicycleRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.in2000team5.data_layer.repository.AirQualityRepository

// Viewmodel for bicycle route data. Offers getters and methods to post values.
class BicycleRouteViewModel(appObj: Application): AndroidViewModel(appObj) {
    private val airQualityRepository = AirQualityRepository(airQualityDataSource = AirQualityRemoteDataSource())
    private val bicycleRouteRepository = BicycleRouteRepository(appObj)
    private val bicycleRoutes = SnapshotStateList<SnapshotMutableState<BicycleRoute>>()
    private val _isLoading: MutableState<Boolean> = mutableStateOf(true) // Used to decide when to close splash screen
    val isLoading: State<Boolean> = _isLoading

    init {
        makeApiRequest()
        bicycleRouteRepository.addRoutesFromDatabase(bicycleRoutes)
    }

    // Uses thread to calculate AQI-index for bicycle route asynchronously
    fun getAirQualityAvgForRoute(route: MutableState<BicycleRoute>) {
        viewModelScope.launch(Dispatchers.IO){
            route.value.AQI.value = airQualityRepository.fetchAvgAirQualityAtRoute(route.value.fragmentList)
            bicycleRouteRepository.updateAQI(route.value.id, route.value.AQI.value?:-1.0)
        }
    }

    private fun makeApiRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            bicycleRouteRepository.makeBigRoutes(this@BicycleRouteViewModel)
            _isLoading.value = false
        }
    }

    fun postRoutes(route: SnapshotMutableState<BicycleRoute>){
        bicycleRoutes.add(route)
        //bicycleRoutes.add(routes)
    }

    fun getRoutes(): SnapshotStateList<SnapshotMutableState<BicycleRoute>> {
        return bicycleRoutes
    }

    fun addRouteFromUser(context: Context, start: String, slutt: String): Boolean {
        val result = bicycleRouteRepository.addRouteFromUser(
            this@BicycleRouteViewModel, context, start, slutt)

        val text = if (result) "Rute lagt til" else "Oppgi gyldig start og slutt"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(context, text, duration)
        toast.show()

        return result
    }

    fun insertBicycleRoute(bicycleRoute: MutableState<BicycleRoute>) {
        viewModelScope.launch {
            bicycleRouteRepository.insertBicycleRoute(bicycleRoute.value)
        }
    }
}
