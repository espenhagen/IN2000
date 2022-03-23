package com.example.in2000team5

import com.google.android.gms.maps.model.LatLng


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