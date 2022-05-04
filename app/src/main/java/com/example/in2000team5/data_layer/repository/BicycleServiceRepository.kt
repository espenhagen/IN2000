package com.example.in2000team5.data_layer.repository

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotMutableState
import com.example.in2000team5.data_layer.datasource.BicycleServiceLocalDataSource
import com.example.in2000team5.ui_layer.viewmodels.BicycleRouteViewModel
import com.google.android.gms.maps.model.LatLng
import java.io.InputStream

class BicycleServiceRepository {

    private val bicycleServiceLocalDataSource = BicycleServiceLocalDataSource()

    internal suspend fun readServiceStations(inputStream: InputStream, context: BicycleRouteViewModel){
        val stations = bicycleServiceLocalDataSource.readTextFile(inputStream)
        stations.forEach {
            val station = it.split("//")
            val stationPoint = mutableStateOf(LatLng(it[1].toDouble(), it[2].toDouble()))
            Log.d("stationPoint", stationPoint.toString())
            Log.d("station", station.toString())
            context.postServiceStations(stationPoint as SnapshotMutableState<String>)
        }

        Log.d("stasjoner", stations.toString())

    }
}