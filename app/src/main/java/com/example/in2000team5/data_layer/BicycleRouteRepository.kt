package com.example.in2000team5.data_layer


import android.util.Log
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.math.round

class BicycleRouteRepository {
    private val bicycleRouteRemoteDataSource = BicycleRouteRemoteDataSource()

    private val airQualDataSource = AirQualDataSource()

    fun getRealtimeAQI(aqiDataobj : AirQualData?) : Double?{

        val data = aqiDataobj?.data?.time?.find { it.from.equals(getCurrentTimeAsString())}

        return data?.variables?.AQI?.value?.toDouble()

    }

    fun getCurrentTimeAsString() : String{

        //paddStart sørger for at verdier får 0 foran seg selvom verdien er under 10
        //Calender.XXXX gir en index hvor verdien finnes i listen

        val year = Calendar.getInstance()[Calendar.YEAR]
        //Verdien fra måender starter på 0 (Januar er 0)
        val month = Calendar.getInstance()[Calendar.MONTH].plus(1).toString().padStart(2, '0')
        val date = Calendar.getInstance()[Calendar.DAY_OF_MONTH].toString().padStart(2, '0')
        val hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY].toString().padStart(2, '0')

        //Standard reftime for luftkvalitets-API
        return  "${year}-${month}-${date}T${hour}:00:00Z"
    }

    suspend fun fetchAirQualAtRoute(routeList:List<LatLng>): Double? {
        var avg = 0.0
        var numberOfPoint = routeList.size
        for(route in routeList){
            val data = airQualDataSource.fetchAirQualAtPointDS(route.latitude.toString(), route.longitude.toString())
            val airIndex = getRealtimeAQI(data)

            if (airIndex != null) {
                avg += airIndex
            }
            else{
                numberOfPoint-=1
            }
        }
        return try {
            avg.div(numberOfPoint).round(2)
        } catch (exception: Exception) {
            Log.d("Response", "Error while finding air index: ${exception.message}")
            null
        }
    }

    //found on internett (stackoverflow)
    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }

}