package com.example.in2000team5

import com.google.android.gms.maps.model.LatLng

/*
data class SykkelRute (
    val From: String,
    val To: String,
    val Temp: Float,
    val Weather: String,
    val Lengde: String,
    val HoydeDiff: String,
    val Luftkvalitet: String,
    val beskrivelse: String,
    val Longitude: Double,
    val Latitude: Double,
    val rutedata: List<LatLng>? = null,
)
*/

data class BicycleRoute(
            val id: Number?,
            val routeNr: Number?,
            val coordinates: List<LatLng>?,
            val startDistrict: String?,
            val endDistrict: String?,
            val start: String?,
            val end: String?,
            val distance: Double
            )