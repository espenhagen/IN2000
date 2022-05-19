package com.example.in2000team5.ui_layer.compose_elements


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
import com.example.in2000team5.utils.SupportInformation


//Dynamic lists depending on the slider position
val clothingSupportList = mutableStateListOf<String>()
val itemSupportList = mutableStateListOf<String>()


//Is the base function for the information screen
@Composable
fun InformationScreenBase(weatherDataViewModel: WeatherDataViewModel) {

    Surface(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = MaterialTheme.colors.background
    ) {
        Column {

            TimeSlide(weatherDataViewModel)

            LazyColumn{
                item{
                    WeatherDetailsBox(weatherDataViewModel)
                }
                item{
                    ClothingSupportBox()
                }
                item{
                    ChecklistBox()
                }
                item{
                    InformationBox()
                }
            }
        }
    }
}

//Updates the weather details and suggestion box depending on the slider position
fun updateSupportData(weatherDataViewModel: WeatherDataViewModel, sliderPosition: ClosedFloatingPointRange<Float>) {
    val start = sliderPosition.start.toInt()
    val end = sliderPosition.endInclusive.toInt() + 2

    weatherDataViewModel.weatherTimes.value.updateSliderData(start,end)
    SupportInformation.getRecommendedClothing(weatherDataViewModel.weatherTimes.value, clothingSupportList)
    SupportInformation.getRecommendedItems(weatherDataViewModel.weatherTimes.value, itemSupportList)
}

//Slider to make a time interval for weather details and suggestion box
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

//Shows the box for weather details
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
            Column {
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
                if (model.weatherTimes.value.isDark.value || model.weatherTimes.value.isSuncreamRecommended.value || model.weatherTimes.value.isSlippery.value) {
                    if (model.weatherTimes.value.isDark.value) {
                        Text(
                            text = "Det kan være mørkt i løpet av turen!",
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Yellow)
                                .padding(4.dp)
                        )
                    }
                    if (model.weatherTimes.value.isSuncreamRecommended.value) {
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

//Shows the box for cloathing support
@Composable
fun ClothingSupportBox() {
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
                clothingSupportList.forEach {
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

//Shows a checklist for simple bike preparation
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
            SupportInformation.getChecklist().forEach {
                Text(text = it, modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
fun InformationBox() {
    Column(
        Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .border(5.dp, Color.DarkGray, shape = RoundedCornerShape(10.dp))
            .padding(5.dp)
            .background(MaterialTheme.colors.surface)

    ){
        Text(
            text = "Informasjon",
            color = MaterialTheme.colors.secondaryVariant,
            style = MaterialTheme.typography.h5,
            modifier = Modifier
                .padding(4.dp)
                .align(alignment = Alignment.CenterHorizontally)

        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)){
            Text(text = "Vår app henter værdata fra metrologisk insitutt og sykkelruter fra Oslo kommune. ",
                modifier = Modifier
                    .padding(15.dp)
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}




