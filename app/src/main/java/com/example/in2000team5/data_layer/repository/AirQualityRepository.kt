package com.example.in2000team5.data_layer.repository

import com.example.in2000team5.data_layer.datasource.AirQualData
import com.example.in2000team5.data_layer.datasource.AirQualityRemoteDataSource
import com.example.in2000team5.utils.MetUtils
import com.google.android.gms.maps.model.LatLng
import com.example.in2000team5.utils.GeneralUtils.Companion.round

// Fetches current AQI from remote datasource, and prepares AQI-data for the viewmodel.
class AirQualityRepository(
    private val airQualityDataSource: AirQualityRemoteDataSource
) {
    private fun getRealtimeAQI(aqiDataObject: AirQualData?): Double? {
        val data = aqiDataObject?.data?.time?.find {
            it.from.equals(MetUtils.getCurrentTimeAsString())
        }
        return data?.variables?.AQI?.value?.toDouble()
    }

    /**
     * Calculates an average from the routes in the [routeList] and return an average air quality
     * index for the route.
     * The average is calculated on selected routes, which the method itself selects.
     */
    suspend fun fetchAvgAirQualityAtRoute(routeList: MutableList<List<LatLng>?>): Double {
        var tot = 0.0
        var sampledPoints = 0
        val MIN_NUM_OF_SAMPLEPOINTS = 10

        val numberOfFragments = routeList.size

        if (numberOfFragments >= MIN_NUM_OF_SAMPLEPOINTS) {
            for (frag in routeList) {
                val point = frag?.get(0) //henter ut første punkt i fragmentet
                if (point != null) {
                    val data = airQualityDataSource.fetchAirQualityAtPointDataSource(
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
                        val data = airQualityDataSource.fetchAirQualityAtPointDataSource(
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
                        val point = frag[x] //henter ut første punkt i fragmentet
                        val data = airQualityDataSource.fetchAirQualityAtPointDataSource(
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
        return tot.div(sampledPoints).round(3)
    }
}