package com.example.in2000team5.data_layer.repository

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotMutableState
import com.example.in2000team5.data_layer.datasource.BicycleServiceLocalDataSource
import com.example.in2000team5.data_layer.datasource.ServiceStation
import com.example.in2000team5.ui_layer.viewmodels.BicycleRouteViewModel
import com.google.android.gms.maps.model.LatLng
import java.io.InputStream

class BicycleServiceRepository {

    private val bicycleServiceLocalDataSource = BicycleServiceLocalDataSource()

    internal suspend fun readServiceStations(inputStream: InputStream, context: BicycleRouteViewModel){
        val stations = bicycleServiceLocalDataSource.readTextFile(inputStream)
        val delim = "//"
        stations.forEach {
            val station = it.split(delim)
            if (station.size > 1) {
                val serviceStation = mutableStateOf(ServiceStation(station[0], LatLng(station[1].toDouble(), station[2].toDouble())))
                context.postServiceStations(serviceStation as SnapshotMutableState<ServiceStation>)
            }
        }

        Log.d("stasjoner", stations.toString())

    }
}