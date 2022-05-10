package com.example.in2000team5.ui_layer.compose_screen_elements


import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.in2000team5.data_layer.repository.BicycleRoute
import com.example.in2000team5.utils.RouteUtils
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun ShowAllRoutes(ruter: SnapshotStateList<SnapshotMutableState<BicycleRoute>>) {
    val choices = mutableListOf("ID", "Luftkvalitet", "Lengde", "Alfabetisk")
    var valg: String by remember { mutableStateOf(choices[0]) }

    Column {
        valg = customSpinner(choices) //collected from spinner composable object

        LazyColumn(
            modifier = Modifier.padding(bottom = 55.dp)
        ) {
            when (valg) {

                "ID" -> {
                    items(ruter.sortedBy { it.value.id }) { rute ->
                        if(rute.value.id > 0) BicycleRouteCard(rute)

                    }
                }
                "Luftkvalitet" -> {
                    items(ruter.sortedBy { it.value.AQI.value }) { rute ->
                        if(rute.value.id > 0) BicycleRouteCard(rute)

                    }
                }
                "Lengde" -> {
                    items(ruter.sortedBy { it.value.length }) { rute ->
                        if(rute.value.id > 0) BicycleRouteCard(rute)
                    }
                }
                "Alfabetisk" -> {
                    items(ruter.sortedBy { it.value.start }) { rute ->
                        if(rute.value.id > 0) BicycleRouteCard(rute)
                    }
                }
                else -> {
                    items(ruter) { rute ->
                        if(rute.value.id > 0) BicycleRouteCard(rute)
                    }
                }
            }
        }
    }
}


@Composable
fun BicycleRouteCard(rute: SnapshotMutableState<BicycleRoute>) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 3.dp)
    ) {

        var isExpanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier
            .clickable(
                onClickLabel = (if (isExpanded) R.string.minimize_card else R.string.expand_card).toString()
            ) { isExpanded = !isExpanded }
        ) {
            //Headline
            Text(
                text = "${rute.value.start} - ${rute.value.end}",
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .padding(10.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Lengde: ${rute.value.length.toInt()} meter\nRuteID: ${rute.value.id}",
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .width(160.dp),
                        //maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                        style = MaterialTheme.typography.body1,
                        lineHeight = 30.sp
                    )
                    //AQI
                    AirQualIcon(aqi = rute.value.AQI.value)
            Row {
                if (isExpanded) {
                    if (rute.value.id in 1..8) LiteMap(rute = rute)
                    else{
                        val context = LocalContext.current
                        Toast.makeText(context,"Kan ikke vise kart for egne ruter.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}


@Composable
fun LiteMap(rute: SnapshotMutableState<BicycleRoute>){
    
    val plass = rute.value.fragmentList[0]?.get(0)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(plass!!, 12f)
    }
    GoogleMap(
        googleMapOptionsFactory = {
            GoogleMapOptions()
                .liteMode(true)
        },
        modifier = Modifier.height(200.dp),
        cameraPositionState = cameraPositionState
    ) {
//
        for (fragment in rute.value.fragmentList) {
            Polyline(fragment!!, color = RouteUtils.routeColor(rute.value.id))
        }
    }
}

@Composable
fun AirQualIcon(aqi:Double?){
    var color : Color
    if (aqi != null) {
        if (aqi > 2.5){ //red
            color = Color(red = 1f, green = 0f, blue = 0f, alpha = 1f)
        }
        else if (aqi <2){ //green
            color = Color(red = 0f, green = 1f, blue = 0f, alpha = 1f)
        }
        else { //yellow
            color = Color(red = 1f, green = 1f, blue = 0f, alpha = 1f)

            //To fade between colors one could implement a function based on aqi-value e.g.:
            //color = Color(red = 1f*aqi.toFloat(), green = 1f*((2-aqi.toFloat())*2), blue = 0f, alpha = 1f)


        }
    } else {
        color = Color(red = 0f, green = 0f, blue = 0f, alpha = 0f)

    }
    Row(){
        Text(
            text = "Luftkvalitet: $aqi ",
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 12.dp),
            style = MaterialTheme.typography.body1
        )
        Canvas(modifier = Modifier.size(10.dp) .align(Alignment.CenterVertically), onDraw = {
            drawCircle(color = color)
        })

    }

}



@Composable
fun customSpinner (choices: MutableList<String>):String{
    var valg: String by remember { mutableStateOf(choices[0]) }
    var expanded by remember { mutableStateOf(false)}

    Box(Modifier.fillMaxWidth(),contentAlignment = Alignment.Center) {
        Row( modifier = Modifier
            .clickable {
                expanded = !expanded
            }
            .padding(2.dp)
            .border(1.dp, Color.LightGray, RectangleShape)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                buildAnnotatedString {
                    append("Sorter etter:  ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(valg)
                    }
                },
                Modifier
                    .padding(all =5.dp))
            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "")
        }
        DropdownMenu(
            expanded = expanded,
            modifier =
            Modifier
                .fillMaxWidth(),
            onDismissRequest = {
                expanded = false
            }
        ) {
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
    return valg
}