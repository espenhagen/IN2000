package com.example.in2000team5.domain_layer

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in2000team5.data_layer.BicycleRoute
import com.example.in2000team5.data_layer.BicycleRouteRemoteDataSource
import com.example.in2000team5.data_layer.BicycleRouteRepository
import com.example.in2000team5.data_layer.BigBikeRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import com.example.in2000team5.data_layer.repository.AirQualityRepository

class BicycleViewModel: ViewModel() {
    private val airQualRepo = AirQualityRepository()
    private val repositoryRoutes = BicycleRouteRepository()
    private val bikeRoutedatasrc = BicycleRouteRemoteDataSource()
    private val bicycleRoutes = MutableLiveData<List<BigBikeRoute>>()
    private val routes = mutableStateListOf<BigBikeRoute>()

    private val _bicycleRoutesSharedFlow = MutableSharedFlow<List<BicycleRoute>>()
    val bicycleRoutesSharedFlow = _bicycleRoutesSharedFlow.asSharedFlow()

    fun getAirQualAvgForRoute(route: BigBikeRoute) {
        // Do an asynchronous operation to fetch users.
        viewModelScope.launch(Dispatchers.IO){
                route.AQI = airQualRepo.fetchAvgAirQualAtRoute(route.fragmentList)
        }
        //if (avgAQI == null) Log.d("Response", "Error getting avg. AQI")
    }

    fun makeApiRequest(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            //repositoryRoutes.constructRoutesThreads(this@BicycleViewModel, context)
            //Log.e("constructRoutesThreads Ferdig", "tommel opp")
            repositoryRoutes.makeBigRoutes(this@BicycleViewModel, context)
            Log.e("myConstructRoutes Ferdig", "tommel sidelengs")
        }
    }

    fun postRoutes(route: BigBikeRoute){
        routes.add(route)
        bicycleRoutes.postValue(routes)
    }

    fun getRoutes(): SnapshotStateList<BigBikeRoute> {
        return routes
    }
}
