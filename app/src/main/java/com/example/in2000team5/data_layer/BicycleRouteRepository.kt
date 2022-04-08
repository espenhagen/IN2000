package com.example.in2000team5.data_layer


import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import com.example.in2000team5.domain_layer.BicycleViewModel
import com.example.in2000team5.utils.metUtils
import com.example.in2000team5.utils.routeUtils
import com.google.android.gms.maps.model.LatLng
import org.locationtech.proj4j.CRSFactory
import org.locationtech.proj4j.CoordinateTransformFactory
import org.locationtech.proj4j.ProjCoordinate
import kotlin.math.round

class BicycleRouteRepository {

    private val airQualDataSource = AirQualDataSource()
    private val bikeRoutedatasrc = BicycleRouteRemoteDataSource()
    private val bigRouteMap: HashMap<Int, MutableList<List<LatLng>?>> = HashMap()

    suspend fun constructRoutesThreads(bicycleViewModel: BicycleViewModel, context: Context) {
        val features = bikeRoutedatasrc.fetchRoutes()
        if (features != null) {
            for (bicycleFeature in features) {
                makeEachRoute(bicycleFeature, context, bicycleViewModel)
            }
        }
    }

    suspend fun makeBigRoutes(bicycleViewModel: BicycleViewModel, context: Context) {
        bikeRoutedatasrc.fetchRoutes()?.forEach {
            addCords(it)
        }

        //Naa har jeg et hashmap med en liste med alle smaaturene
        val routeNames = routeUtils.routeNames()


        bigRouteMap.forEach {


            val bigBikeRoute = BigBikeRoute(
                it.key,
                it.value,
                routeNames[it.key]?.get(0)!!,
                routeNames[it.key]?.get(1)!!,
                calculateRouteLength(it.value),
                2.0
            )
            bicycleViewModel.getAirQualAvgForRoute(bigBikeRoute)

            bicycleViewModel.postRoutes(bigBikeRoute)
            //Log.d("start", bigBikeRoute.start)
            //Log.d("slutt", bigBikeRoute.slutt)
            //Log.e("big Route", bigBikeRoute.toString())
        }


        //logging og testing
        val stringBuilder = StringBuilder()
        var antNoder = 0
        stringBuilder.append("map->").append("\n")
        for (i in 0 until bigRouteMap.size) {
            stringBuilder.append("$i: ", bigRouteMap[i]?.size).append("\n")
            antNoder += bigRouteMap[i]?.size!!
        }
        stringBuilder.append("antNoder: ").append(antNoder)
        Log.d("RouteCordMap", stringBuilder.toString())


    }

    private fun addCords(bicycleFeature: Features) {
        val latLngList = constructLatLngList(bicycleFeature.geometry?.coordinates)
        val id: Int
        if (bicycleFeature.properties?.rute == null) {
            id = 0
        } else {
            id = bicycleFeature.properties.rute.toInt()
        }

        if (!bigRouteMap.containsKey(id)) {
            bigRouteMap[id] = mutableListOf(latLngList)
        } else {
            bigRouteMap[id]?.add(latLngList)
        }
    }

    private fun makeEachRoute(
        bicycleFeature: Features,
        context: Context,
        bicycleViewModel: BicycleViewModel
    ) {

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

        var total: Double = 0.0
        if (latLngList != null) {
            total = latLngListLength(latLngList)
        }
        val bicycleRoute =
            BicycleRoute(routeNr, latLngList, startDistrict, endDistrict, start, end, total)
    }

    private fun latLngListLength(latLngList: List<LatLng>): Double {
        var total = 0.0
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
        return total
    }

    private fun calculateRouteLength(ruteliste: MutableList<List<LatLng>?>): Double {
        //var sum = 0.0

        //ruteliste.forEach {
        //    sum += latLngListLength(it!!)
        //}

        //return sum

        return ruteliste.fold(0.0) { total, next -> total + latLngListLength(next!!) }
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
        return tot.div(sampledPoints)
    }

    //found on internett (stackoverflow)
    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return round(this * multiplier) / multiplier
    }
}

data class BicycleRoute(

    val routeNr: Number?,
    val coordinates: List<LatLng>?,
    val startDistrict: String?,
    val endDistrict: String?,
    val start: String?,
    val end: String?,
    val distance: Double,
)

data class BigBikeRoute(
    val id: Int,
    val fragmentList: MutableList<List<LatLng>?>,
    val start: String,
    val slutt: String,
    val length: Double,
    var AQI: Double?
)