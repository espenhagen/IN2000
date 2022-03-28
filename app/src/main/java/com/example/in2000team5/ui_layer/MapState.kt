package com.example.in2000team5.ui_layer

import com.google.maps.android.compose.MapProperties

data class MapState(
    val properties: MapProperties = MapProperties(),
    val isFalloutMap: Boolean = false
)
