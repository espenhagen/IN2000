package com.example.in2000team5.ui_layer.compose_screen_elements


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.in2000team5.R
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel
import com.example.in2000team5.utils.MetUtils
import com.example.in2000team5.utils.SupportInfo
import com.example.in2000team5.utils.WeatherDetails

val weatherDetailsObject = WeatherDetails()
val cloathingSupportList = mutableStateListOf<String>()
val itemSupportList = mutableStateListOf<String>()
val checkList = SupportInfo.getChecklist()


@Composable
fun SupportScreen(model: WeatherDataViewModel) {

    Surface(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = 55.dp),

        color = MaterialTheme.colors.background
    ) {
        Column {
            if(model.weatherTimes.size!=0){
                TimeSlide(model)
            }

            LazyColumn{

                item{
                    WeatherDetailsBox()
                }
                item{
                    CloathingSupportBox()
                }
                item{
                    ChecklistBox()
                }
                item{
                    CreditBox()
                }
            }
        }
    }
}

fun updateSupportData(
    model: WeatherDataViewModel,
    sliderPosition: ClosedFloatingPointRange<Float>
) {

    val start = sliderPosition.start.toInt()
    val end = sliderPosition.endInclusive.toInt() +1
    weatherDetailsObject.update(model.weatherTimes.subList(start,end))
    SupportInfo.getRecommendedClothing2(weatherDetailsObject, cloathingSupportList)
    SupportInfo.getRecommendedItems(weatherDetailsObject, itemSupportList)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimeSlide(model: WeatherDataViewModel) {
    var sliderPosition by remember { mutableStateOf(0f..2f) }
    updateSupportData(model, sliderPosition)
    Column(modifier = Modifier.padding(20.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = "Fra " + MetUtils.getDateAndHour(model.weatherTimes[sliderPosition.start.toInt()].time.toString()))
            Text(text = "Til " + MetUtils.getDateAndHour(model.weatherTimes[sliderPosition.endInclusive.toInt()].time.toString()))
        }

        RangeSlider(
            values = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..model.weatherTimes.lastIndex.toFloat(),
            onValueChangeFinished = {
                updateSupportData(model, sliderPosition)
            }
        )
    }
}

@Composable
fun WeatherDetailsBox() {
    Column(
        Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .border(5.dp, Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(5.dp)
            .background(MaterialTheme.colors.surface)
    ){
        Text(
            text = "Vær og føre",
            color = MaterialTheme.colors.secondaryVariant,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(4.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Row (
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                ){
            Text(
                text = "Temperatur: ",
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(horizontal = 3.dp)

            )
            Column(modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
            ) {
                Text(text = "${weatherDetailsObject.minTemperature.value}°C min")
                Text(text = "${weatherDetailsObject.maxTemperature.value}°C max")
                Text(text = "${weatherDetailsObject.averageTemperature.value}°C snitt")

            }

            Image(
                painter = painterResource(R.drawable.unknown),
                contentDescription = "Bilde kommer",
                Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .size(40.dp)
            )
        }
        Row (
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Nedbør: ",
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(horizontal = 3.dp)

            )
            Column(modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
            ) {
                Text(text = "${weatherDetailsObject.totalRain.value} mm totalt")
                Text(text = "${weatherDetailsObject.maxRain.value} mm/h max")
            }
            Image(
                painter = painterResource(R.drawable.unknown),
                contentDescription = "Bilde kommer",
                Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .size(40.dp)
            )
        }

        Row (
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ){
            Text(
                text = "Vind: ",
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(horizontal = 3.dp)

            )
            Column(modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
            ) {
                Text(text = "${weatherDetailsObject.maxWind.value} m/s max")
                Text(text = "${weatherDetailsObject.averageWind.value} m/s snitt")
            }

            Image(
                painter = painterResource(R.drawable.wind_arrow),
                contentDescription = "Vindretning",
                Modifier
                    .rotate(weatherDetailsObject.windDirection.value)
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .size(40.dp)
            )
        }


            Column {
                if (weatherDetailsObject.isDark.value || weatherDetailsObject.isSuncremRecomended.value || weatherDetailsObject.isSlippery.value) {
                    if (weatherDetailsObject.isDark.value) {
                        Text(
                            text = "Det kan være mørkt i løpet av turen!",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Yellow)
                                .padding(4.dp)
                        )
                    }
                    if (weatherDetailsObject.isSuncremRecomended.value) {
                        Text(
                            text = "Anbefaler solkrem!",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Yellow)
                                .padding(4.dp)
                        )

                    }
                    if (weatherDetailsObject.isSlippery.value) {
                        Text(
                            text = "Det kan være glatt!",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Yellow)
                                .padding(4.dp)
                        )
                    }
            } else {
                Text(
                    text = "Ingen varsler",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)

                )
            }
        }
    }
}

@Composable
fun CloathingSupportBox() {
    Column(
        Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .border(5.dp, Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(5.dp)
            .background(MaterialTheme.colors.surface)

    ){
        Text(
            text = "Anbefalinger",
            color = MaterialTheme.colors.secondaryVariant,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(4.dp)
                .align(alignment = Alignment.CenterHorizontally)

        )
        Row (
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            Column(modifier = Modifier
                .background(Color.LightGray)
                .padding(20.dp)
            ){
                Text(text = "Klær:", textDecoration = TextDecoration.Underline)
                cloathingSupportList.forEach {
                    Text(text = it)
                }

            }

            Column(modifier = Modifier
                .background(Color.LightGray)
                .padding(20.dp)
            ){
                Text(text = "Utstyr:", textDecoration = TextDecoration.Underline)
                itemSupportList.forEach {
                    Text(text = it)
                }

            }

        }
    }
}

@Composable
fun ChecklistBox() {
    Column(
        Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .border(5.dp, Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(5.dp)
            .background(MaterialTheme.colors.surface)

    ){
        Text(
            text = "Vedlikehold sjekkliste",
            color = MaterialTheme.colors.secondaryVariant,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(4.dp)
                .align(alignment = Alignment.CenterHorizontally)

        )
        
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)){
            checkList.forEach {
                Text(text = it, modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
fun CreditBox() {
    Column(
        Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .border(5.dp, Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(5.dp)
            .background(MaterialTheme.colors.surface)

    ){
        Text(
            text = "Kreditering",
            color = MaterialTheme.colors.secondaryVariant,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(4.dp)
                .align(alignment = Alignment.CenterHorizontally)

        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)){
            for(i in 0..5){
                Text(text = "_______________", modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
            }
        }
    }
}



