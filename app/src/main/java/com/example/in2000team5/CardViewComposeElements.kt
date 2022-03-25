package com.example.in2000team5

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.in2000team5.ui.theme.IN2000Team5Theme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun InfoRow(uv:Int, sykkelfore:String){
    Surface(
        shape = MaterialTheme.shapes.small,
        elevation = 2.dp,
        modifier = Modifier
            .height(60.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colors.secondary
    ) {
        Row(){
            Image(painter = painterResource(id = R.drawable.sunscreen), contentDescription = "en sol" )
            Text(
                text = "UV-indeks: $uv",
                style = MaterialTheme.typography.body1,
            )
            Image(painter = painterResource(id = R.drawable.bike), contentDescription = "en sykkel")
            Text(
                text = "Sykkelføre: $sykkelfore",
                style = MaterialTheme.typography.body1,
            )
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
        Row(
            modifier = Modifier
                .padding(all = 8.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.bike),
                contentDescription = "Weather",
                modifier = Modifier
                    .size(100.dp)
                    .border(1.5.dp, MaterialTheme.colors.secondary)
            )
            Spacer(modifier = Modifier.width(10.dp))

            //Variabel for å huske om meldingen er expanded eller ikke
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
                                        painter = painterResource(R.drawable.sun),
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
                                        text = "${String.format("%.1f", 100.10)}°C",
                                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                                        style = MaterialTheme.typography.body2,

                                        )
                                }
                                Column {
                                    Image(
                                        painter = painterResource(getAirIcon("bad")),
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
                                        text = "nyde",
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
    }
    Spacer(modifier = Modifier.width(10.dp))
}


@Composable
fun VisAlleRuter(ruter: List<BicycleRoute>) {
    LazyColumn {
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





fun getWeatherIcon(description: String): Int {
    return when (description) {
        "rainy" -> R.drawable.rain
        "sunny" -> R.drawable.sun
        "both" -> R.drawable.suncloud
        "cloudy" -> R.drawable.cloud
        else -> {
            R.drawable.unknown
        }
    }
}

fun getAirIcon(description: String): Int {
    return when (description) {
        "good" -> R.drawable.goodair
        "bad" -> R.drawable.badair
        else -> {
            R.drawable.unknown
        }
    }
}
