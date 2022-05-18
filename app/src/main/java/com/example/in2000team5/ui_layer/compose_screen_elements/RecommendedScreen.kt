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
import com.example.in2000team5.utils.MetUtils.Companion.getWindDirectionDescription
import com.example.in2000team5.utils.SupportInfo


val cloatingSupportList = mutableStateListOf<String>()
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

            TimeSlide(model)

            LazyColumn{

                item{
                    WeatherDetailsBox(model)
                }
                item{
                    CloatingSupportBox()
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
    //model.weatherTimes.value.updateList(model.weatherTimes)
    val start = sliderPosition.start.toInt()
    val end = sliderPosition.endInclusive.toInt() +2

    model.weatherTimes.value.updateSliderData(start,end)
    SupportInfo.getRecommendedClothing(model.weatherTimes.value, cloatingSupportList)
    SupportInfo.getRecommendedItems(model.weatherTimes.value, itemSupportList)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimeSlide(model: WeatherDataViewModel) {
    var sliderPosition by remember { mutableStateOf(0f..2f) }
    updateSupportData(model, sliderPosition)
    Column(modifier = Modifier.padding(20.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = "Fra " + MetUtils.getDateAndHour(model.weatherTimes.value.getTimeOfIndex(sliderPosition.start.toInt())))
            Text(text = "Til " + MetUtils.getDateAndHour(model.weatherTimes.value.getTimeOfIndex(sliderPosition.endInclusive.toInt()+1)))
        }

        RangeSlider(
            values = sliderPosition,
            onValueChange = { sliderPosition = it },
            valueRange = 0f..(model.weatherTimes.value.getSizeOfList()).toFloat(),
            onValueChangeFinished = {
                updateSupportData(model, sliderPosition)
            }
        )
    }
}

@Composable
fun WeatherDetailsBox(model: WeatherDataViewModel) {
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
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(10.dp)
                ){
            Text(
                text = "Temp: ",
                style = MaterialTheme.typography.h5,
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(horizontal = 3.dp)

            )
            Column(modifier = Modifier
                .align(alignment = Alignment.CenterVertically)
            ) {
                Text(text = "${model.weatherTimes.value.minTemperature.value}°C min")
                Text(text = "${model.weatherTimes.value.maxTemperature.value}°C max")
                Text(text = "${model.weatherTimes.value.averageTemperature.value}°C snitt")

            }


            Image(
                painter = painterResource(R.drawable.temperature),
                contentDescription = "Temperatur",
                Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .size(40.dp)
            )
        }
        Row (
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
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
                Text(text = "${model.weatherTimes.value.totalRain.value} mm totalt")
                Text(text = "${model.weatherTimes.value.maxRain.value} mm/h max")
            }
            Image(
                painter = painterResource(R.drawable.raindrop),
                contentDescription = "Nedbør",
                Modifier
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .size(40.dp)
            )
        }

        Row (
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(10.dp)
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
                Text(text = "${model.weatherTimes.value.maxWind.value} m/s max")
                Text(text = "${model.weatherTimes.value.averageWind.value} m/s snitt")
            }
            Column(){
                Image(
                    painter = painterResource(R.drawable.wind_arrow),
                    contentDescription = "Vindretning",
                    Modifier
                        .rotate(model.weatherTimes.value.windDirection.value)
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                        .size(40.dp)
                )
                Text(
                    text = getWindDirectionDescription(model.weatherTimes.value.windDirection.value),
                    Modifier.align(alignment = Alignment.CenterHorizontally)
                )
            }

        }


            Column {
                if (model.weatherTimes.value.isDark.value || model.weatherTimes.value.isSuncremRecomended.value || model.weatherTimes.value.isSlippery.value) {
                    if (model.weatherTimes.value.isDark.value) {
                        Text(
                            text = "Det kan være mørkt i løpet av turen!",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Yellow)
                                .padding(4.dp)
                        )
                    }
                    if (model.weatherTimes.value.isSuncremRecomended.value) {
                        Text(
                            text = "Anbefaler solkrem!",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Yellow)
                                .padding(4.dp)
                        )

                    }
                    if (model.weatherTimes.value.isSlippery.value) {
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
fun CloatingSupportBox() {
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
                cloatingSupportList.forEach {
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




