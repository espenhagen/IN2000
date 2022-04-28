package com.example.in2000team5.ui_layer.viewmodels

import android.content.Context
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
class BicycleRouteViewModel: ViewModel() {
    private val airQualityRepository = AirQualityRepository(airQualityDataSource = AirQualityRemoteDataSource())
    private val bicycleRouteRepository = BicycleRouteRepository()
    private val bicycleRoutes = SnapshotStateList<SnapshotMutableState<BicycleRoute>>()
    private val _isLoading: MutableState<Boolean> = mutableStateOf(true) // Used to decide when to close splash screen
    val isLoading: State<Boolean> = _isLoading

    init {
        makeApiRequest()
    }

    // Uses thread to calculate AQI-index for bicycle route asynchronously
    fun getAirQualityAvgForRoute(route: MutableState<BicycleRoute>) {
        viewModelScope.launch(Dispatchers.IO){
            route.value.AQI.value = airQualityRepository.fetchAvgAirQualityAtRoute(route.value.fragmentList)
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
        getAirQualityAvgForRoute(route)
        //bicycleRoutes.add(routes)
    }

    fun getRoutes(): SnapshotStateList<SnapshotMutableState<BicycleRoute>> {
        return bicycleRoutes
    }

    fun addRouteFromUser(context: Context, start: String, slutt: String): Boolean {
        return bicycleRouteRepository.addRouteFromUser(this@BicycleRouteViewModel, context, start, slutt)
    }
}

