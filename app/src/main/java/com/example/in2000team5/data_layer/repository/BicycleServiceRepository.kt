package com.example.in2000team5.data_layer.repository

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotMutableState
import com.example.in2000team5.data_layer.datasource.local.BicycleServiceLocalDataSource
import com.example.in2000team5.data_layer.datasource.local.ServiceStation
import com.example.in2000team5.ui_layer.viewmodels.BicycleRouteViewModel
import com.google.android.gms.maps.model.LatLng
import java.io.InputStream

/**
 * The class fetches all bicycle service stations from the local datasource, converts to actual
 * objects, and posts the objects to the viewmodel.
 */
class BicycleServiceRepository {

    private val bicycleServiceLocalDataSource = BicycleServiceLocalDataSource()

    internal fun readServiceStations(inputStream: InputStream, context: BicycleRouteViewModel){
        val stations = bicycleServiceLocalDataSource.readTextFile(inputStream)
        val delimiters = "//"
        stations.forEach {
            val station = it.split(delimiters)
            if (station.size > 1) {
                val serviceStation = mutableStateOf(ServiceStation(station[0], LatLng(station[1].toDouble(), station[2].toDouble())))
                context.postServiceStations(serviceStation as SnapshotMutableState<ServiceStation>)
            }
        }
    }
}