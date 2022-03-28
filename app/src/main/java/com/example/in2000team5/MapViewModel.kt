package com.example.in2000team5

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MapsViewModel: ViewModel() {

    var state by mutableStateOf(MapState())
}
