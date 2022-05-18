package com.example.in2000team5.utils

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.in2000team5.data_layer.repository.WeatherTimesData


class SupportInformation {
    companion object {

        //Analyse weather data for period and returns a list of clothing suggestion
        fun getRecommendedClothing(weatherTimesData: WeatherTimesData, cloatingList: SnapshotStateList<String>){
            cloatingList.clear()

            val minTemperature = weatherTimesData.minTemperature.value

            if(weatherTimesData.isRaining.value){

                if(weatherTimesData.maxRain.value > 1.0){
                    cloatingList.add("Regnjakke")
                    cloatingList.add("Regnbukse")
                }
                else{
                    cloatingList.add("Vannavstøtende jakke")
                }

                if (minTemperature < 10){
                    cloatingList.add("Varmt undertøy")
                }
            }

            else{
                when{
                    minTemperature <= 0 -> cloatingList.add("Jakke") && cloatingList.add("Varm bukse")
                    (minTemperature > 0 && minTemperature <= 10 ) -> cloatingList.add("Tynn jakke")&& cloatingList.add("Lang bukse")
                    (minTemperature > 10 && minTemperature <= 20 ) -> cloatingList.add("Tynn jakke/Langarmet")&& cloatingList.add("Lang bukse")
                    (minTemperature > 20) -> cloatingList.add("T-shorte")&&(minTemperature > 20) && cloatingList.add("Shorts")
                }
            }

            when{
                minTemperature <= 0 -> cloatingList.add("Buff") && cloatingList.add("Hansker") && cloatingList.add("Lange sokker")&& cloatingList.add("Ørevarmer")
                (minTemperature > 0 && minTemperature <= 10 ) -> cloatingList.add("Buff") && cloatingList.add("Hansker")
                (minTemperature > 10 && minTemperature <= 15 ) -> cloatingList.add("Hansker")
            }
        }

        //Analyse weather data for period and returns a list of bike equipment suggestion
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



