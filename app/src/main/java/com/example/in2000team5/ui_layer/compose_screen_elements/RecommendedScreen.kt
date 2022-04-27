package com.example.in2000team5.ui_layer.compose_screen_elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.*
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
    val end = sliderPosition.endInclusive.toInt() -1
    wDetails.value = SupportInfo.getWeatherDetailsInfo(model, start, end)
    rClothing.value = SupportInfo.getRecommendedClothing(model, start, end)
    conditions.value = SupportInfo.getBikeConditions(model, start, end)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TimeSlide(model: WeatherDataViewModel) {
    var sliderPosition by remember { mutableStateOf(0f..(model.weatherTimes.lastIndex).toFloat()) }
    updateSupportData(model, sliderPosition)
    Text(text = "Fra: " + MetUtils.getDateAndHour(model.weatherTimes[sliderPosition.start.toInt()].time.toString()) + "\r\nTil: " + MetUtils.getDateAndHour(model.weatherTimes[sliderPosition.endInclusive.toInt()].time.toString())  )
    RangeSlider(
        values = sliderPosition,
        onValueChange = { sliderPosition = it },
        valueRange = 0f..model.weatherTimes.lastIndex.toFloat(),
        steps = model.weatherTimes.size,
        onValueChangeFinished = {
            updateSupportData(model, sliderPosition)
        },
    )
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
            /*
            Text(
                text = "Planlegg Dagen:",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h3,
                textDecoration = TextDecoration.Underline
            )
            */

            if(model.weatherTimes.size!=0){
                TimeSlide(model)
            }

            checklist.value = SupportInfo.getChecklist()

            InfoBox(model = model, "Detaljert om været:", wDetails, Color.LightGray)
            InfoBox(model = model, "Anbefalt påkledning:", rClothing, Color.White)
            InfoBox(model = model, "Sykkelforhold:", conditions, Color.LightGray)
            InfoBox(model = model, "Vedlikehold checklist:", checklist, Color.White)
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