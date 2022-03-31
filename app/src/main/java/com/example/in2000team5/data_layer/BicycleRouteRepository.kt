package com.example.in2000team5.data_layer


import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.example.in2000team5.domain_layer.BicycleViewModel
import com.google.android.gms.maps.model.LatLng
import org.locationtech.proj4j.CRSFactory
import org.locationtech.proj4j.CoordinateTransformFactory
import org.locationtech.proj4j.ProjCoordinate
import java.util.*
import kotlin.math.round

class BicycleRouteRepository {

    private val airQualDataSource = AirQualDataSource()
    private val bikeRoutedatasrc = BicycleRouteRemoteDataSource()

    suspend fun constructRoutesThreads(bicycleViewModel: BicycleViewModel, context: Context) {
        val features = bikeRoutedatasrc.fetchRoutes()
        if (features != null) {
            for (bicycleFeature in features) {
                makeEachRoute(bicycleFeature, context, bicycleViewModel)

            }
        }
    }

    private fun makeEachRoute(bicycleFeature: Features, context: Context, bicycleViewModel: BicycleViewModel) {

            val geocoder = Geocoder(context)
            val id = bicycleFeature.properties?.objectid
            val routeNr = bicycleFeature.properties?.rute
            val latLngList = constructLatLngList(bicycleFeature.geometry?.coordinates)

            val firstLatLng = latLngList?.get(0)
            val lastLatLng = latLngList?.get(latLngList.lastIndex)

            val startAddress = getAddress(geocoder, firstLatLng)
            val endAddress = getAddress(geocoder, lastLatLng)

            val startDistrict = constructDistrict(startAddress)
            val endDistrict = constructDistrict(endAddress)
            val start = constructAddress(startAddress)
            val end = constructAddress(endAddress)

            var total = 0.0
            if (latLngList != null) {
                for (i in 1 until latLngList.size) {
                    val lengthResult = FloatArray(1)
                    Location.distanceBetween(
                        latLngList[i - 1].latitude,
                        latLngList[i - 1].longitude,
                        latLngList[i].latitude,
                        latLngList[i].longitude,
                        lengthResult
                    )
                    total += lengthResult[0]
                }
            }
        val bicycleRoute = BicycleRoute(id, routeNr, latLngList, startDistrict, endDistrict, start, end, total, null)
        bicycleViewModel.fetchAirQualForRouteOnAvg(bicycleRoute)
        bicycleViewModel.postRoutes(bicycleRoute)

    }

    private fun getAddress(geocoder: Geocoder, latLng: LatLng?): Address? {
        if (latLng != null) {
            val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            return address[0]
        }
        return null
    }

    private fun constructAddress(address: Address?): String? {
        if (address != null) {
            if (address.thoroughfare == null) {
                return address.getAddressLine(0)
            }
            return address.thoroughfare
        }
        return null
    }

    private fun constructDistrict(address: Address?): String? {
        if (address != null) {
            return address.subLocality
        }
        return null
    }

    private fun constructLatLngList(utmList: List<List<Number>>?): List<LatLng>? {
        if (utmList == null) return null

        val routes = mutableListOf<LatLng>()

        // Setting up transforming coordinates
        val ctFactory = CoordinateTransformFactory()
        val csFactory = CRSFactory()
        val csName1 = "EPSG:25832" // UTM
        val csName2 = "EPSG:4326"   // WSG84
        val crs1 = csFactory.createFromName(csName1)
        val crs2 = csFactory.createFromName(csName2)
        val trans = ctFactory.createTransform(crs1, crs2)   // Creating transform from UTM to WSG84

        // Input and output points.
        // Constructed once per thread and then reused.
        val point1 = ProjCoordinate()
        val point2 = ProjCoordinate()

        for (i in utmList.indices) {
            point1.x = utmList[i].component1().toDouble()
            point1.y = utmList[i].component2().toDouble()
            // Transform point
            trans.transform(point1, point2)
            // Adding list of transformed coordinates to the route
            routes.add(LatLng(point2.y, point2.x))
        }
        return routes
    }



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
        for(point in routeList){
            val data = airQualDataSource.fetchAirQualAtPointDS(point.latitude.toString(), point.longitude.toString())
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