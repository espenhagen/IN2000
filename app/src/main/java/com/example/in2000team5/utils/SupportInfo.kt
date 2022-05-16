package com.example.in2000team5.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.in2000team5.data_layer.repository.TimeSliderData
import com.example.in2000team5.data_layer.repository.WeatherTimeDetails
import com.example.in2000team5.utils.GeneralUtils.Companion.round



class SupportInfo {
    companion object {
        //Analyse weather data for period and returns a suggestion on clothing
        fun getRecommendedClothing(timeSliderData: TimeSliderData, cloatingList: SnapshotStateList<String>){
            cloatingList.clear()

            if(timeSliderData.isRaining.value){

                if(timeSliderData.maxRain.value > 1.0){
                    cloatingList.add("Regnjakke")
                    cloatingList.add("Regnbukse")
                }
                else{
                    cloatingList.add("Vannavstøtende jakke")
                }

                if (timeSliderData.minTemperature.value < 10){
                    cloatingList.add("Varmt undertøy")
                }
            }

            else{
                val avgTemp = timeSliderData.minTemperature.value
                when{
                    avgTemp <= 0 -> cloatingList.add("Jakke") && cloatingList.add("Varm bukse")
                    (avgTemp > 0 && avgTemp <= 10 ) -> cloatingList.add("Tynn jakke")&& cloatingList.add("Lang bukse")
                    (avgTemp > 10 && avgTemp <= 20 ) -> cloatingList.add("Tynn jakke/Langarmet")&& cloatingList.add("Lang bukse")
                    (avgTemp > 20) -> cloatingList.add("T-shorte")&&(avgTemp > 20) && cloatingList.add("Shorts")
                }
            }

            val avgTemp = timeSliderData.minTemperature.value
            when{
                avgTemp <= 0 -> cloatingList.add("Buff") && cloatingList.add("Hansker") && cloatingList.add("Lange sokker")&& cloatingList.add("Ørevarmer")
                (avgTemp > 0 && avgTemp <= 10 ) -> cloatingList.add("Buff") && cloatingList.add("Hansker")
                (avgTemp > 10 && avgTemp <= 15 ) -> cloatingList.add("Hansker")
            }
        }

        //Analyse weather data for period and returns a suggestion on bike equipment
        fun getRecommendedItems(timeSliderData: TimeSliderData, itemList: SnapshotStateList<String>){
            itemList.clear()
            itemList.add("Hjelm!")

            if(timeSliderData.numberOfHours.value > 2){
                itemList.add("Drikke")
            }

            if(timeSliderData.isSuncremRecomended.value){
                itemList.add("Solkrem")
                itemList.add("Solbriller")
            }

            if(timeSliderData.isRaining.value){
                itemList.add("Briller")
            }

            if(timeSliderData.minTemperature.value < 0){
                itemList.add("Piggdekk")
            }
        }


        //Returns a simple checklist for bike preparation
        fun getChecklist(): List<String> {
            return listOf(
                "Luft i dekkene",
                "Styreskruen er godt strammet",
                "Hjulskrune sitter godt",
                "Kjedet er oljet",
                "Dekkene har ingen sprekker",
                )
        }

    }
}



