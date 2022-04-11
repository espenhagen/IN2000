package com.example.in2000team5.ui_layer.cardViewActivity

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.in2000team5.R
import com.example.in2000team5.data_layer.BicycleRoute
import com.example.in2000team5.data_layer.BigBikeRoute
import com.example.in2000team5.ui_layer.bicycleRouteList
import com.example.in2000team5.domain_layer.WeatherDataViewModel
import com.example.in2000team5.utils.metUtils.Companion.getWeatherIcon
import com.example.in2000team5.utils.routeUtils.Companion.routeColor
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import kotlin.math.absoluteValue


@Composable
fun InfoRow(model: WeatherDataViewModel) {
    Surface(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        color = MaterialTheme.colors.background
    ) {
        Row {
            val id = getWeatherIcon(model.getSymbolName())
            Log.d("symbolnavn","${model.getSymbolName()}" )

            //Det skal gå an å hente id fra en streng - men får ikke til, så bruker "getWeatherIcon()"
            //val denne = android.content.res.Resources.getSystem()
            //val id= denne.getIdentifier("bike","drawable","com.example.in2000team5")
            Image(
                painter = painterResource(id = id),


                contentDescription = "en sol",
                Modifier
                    .size(70.dp)
                    .padding(3.dp)

            )
            Text(
                text = "${model.getTemperature()}°C",
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(horizontal = 6.dp)

            )
            //kan legge til vindretning eller liknende her:
            val windDirection = model.getWindDirection()
            if (windDirection == null){
                Image(
                    painter = painterResource(R.drawable.unknown),
                    contentDescription = "kunne ikke hente vindretning")
            }else {
                Image(
                    painter = painterResource(R.drawable.wind_arrow),
                    contentDescription = "en sol",
                    Modifier
                        .rotate(windDirection.toFloat())
                        . padding(horizontal = 20.dp, vertical = 10.dp)
                        .size(40.dp)
                )
            }
            Column(
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
            ) {
                Text(
                    text = "vindretning: ${model.getWindDirection()}",
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    text = "vindstyrke: ${model.getWindSpeed()}",
                    style = MaterialTheme.typography.body1,
                )
            }

        }
    }
}


@Composable
fun SykkelRuteCard(rute: BigBikeRoute) {
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
                text = "${rute.start} - ${rute.slutt}",
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.width(10.dp))

            Spacer(modifier = Modifier.height(6.dp))
            Column {
                Row {
                    Column {
                        Text(
                            text = "Lengde: ${rute.length.toInt()} meter",
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
                                    text = rute.id.toString(),
                                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                                    style = MaterialTheme.typography.body2,

                                    )
                            }
                            Column {
                                Image(
                                    painter = painterResource(getAirIcon(rute.AQI)),
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
                                    text = rute.AQI.toString(),
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
                    val plass = rute.fragmentList[0]?.get(0)

                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(plass!!, 10f)
                    }
                    GoogleMap(
                        modifier = Modifier.height(200.dp),
                        cameraPositionState = cameraPositionState,

                        ) {
//
                        for (fragment in rute.fragmentList) {
                            Polyline(fragment!!, color = routeColor(rute.id))
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.width(10.dp))
}


@Composable
fun VisAlleRuter(ruter: List<BigBikeRoute>) {
    LazyColumn(
        modifier = Modifier.padding(bottom = 55.dp)
    ) {
        items(ruter) { rute ->
            SykkelRuteCard(rute)

        }
    }
}



fun getAirIcon(index: Double?): Int {
    if (index == null) {
        return R.drawable.unknown
    } else if (index < 2) {
        return R.drawable.goodair
    }
    return R.drawable.badair
}
