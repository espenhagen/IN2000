package com.example.in2000team5.ui_layer.compose_screen_elements

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import com.example.in2000team5.R
import com.example.in2000team5.ui_layer.viewmodels.BicycleRouteViewModel
import com.example.in2000team5.utils.RouteUtils
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(bicycleRouteViewModel: BicycleRouteViewModel) {
    var openDialog by remember { mutableStateOf(false) }
    var info by remember { mutableStateOf("")}
    var title by remember { mutableStateOf("")}
    var vis by remember { mutableStateOf(false) }


    val oslo = LatLng(59.9139, 10.7522)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(oslo, 12f)
    }
    Box {

        GoogleMap(
            modifier = Modifier.padding(bottom = 50.dp),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(compassEnabled = true, myLocationButtonEnabled = true)
        ) {
            Marker(     // Adds marker to the map
                state = MarkerState(position = oslo),
                title = "Oslo",
                snippet = "Marker in Oslo"
            )
            if(vis){
            for (station in bicycleRouteViewModel.getServiceStations()) {
                station.value.let {
                    Log.d("latlng", it.toString())
                    val name = it.name
                    Circle(
                        it.coordinates,
                        true,
                        Color(100, 100, 255),
                        100.0,
                        Color(0, 0, 0),
                        null,
                        0.0F,
                        null,
                        true,
                        0F,
                        onClick = {
                            info = ("Service-stasjon her:\n$name")
                            title = ("Service-stasjon")
                            openDialog = true
                        }
                    )

                }
            }}

            for (storRute in bicycleRouteViewModel.getRoutes()) {

                for (liste in storRute.value.fragmentList) {
                    liste.let {
                        Polyline(
                            points = it!!,
                            color = RouteUtils.routeColor(storRute.value.id),
                            clickable = true,
                            onClick = {
                                title = "Info om Rute"
                                info =
                                    ("RuteID: ${storRute.value.id} \n${storRute.value.start} - ${storRute.value.end}\n\nSe mer informasjon i listen av ruter.")
                                openDialog = true
                            }
                        )
                    }
                }
//
            }


        }
        if (openDialog) {
            AlertDialog(
                onDismissRequest = {
                    openDialog = false
                },
                title = {
                    Text(text = title)
                },
                text = {
                    Text(
                        text = info
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
        Button(
            modifier = Modifier

                .align(Alignment.TopEnd)
                .offset(),
            onClick = {
                vis = !vis
                Log.d("hei", vis.toString())
            }
        ) {

            Image(
                painterResource(id = R.drawable.unknown),
                contentDescription = "Cart button icon",
                modifier = Modifier.size(40.dp)
            )

            Text(text = "Vis sykkelrep", Modifier.padding(start = 10.dp))
        }
    }
}

