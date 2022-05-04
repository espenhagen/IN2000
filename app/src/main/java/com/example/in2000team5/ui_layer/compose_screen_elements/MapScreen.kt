package com.example.in2000team5.ui_layer.compose_screen_elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.in2000team5.ui_layer.viewmodels.BicycleRouteViewModel
import com.example.in2000team5.utils.RouteUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(
    bicycleRouteViewModel: BicycleRouteViewModel,
    latitude: Double,
    longitude: Double,
) {
    var openDialog by remember { mutableStateOf(false) }
    var info by remember { mutableStateOf("")}

    val oslo = LatLng(59.9139, 10.7522)
    val startpos = LatLng(latitude, longitude)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(oslo, 15f)
    }

    GoogleMap(
        modifier = Modifier.padding(bottom = 50.dp),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(compassEnabled = true, myLocationButtonEnabled = true)
    ) {
        Marker(     // Adds marker to the map
            position = startpos,
            title = "Oslo",
            snippet = "Marker in Oslo"
        )

        for (storRute in bicycleRouteViewModel.getRoutes()) {
            if (storRute.value.id != 0 && storRute.value.id < 9) {
                for (liste in storRute.value.fragmentList) {
                    liste.let {
                        Polyline(
                            points = it!!,
                            color = RouteUtils.routeColor(storRute.value.id),
                            clickable = true,
                            onClick = {
                                info = ("RuteID: ${storRute.value.id} \n${storRute.value.start} - ${storRute.value.end}\n\nSe mer informasjon i listen av ruter.")
                                openDialog = true
                            }
                        )
                    }
                }
//
            }

        }
    }
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            title = {
                Text(text = "Info om ruten")
            },
            text = {
                Text(text = info
                )
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(all = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openDialog = false }
                    ) {
                        Text("Ok")
                    }
                }
            }
        )
    }

}