package com.example.in2000team5.domain_layer

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in2000team5.data_layer.BicycleRoute
import com.example.in2000team5.data_layer.BicycleRouteRemoteDataSource
import com.example.in2000team5.data_layer.BicycleRouteRepository
import com.example.in2000team5.data_layer.BigBikeRoute
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class BicycleViewModel: ViewModel() {

    private val repositoryRoutes = BicycleRouteRepository()
    private val bikeRoutedatasrc = BicycleRouteRemoteDataSource()
    private val bicycleRoutes = MutableLiveData<List<BigBikeRoute>>()
    private val routes = mutableListOf<BigBikeRoute>()

    private val _bicycleRoutesSharedFlow = MutableSharedFlow<List<BicycleRoute>>()
    val bicycleRoutesSharedFlow = _bicycleRoutesSharedFlow.asSharedFlow()

    fun getAirQualAvgForRoute(route: BigBikeRoute) {
        //var avgAQI: Double? = null
        // Do an asynchronous operation to fetch users.

        viewModelScope.launch(Dispatchers.IO){
                route.AQI = repositoryRoutes.fetchAvgAirQualAtRoute(route.fragmentList)
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

    fun getRoutes(): LiveData<List<BigBikeRoute>> {
        return bicycleRoutes
    }
}
