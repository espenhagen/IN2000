package com.example.in2000team5.utils

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.in2000team5.data_layer.repository.WeatherTimesData


class SupportInfo {
    companion object {
        //Analyse weather data for period and returns a suggestion on clothing
        fun getRecommendedClothing(weatherTimesData: WeatherTimesData, cloatingList: SnapshotStateList<String>){
            cloatingList.clear()

            if(weatherTimesData.isRaining.value){

                if(weatherTimesData.maxRain.value > 1.0){
                    cloatingList.add("Regnjakke")
                    cloatingList.add("Regnbukse")
                }
                else{
                    cloatingList.add("Vannavstøtende jakke")
                }

                if (weatherTimesData.minTemperature.value < 10){
                    cloatingList.add("Varmt undertøy")
                }
            }

            else{
                val avgTemp = weatherTimesData.minTemperature.value
                when{
                    avgTemp <= 0 -> cloatingList.add("Jakke") && cloatingList.add("Varm bukse")
                    (avgTemp > 0 && avgTemp <= 10 ) -> cloatingList.add("Tynn jakke")&& cloatingList.add("Lang bukse")
                    (avgTemp > 10 && avgTemp <= 20 ) -> cloatingList.add("Tynn jakke/Langarmet")&& cloatingList.add("Lang bukse")
                    (avgTemp > 20) -> cloatingList.add("T-shorte")&&(avgTemp > 20) && cloatingList.add("Shorts")
                }
            }

            val avgTemp = weatherTimesData.minTemperature.value
            when{
                avgTemp <= 0 -> cloatingList.add("Buff") && cloatingList.add("Hansker") && cloatingList.add("Lange sokker")&& cloatingList.add("Ørevarmer")
                (avgTemp > 0 && avgTemp <= 10 ) -> cloatingList.add("Buff") && cloatingList.add("Hansker")
                (avgTemp > 10 && avgTemp <= 15 ) -> cloatingList.add("Hansker")
            }
        }

        //Analyse weather data for period and returns a suggestion on bike equipment
        fun getRecommendedItems(weatherTimesData: WeatherTimesData, itemList: SnapshotStateList<String>){
            itemList.clear()
            itemList.add("Hjelm!")

            if(weatherTimesData.numberOfHours.value > 2){
                itemList.add("Drikke")
            }
            if(weatherTimesData.numberOfHours.value > 3){
                itemList.add("Næring")
            }
            if(weatherTimesData.isSuncreamRecommended.value){
                itemList.add("Solkrem")
                itemList.add("Solbriller")
            }

            if(weatherTimesData.isRaining.value){
                itemList.add("Briller")
            }

            if(weatherTimesData.minTemperature.value < 0){
                itemList.add("Piggdekk")
            }

            if(weatherTimesData.isDark.value){
                itemList.add("Refleks")
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



