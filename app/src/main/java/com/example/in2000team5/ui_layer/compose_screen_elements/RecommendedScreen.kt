package com.example.in2000team5.ui_layer.compose_screen_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel
import com.example.in2000team5.utils.MetUtils
import com.example.in2000team5.utils.SupportInfo

val wDetails = mutableStateOf<String?>(null)
val rClothing = mutableStateOf<String?>(null)
val conditions = mutableStateOf<String?>(null)
val checklist = mutableStateOf<String?>(null)

fun updateSupportData(
    model: WeatherDataViewModel,
    sliderPosition: ClosedFloatingPointRange<Float>
) {
    val start = sliderPosition.start.toInt()
    val end = sliderPosition.endInclusive.toInt()
    wDetails.value = SupportInfo.getWeatherDetailsInfo(model, start, end)
    rClothing.value = SupportInfo.getRecommendedClothing(model, start, end)
    conditions.value = SupportInfo.getBikeConditions(model, start, end)
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
            steps = 0,
            onValueChangeFinished = {
                updateSupportData(model, sliderPosition)
            }
        )
    }

}

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
            if(model.weatherTimes.size!=0){
                TimeSlide(model)
            }

            LazyColumn{
                item{
                    InfoBox(model = model, "Detaljert om været:", wDetails, Color.LightGray)
                }
                item {
                    InfoBox(model = model, "Anbefalt påkledning:", rClothing, Color.White)
                }
                item {
                    InfoBox(model = model, "Sykkelforhold:", conditions, Color.LightGray)
                }

                item {
                    checklist.value = SupportInfo.getChecklist()
                    InfoBox(model = model, "Vedlikehold checklist:", checklist, Color.White)
                }

                item {
                    checklist.value = SupportInfo.getChecklist()
                    InfoBox(model = model, "Vedlikehold checklist:", checklist, Color.White)
                }
                item {
                    checklist.value = SupportInfo.getChecklist()
                    InfoBox(model = model, "Vedlikehold checklist:", checklist, Color.White)
                }
                item {
                    checklist.value = SupportInfo.getChecklist()
                    InfoBox(model = model, "Vedlikehold checklist:", checklist, Color.White)
                }
                item {
                    checklist.value = SupportInfo.getChecklist()
                    InfoBox(model = model, "Vedlikehold checklist:", checklist, Color.White)
                }
                item {
                    checklist.value = SupportInfo.getChecklist()
                    InfoBox(model = model, "Vedlikehold checklist:", checklist, Color.White)
                }
                item {
                    checklist.value = SupportInfo.getChecklist()
                    InfoBox(model = model, "Vedlikehold checklist:", checklist, Color.White)
                }
                item {
                    checklist.value = SupportInfo.getChecklist()
                    InfoBox(model = model, "Vedlikehold checklist:", checklist, Color.White)
                }
            }
        }
    }
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