package com.example.in2000team5.ui_layer.cardViewActivity

import android.text.style.BackgroundColorSpan
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.in2000team5.R
import com.example.in2000team5.data_layer.BicycleRoute
import com.example.in2000team5.domain_layer.WeatherDataViewModel
import com.example.in2000team5.ui_layer.theme.IN2000Team5Theme
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import org.intellij.lang.annotations.JdkConstants


@Composable
fun InfoRow(model: WeatherDataViewModel) {
    Surface(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
    ) {
        Row {
            val id = getWeatherIcon(model.liveSymbol.value)

            //Det skal gå an å hente id fra en streng - men får ikke til.
            //val denne = android.content.res.Resources.getSystem()
            //val id= denne.getIdentifier("bike","drawable","com.example.in2000team5")

            Image(
                painter = painterResource(id = id),
                contentDescription = "en sol",
            )
            Text(
                text = "${model.liveTemperature.value}°C",
                style = MaterialTheme.typography.h4
            )
            //kan legge til vindretning eller liknende her:
            Image(
                painter = painterResource(id = id),
                contentDescription = "en sol",
            )
            Column() {
                Text(
                    text = "vindretning: ${model.liveWindDirection.value}",
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    text = "vindstyrke: ${model.liveWindSpeed.value}",
                    style = MaterialTheme.typography.body1,
                )
            }

        }
    }
}


@Composable
fun SykkelRuteCard(rute: BicycleRoute) {
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
                text = "${rute.start} - ${rute.end}",
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.width(10.dp))

            Spacer(modifier = Modifier.height(6.dp))
            Column {
                Row {
                    Column {
                        Text(
                            text = "Lengde: ${rute.distance.toInt()} meter",
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
                                    painter = painterResource(R.drawable.clearsky_day),
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
                                    text = rute.routeNr.toString(),
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
                    Text(
                        text = "bad",
                        modifier = Modifier
                            .padding(all = 4.dp)
                            .fillMaxWidth(),
                        style = MaterialTheme.typography.body2,
                        lineHeight = 30.sp
                    )
                }
            }
            Row {
                if (isExpanded) {
                    val plass = rute.coordinates?.get(0)
                    //val singapore = LatLng(1.35, 103.87)

                    val cameraPositionState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(plass!!, 10f)
                    }
                    GoogleMap(
                        //modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,

                        ) {
                        Marker(
                            position = plass!!,
                            title = rute.start,
                        )
                        Polyline(rute.coordinates)
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.width(10.dp))
}


@Composable
fun VisAlleRuter(ruter: List<BicycleRoute>) {
    LazyColumn(
        modifier = Modifier.padding(bottom = 55.dp)
    ) {
        items(ruter) { rute ->
            SykkelRuteCard(rute)

        }
    }
}

/*@Preview
@Composable
fun PreviewVisAleRuter(){
    IN2000Team5Theme() {
        VisAlleRuter(ruter = SampleData.eksempelRuter)
    }
}
*/

fun getWeatherIcon(description: String?): Int {
    return when (description) {
        "clearsky" -> R.drawable.clearsky_day
        "cloudy" -> R.drawable.cloudy
        "fair" -> R.drawable.fair_day
        "fog" -> R.drawable.fog
        "heavyrain" -> R.drawable.heavyrain
        "heavyrainandthunder" -> R.drawable.heavyrainandthunder
        "heavyrainshowers" -> R.drawable.heavyrainshowers_day
        "heavyrainshowersandthunder" -> R.drawable.heavyrainshowersandthunder_day
        "heavysleet" -> R.drawable.heavysleet
        "heavysleetandthunder" -> R.drawable.heavysleetandthunder
        "heavysleetshowers" -> R.drawable.heavysleetshowers_day
        "heavysleetshowersandthunder" -> R.drawable.heavysleetshowersandthunder_day
        "heavysnow" -> R.drawable.heavysnow
        "heavysnowandthunder" -> R.drawable.heavysnowandthunder
        "heavysnowshowers" -> R.drawable.heavysnowshowers_day
        "heavysnowshowersandthunder" -> R.drawable.heavysnowshowersandthunder_day
        "lightrain" -> R.drawable.lightrain
        "lightrainandthunder" -> R.drawable.lightrainandthunder
        "lightrainshowers" -> R.drawable.lightrainshowers_day
        "lightrainshowersandthunder" -> R.drawable.lightrainshowersandthunder_day
        "lightsleet" -> R.drawable.lightsleet
        "lightsleetandthunder" -> R.drawable.lightsleetandthunder
        "lightsleetshowers" -> R.drawable.lightsleetshowers_day
        "lightsnow" -> R.drawable.lightsnow
        "lightsnowandthunder" -> R.drawable.lightsnowandthunder
        "lightsnowshowers" -> R.drawable.lightsnowshowers_day
        "lightssleetshowersandthunder" -> R.drawable.lightssleetshowersandthunder_day
        "lightssnowshowersandthunder" -> R.drawable.lightssnowshowersandthunder_day
        "partlycloudy" -> R.drawable.partlycloudy_day
        "rain" -> R.drawable.rain
        "rainandthunder" -> R.drawable.rainandthunder
        "rainshowers" -> R.drawable.rainshowers_day
        "rainshowersandthunder" -> R.drawable.rainshowersandthunder_day
        "sleet" -> R.drawable.sleet
        "sleetandthunder" -> R.drawable.sleetandthunder
        "sleetshowers" -> R.drawable.sleetshowers_day
        "sleetshowersandthunder" -> R.drawable.sleetshowersandthunder_day
        "snow" -> R.drawable.snow
        "snowandthunder" -> R.drawable.snowandthunder
        "snowshowers" -> R.drawable.snowshowers_day
        "snowshowersandthunder" -> R.drawable.snowshowersandthunder_day
        else -> {
            R.drawable.unknown
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
