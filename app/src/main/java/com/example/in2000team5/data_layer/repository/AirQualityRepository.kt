package com.example.in2000team5.data_layer.repository

import com.example.in2000team5.data_layer.datasource.AirQualData
import com.example.in2000team5.data_layer.datasource.AirQualityRemoteDataSource
import com.example.in2000team5.utils.MetUtils
import com.google.android.gms.maps.model.LatLng
import com.example.in2000team5.utils.GeneralUtils.Companion.round

const val MIN_NUM_OF_SAMPLE_POINTS = 10

/**
 * Fetches current air quality index from remote datasource, and prepares data for the view model.
 */
class AirQualityRepository(
    private val airQualityDataSource: AirQualityRemoteDataSource
) {
    private var totalAirQualityIndex = 0.0
    private var sampledPoints = 0


    /**
     * Calculates an average from the routes in the [routeList] and return an average air quality
     * index for the route.
     * The average is calculated on selected routes, which the method itself selects.
     */
    suspend fun fetchAvgAirQualityAtRoute(routeList: MutableList<List<LatLng>?>): Double {
        val numberOfFragments = routeList.size

        if (numberOfFragments >= MIN_NUM_OF_SAMPLE_POINTS) {
            for (fragment in routeList) {
                val point = fragment?.get(0) // Returns the first point of the fragment
                if (point != null) {
                    incrementTotalIndexFromPoint(point)
                }
            }
        } else {
            val perFrag = 10 / numberOfFragments
            for (fragment in routeList) {
                if (fragment!!.size <= perFrag) {
                    for (point in fragment) {
                        incrementTotalIndexFromPoint(point)
                    }
                } else {
                    for (x in 0..fragment.size - 2 step fragment.size / perFrag) {
                        val point = fragment[x]
                        incrementTotalIndexFromPoint(point)
                    }
                }
            }
        }
        return totalAirQualityIndex.div(sampledPoints).round(-1)
    }

    /* Helper method adds point air quality to total air quality */
    private suspend fun incrementTotalIndexFromPoint(point: LatLng) {
        val data = airQualityDataSource.fetchAirQualityAtPointDataSource(
            point.latitude.toString(),
            point.longitude.toString()
        )
        val airQualityAtPoint = getRealtimeAQI(data)
        if (airQualityAtPoint != null) {
            totalAirQualityIndex += airQualityAtPoint
            sampledPoints++
        }

    }

    /* Helper method returns point air quality from data source */
    private fun getRealtimeAQI(aqiDataObject: AirQualData?): Double? {
        val data = aqiDataObject?.data?.time?.find {
            it.from.equals(MetUtils.getCurrentTimeAsString())
        }
        return data?.variables?.AQI?.value?.toDouble()
    }
}