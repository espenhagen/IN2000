package com.example.in2000team5.ui_layer.compose_elements

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.in2000team5.R
import com.example.in2000team5.ui_layer.theme.ServiceStationColor
import com.example.in2000team5.ui_layer.viewmodels.BicycleInformationViewModel
import com.example.in2000team5.utils.RouteUtils
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

//Shows the fulls screen map with the routes and service stations drawn.
@Composable
fun MapScreen(bicycleInformationViewModel: BicycleInformationViewModel) {
    var openDialog by remember { mutableStateOf(false) }
    var info by remember { mutableStateOf("")}
    var title by remember { mutableStateOf("")}
    var showStations by remember { mutableStateOf(true) }


    val oslo = LatLng(59.9139, 10.7522)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(oslo, 12f)
    }
    Box {
        //The fullscreen map
        GoogleMap(
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(compassEnabled = true, myLocationButtonEnabled = true)
        ) {
            Marker(     // Adds marker to the map
                state = MarkerState(position = oslo),
                title = "Oslo",
                snippet = "Marker in Oslo"
            )

            //Draws the routes
            for (storRute in bicycleInformationViewModel.getRoutes()) {
                if (storRute.value.id in 1..8) {
                    for (liste in storRute.value.fragmentList) {
                        Polyline(
                            points = liste!!,
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
            }

            //shows the service stations if this is the users choice (by clicking the button, default = on)
            if (showStations) {
                for (station in bicycleInformationViewModel.getServiceStations()) {
                    station.value.let {
                        Log.d("latlng", it.toString())
                        val name = it.name
                        Circle(
                            it.coordinates,
                            true,
                            ServiceStationColor,
                            100.0,
                            ServiceStationColor,
                            null,
                            0.0F,
                            null,
                            true,
                            1F,
                            onClick = {
                                info = ("Service-stasjon her:\n$name")
                                title = ("Service-stasjon")
                                openDialog = true
                            }
                        )

                    }
                }
            }
        }
        //Dialog to show more information about a route or service station if the user clicks them
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
        //Button to hide/show service stations
        Button(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x= (-10).dp, y=5.dp),
            onClick = {
                showStations = !showStations
            },
            colors = buttonColors(ServiceStationColor, Color.Black)
        ) {
            Column {
                Image(
                    painterResource(id = R.drawable.servicestations),
                    contentDescription = "Service Station Button Icon",
                    modifier = Modifier.size(40.dp)
                )
                val buttonText = if (showStations) {
                    "Skjul"
                } else {
                    "Vis"
                }
                Text(text = buttonText,
                    Modifier
                        .padding(start = 0.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }
        }
    }
}
