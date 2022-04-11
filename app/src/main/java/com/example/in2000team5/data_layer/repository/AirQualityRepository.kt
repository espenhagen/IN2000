package com.example.in2000team5.data_layer.repository

import android.util.Log
import com.example.in2000team5.data_layer.AirQualData
import com.example.in2000team5.data_layer.AirQualDataSource
import com.example.in2000team5.utils.metUtils
import com.google.android.gms.maps.model.LatLng
import com.example.in2000team5.utils.genUtils.Companion.round


class AirQualityRepository {
    private val airQualDataSource = AirQualDataSource()


    fun getRealtimeAQI(aqiDataobj: AirQualData?): Double? {

        val data =
            aqiDataobj?.data?.time?.find { it.from.equals(metUtils.getCurrentTimeAsString()) }

        return data?.variables?.AQI?.value?.toDouble()

    }


    suspend fun fetchAvgAirQualAtRoute(routeList: MutableList<List<LatLng>?>): Double? {
        var tot = 0.0
        var sampledPoints = 0
        val MIN_NUM_OF_SAMPLEPOINTS = 10

        val numberOfFragments = routeList.size

        if (numberOfFragments >= MIN_NUM_OF_SAMPLEPOINTS) {
            for (frag in routeList) {
                val point = frag?.get(0) //henter ut første punkt i fragmentet
                if (point != null) {
                    val data = airQualDataSource.fetchAirQualAtPointDS(
                        point.latitude.toString(),
                        point.longitude.toString()
                    )
                    val AQIatPoint = getRealtimeAQI(data)
                    if (AQIatPoint != null) {
                        tot += AQIatPoint
                        sampledPoints++
                    }
                }
            }
        } else {
            val perFrag = 10 / numberOfFragments
            for (frag in routeList) {
                if (frag!!.size <= perFrag) {
                    for (point in frag) {
                        val data = airQualDataSource.fetchAirQualAtPointDS(
                            point.latitude.toString(),
                            point.longitude.toString()
                        )
                        val AQIatPoint = getRealtimeAQI(data)
                        if (AQIatPoint != null) {
                            tot += AQIatPoint
                            sampledPoints++
                        }
                    }
                } else {
                    for (x in 0..frag.size - 2 step frag.size / perFrag) {
                        val point = frag.get(x) //henter ut første punkt i fragmentet
                        val data = airQualDataSource.fetchAirQualAtPointDS(
                            point.latitude.toString(),
                            point.longitude.toString()
                        )
                        val AQIatPoint = getRealtimeAQI(data)
                        if (AQIatPoint != null) {
                            tot += AQIatPoint
                            sampledPoints++
                        }
                    }
                }
            }
        }
        Log.d("sjekkverdi", "tot ${tot} , sampledPoints ${sampledPoints}")
        return tot.div(sampledPoints).round(3)
    }
}