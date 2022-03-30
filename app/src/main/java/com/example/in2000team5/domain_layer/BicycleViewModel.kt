package com.example.in2000team5.domain_layer

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in2000team5.data_layer.BicycleRoute
import com.example.in2000team5.data_layer.BicycleRouteRemoteDataSource
import com.example.in2000team5.data_layer.BicycleRouteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BicycleViewModel: ViewModel() {

    val repositoryRoutes = BicycleRouteRepository()
    private val bikeRoutedatasrc = BicycleRouteRemoteDataSource()
    private val bicycleRoutes = MutableLiveData<List<BicycleRoute>>()
    private val routes = mutableListOf<BicycleRoute>()

    fun fetchAirQualForRouteOnAvg(route: BicycleRoute) {
        // Do an asynchronous operation to fetch users.
        viewModelScope.launch(Dispatchers.IO){
            route.coordinates?.let { list ->
                repositoryRoutes.fetchAirQualAtRoute(list).also {
                    route.AQI = it
                }
            }
        }
    }

    fun makeApiRequest(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            bikeRoutedatasrc.fetchRoutes(context, this@BicycleViewModel, repositoryRoutes)
        }
    }

    fun postRoutes(route: BicycleRoute){
        routes.add(route)
        bicycleRoutes.postValue(routes)
    }

    fun getRoutes(): LiveData<List<BicycleRoute>> {
        return bicycleRoutes
    }

}
