package com.example.in2000team5.ui_layer.compose_elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.in2000team5.R
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel

//The app's top-bar, showing a weather-icon based on the API-response, the current temperature and expected rain the current hour.
@Composable
fun WeatherInformationTopBar(model: WeatherDataViewModel) {
    TopAppBar(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .semantics { heading() },
        backgroundColor = MaterialTheme.colors.background) {

        Row (Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceEvenly){

            val context = LocalContext.current
            val id = if (model.weatherTimes.value.currentWeatherSymbol.value == null){
                R.drawable.unknown
            } else{
                context.resources.getIdentifier(model.weatherTimes.value.currentWeatherSymbol.value, "drawable",context.packageName )
            }
            Image(
                painter = painterResource(id = id),
                contentDescription = "en sol",
                Modifier
                    .size(70.dp)
                    .padding(3.dp)
            )
            Text(
                text = "${model.weatherTimes.value.currentTemperature.value}°C",
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(horizontal = 6.dp)
            )
            Column(
                modifier = Modifier.align(alignment = Alignment.CenterVertically)
            ) {
                Text(
                    text = "Regn neste time:",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
                Text(
                    text = "${model.weatherTimes.value.currentRainNextHour.value} mm",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                )
            }
        }
    }
}
