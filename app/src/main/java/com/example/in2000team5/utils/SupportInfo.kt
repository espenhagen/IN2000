package com.example.in2000team5.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.in2000team5.data_layer.datasource.Timeseries
import com.example.in2000team5.data_layer.repository.WeatherDetails
import com.example.in2000team5.utils.GeneralUtils.Companion.round



class SupportInfo {
    companion object {

        fun getRecommendedClothing2(weatherDetails: WeatherDetails, cloatingList: SnapshotStateList<String>){
            cloatingList.clear()

            if(weatherDetails.isRaining.value){

                if(weatherDetails.maxRain.value > 1.0){
                    cloatingList.add("Regnjakke")
                    cloatingList.add("Regnbukse")
                }
                else{
                    cloatingList.add("Vannavstøtende jakke")
                }

                if (weatherDetails.minTemperature.value < 10){
                    cloatingList.add("Varmt undertøy")
                }
            }

            else{
                val avgTemp = weatherDetails.minTemperature.value
                when{
                    avgTemp <= 0 -> cloatingList.add("Jakke") && cloatingList.add("Varm bukse")
                    (avgTemp > 0 && avgTemp <= 10 ) -> cloatingList.add("Tynn jakke")&& cloatingList.add("Lang bukse")
                    (avgTemp > 10 && avgTemp <= 20 ) -> cloatingList.add("Tynn jakke/Langarmet")&& cloatingList.add("Lang bukse")
                    (avgTemp > 20) -> cloatingList.add("T-shorte")&&(avgTemp > 20) && cloatingList.add("Shorts")
                }
            }

            val avgTemp = weatherDetails.minTemperature.value
            when{
                avgTemp <= 0 -> cloatingList.add("Buff") && cloatingList.add("Hansker") && cloatingList.add("Lange sokker")&& cloatingList.add("Ørevarmer")
                (avgTemp > 0 && avgTemp <= 10 ) -> cloatingList.add("Buff") && cloatingList.add("Hansker")
                (avgTemp > 10 && avgTemp <= 15 ) -> cloatingList.add("Hansker")
            }
        }

        fun getRecommendedItems(weatherDetails: WeatherDetails, itemList: SnapshotStateList<String>){
            itemList.clear()
            itemList.add("Hjelm!")

            if(weatherDetails.numberOfHours.value > 2){
                itemList.add("Drikke")
            }

            if(weatherDetails.isSuncremRecomended.value){
                itemList.add("Solkrem")
                itemList.add("Solbriller")
            }

            if(weatherDetails.isRaining.value){
                itemList.add("Briller")
            }

            if(weatherDetails.minTemperature.value < 0){
                itemList.add("Piggdekk")
            }
        }

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

