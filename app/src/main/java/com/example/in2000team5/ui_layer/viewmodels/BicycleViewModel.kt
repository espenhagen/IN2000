package com.example.in2000team5.ui_layer.viewmodels

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.*
import com.example.in2000team5.data_layer.datasource.BicycleRouteRemoteDataSource
import com.example.in2000team5.data_layer.repository.BicycleRouteRepository
import com.example.in2000team5.data_layer.repository.BicycleRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.in2000team5.data_layer.repository.AirQualityRepository

class BicycleViewModel: ViewModel() {
    private val airQualRepo = AirQualityRepository()
    private val repositoryRoutes = BicycleRouteRepository()
    private val bikeRoutedatasrc = BicycleRouteRemoteDataSource()
    private val bicycleRoutes = SnapshotStateList<SnapshotMutableState<BicycleRoute>>()
    private val _isLoading: MutableState<Boolean> = mutableStateOf(true) // Used to decide when to close splash screen
    val isLoading: State<Boolean> = _isLoading

//    private val _bicycleRoutesSharedFlow = MutableSharedFlow<List<BicycleRoute>>()
//    val bicycleRoutesSharedFlow = _bicycleRoutesSharedFlow.asSharedFlow()

    fun getAirQualAvgForRoute(route: MutableState<BicycleRoute>) {
        // Do an asynchronous operation to fetch users.
        viewModelScope.launch(Dispatchers.IO){

            route.value.AQI.value = airQualRepo.fetchAvgAirQualityAtRoute(route.value.fragmentList)
        }
        //if (avgAQI == null) Log.d("Response", "Error getting avg. AQI")
    }

    fun makeApiRequest(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            //repositoryRoutes.constructRoutesThreads(this@BicycleViewModel, context)
            //Log.e("constructRoutesThreads Ferdig", "tommel opp")
            repositoryRoutes.makeBigRoutes(this@BicycleViewModel)
            //Log.d("myConstructRoutes Ferdig", "tommel sidelengs")
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
        return repositoryRoutes.addRouteFromUser(this@BicycleViewModel, context, start, slutt)
    }
}
