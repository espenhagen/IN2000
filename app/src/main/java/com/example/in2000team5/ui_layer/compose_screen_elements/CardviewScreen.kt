package com.example.in2000team5.ui_layer.compose_screen_elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.in2000team5.R
import com.example.in2000team5.data_layer.repository.BicycleRoute
import com.example.in2000team5.utils.RouteUtils
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun BicycleRouteCard(rute: SnapshotMutableState<BicycleRoute>) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
    ) {

        var isExpanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier
            .clickable { isExpanded = !isExpanded }
        ) {

            Text(
                text = "${rute.value.start} - ${rute.value.end}",
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.width(10.dp))

            Spacer(modifier = Modifier.height(6.dp))
            Column {
                Row {
                    Column {
                        Text(
                            text = "Lengde: ${rute.value.length.toInt()} meter",
                            modifier = Modifier
                                .padding(all = 4.dp)
                                .width(160.dp),
                            // If the rute is expanded, we display all its content
                            // otherwise we only display the first line
                            maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                            style = MaterialTheme.typography.body1,
                            lineHeight = 30.sp
                        )
                    }
                    Column {
                        Row {
                            Column {
                                Image(
                                    painter = painterResource(R.drawable.unknown),
                                    contentDescription = "Weather",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .border(
                                            1.5.dp,
                                            MaterialTheme.colors.secondary,
                                            CircleShape
                                        )

                                )
                                Text(
                                    text = rute.value.id.toString(),
                                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                                    style = MaterialTheme.typography.body2,

                                    )
                            }
                            Column {
                                Image(
                                    painter = painterResource(getAirIcon(rute.value.AQI.value)),
                                    contentDescription = "Weather",
                                    modifier = Modifier
                                        .size(40.dp)
                                        .border(
                                            1.5.dp,
                                            MaterialTheme.colors.secondary,
                                            CircleShape
                                        )
                                )
                                Text(
                                    text = rute.value.AQI.value.toString(),
                                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }
                }
            }
            Row {
                if (isExpanded) {
                    val plass = rute.value.fragmentList[0]?.get(0)

                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(plass!!, 10f)
                    }
                    GoogleMap(
                        modifier = Modifier.height(200.dp),
                        cameraPositionState = cameraPositionState,

                        ) {
//
                        for (fragment in rute.value.fragmentList) {
                            Polyline(fragment!!, color = RouteUtils.routeColor(rute.value.id))
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.width(10.dp))
}

@Composable
fun ShowAllRoutes(ruter: SnapshotStateList<SnapshotMutableState<BicycleRoute>>) {
    val choices = mutableListOf("ID", "Luftkvalitet", "Lengde")

    //SPINNER:
    //Kode hentet fra: https://intensecoder.com/spinner-in-jetpack-compose-dropdown/
    //variabler for å holde på state.
    var valg: String by remember { mutableStateOf(choices[0]) }
    var expanded by remember { mutableStateOf(false)}

    Column {
        Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.Center) {
            Row( modifier = Modifier
                .padding(6.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = valg,fontSize = 18.sp,modifier = Modifier.padding(end = 8.dp))
                Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
            }
            DropdownMenu(expanded = expanded, onDismissRequest = {
                expanded = false
            }) {
                choices.forEach{ choice->
                    DropdownMenuItem(onClick = {
                        expanded = false
                        valg = choice

                    }) {
                        Text(text = choice)
                    }
                }
            }
        }

        //OPPRETTER KORTENE BASERT PÅ VALG I SPINNER (DEFAULT: ID)
        LazyColumn(
            modifier = Modifier.padding(bottom = 55.dp)
        ) {
            when (valg) {
                "ID" -> {
                    items(ruter.sortedBy { it.value.id }) { rute ->
                        BicycleRouteCard(rute)

                    }
                }
                "Luftkvalitet" -> {
                    items(ruter.sortedBy { it.value.AQI.value }) { rute ->
                        BicycleRouteCard(rute)

                    }
                }
                "Lengde" -> {
                    items(ruter.sortedBy { it.value.length }) { rute ->
                        BicycleRouteCard(rute)

                    }
                }
                else -> {
                    items(ruter) { rute ->
                        BicycleRouteCard(rute)
                    }
                }
            }
        }
    }
}