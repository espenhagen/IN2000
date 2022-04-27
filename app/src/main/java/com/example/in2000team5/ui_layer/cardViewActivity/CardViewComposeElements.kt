package com.example.in2000team5.ui_layer.cardViewActivity

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.in2000team5.R
import com.example.in2000team5.data_layer.repository.BicycleRoute
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel
import com.example.in2000team5.utils.MetUtils
import com.example.in2000team5.utils.MetUtils.Companion.getWeatherIcon
import com.example.in2000team5.utils.RouteUtils.Companion.routeColor
import com.example.in2000team5.utils.SupportInfo
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*

val wDetails = mutableStateOf<String?>(null)
val rClothing = mutableStateOf<String?>(null)
val conditons = mutableStateOf<String?>(null)
val checklist = mutableStateOf<String?>(null)

@Composable
fun SupportBox(model: WeatherDataViewModel) {

    Surface(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = MaterialTheme.colors.background
    ) {
            Column {
                /*
                Text(
                    text = "Planlegg Dagen:",
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.h3,
                    textDecoration = TextDecoration.Underline
                )
                */

                if(model.weaterTimes.size!=0){
                    TimeSlide(model)
                }

                checklist.value = SupportInfo.getChecklist()

                InfoBox(model = model, "Detaljert om været:", wDetails, Color.LightGray)
                InfoBox(model = model, "Anbefalt påkledning:", rClothing, Color.White)
                InfoBox(model = model, "Sykkelforhold:", conditons, Color.LightGray)
                InfoBox(model = model, "Vedlikehold checklist:", checklist, Color.White)


            }
        }
    }

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimeSlide(model: WeatherDataViewModel) {
    var sliderPosition by remember { mutableStateOf(0f..(model.weaterTimes.lastIndex).toFloat()) }
    updateSupportData(model, sliderPosition)
    Text(text = "Fra: " + MetUtils.getDateAndHour(model.weaterTimes[sliderPosition.start.toInt()].time.toString()) + "\r\nTil: " + MetUtils.getDateAndHour(model.weaterTimes[sliderPosition.endInclusive.toInt()].time.toString())  )
    RangeSlider(
        values = sliderPosition,
        onValueChange = { sliderPosition = it },
        valueRange = 0f..model.weaterTimes.lastIndex.toFloat(),
        steps = model.weaterTimes.size,
        onValueChangeFinished = {
            updateSupportData(model, sliderPosition)
        },
    )
}

fun updateSupportData(
    model: WeatherDataViewModel,
    sliderPosition: ClosedFloatingPointRange<Float>
) {
    val start = sliderPosition.start.toInt()
    val end = sliderPosition.endInclusive.toInt() -1
    wDetails.value = SupportInfo.getWeatherDetailsInfo(model, start, end)
    rClothing.value = SupportInfo.getRecommendedClothing(model, start, end)
    conditons.value = SupportInfo.getBikeConditions(model, start, end)
}

@Composable
fun InfoBox(
    model: WeatherDataViewModel,
    headText: String,
    info: MutableState<String?>,
    lightGray: Color
){
    Column(
        Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .background(color = Color.LightGray)
    ){
        Text(
            modifier = Modifier.padding(5.dp),
            text = headText,
            color = MaterialTheme.colors.secondaryVariant,
            style = MaterialTheme.typography.h5
        )

        if (info.value!= null){

            Text(
                modifier = Modifier.padding(3.dp),
                text = info.value!!
            )
        }
    }

}




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
                        .padding(horizontal = 20.dp, vertical = 10.dp)
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
fun SykkelRuteCard(rute: SnapshotMutableState<BicycleRoute>) {
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
                            Polyline(fragment!!, color = routeColor(rute.value.id))
                        }
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.width(10.dp))
}



@Composable
fun VisAlleRuter(ruter: SnapshotStateList<SnapshotMutableState<BicycleRoute>>) {
    val choices = mutableListOf("ID", "Luftkvalitet", "Lengde")

//SPINNER:
    //Kode hentet fra: https://intensecoder.com/spinner-in-jetpack-compose-dropdown/
    //variabler for å holde på state.
    var valg: String by remember { mutableStateOf(choices[0]) }
    var expanded by remember { mutableStateOf(false)}

    Column() {
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
            if (valg == "ID") {
                items(ruter.sortedBy { it.value.id }) { rute ->
                    SykkelRuteCard(rute)

                }
            } else if (valg == "Luftkvalitet") {
                items(ruter.sortedBy { it.value.AQI.value }) { rute ->
                    SykkelRuteCard(rute)

                }
            } else if (valg == "Lengde") {
                items(ruter.sortedBy { it.value.length }) { rute ->
                    SykkelRuteCard(rute)

                }
            } else {
                items(ruter) { rute ->
                    SykkelRuteCard(rute)
                }
            }
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
