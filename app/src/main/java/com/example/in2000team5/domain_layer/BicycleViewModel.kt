package com.example.in2000team5.domain_layer

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.in2000team5.data_layer.BicycleRouteRemoteDataSource
import com.example.in2000team5.data_layer.repository.BicycleRouteRepository
import com.example.in2000team5.data_layer.repository.BigBikeRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.in2000team5.data_layer.repository.AirQualityRepository

class BicycleViewModel: ViewModel() {
    private val airQualRepo = AirQualityRepository()
    private val repositoryRoutes = BicycleRouteRepository()
    private val bikeRoutedatasrc = BicycleRouteRemoteDataSource()
    private val bicycleRoutes = SnapshotStateList<SnapshotMutableState<BigBikeRoute>>()

//    private val _bicycleRoutesSharedFlow = MutableSharedFlow<List<BicycleRoute>>()
//    val bicycleRoutesSharedFlow = _bicycleRoutesSharedFlow.asSharedFlow()

    fun getAirQualAvgForRoute(route: MutableState<BigBikeRoute>) {
        // Do an asynchronous operation to fetch users.
        viewModelScope.launch(Dispatchers.IO){

            route.value.AQI.value = airQualRepo.fetchAvgAirQualAtRoute(route.value.fragmentList)
        }
        //if (avgAQI == null) Log.d("Response", "Error getting avg. AQI")
    }

    fun makeApiRequest(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            //repositoryRoutes.constructRoutesThreads(this@BicycleViewModel, context)
            //Log.e("constructRoutesThreads Ferdig", "tommel opp")
            repositoryRoutes.makeBigRoutes(this@BicycleViewModel, context)
            //Log.d("myConstructRoutes Ferdig", "tommel sidelengs")
        }
    }

    fun postRoutes(route: SnapshotMutableState<BigBikeRoute>){
        bicycleRoutes.add(route)
        //bicycleRoutes.add(routes)
    }

    fun getRoutes(): SnapshotStateList<SnapshotMutableState<BigBikeRoute>> {
        return bicycleRoutes
    }

    fun addRouteFromUser(context: Context, start: String, slutt: String) {
        repositoryRoutes.addRouteFromUser(this@BicycleViewModel, context, start, slutt)
    }
}
