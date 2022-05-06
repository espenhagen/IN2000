package com.example.in2000team5.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.in2000team5.data_layer.datasource.Timeseries
import com.example.in2000team5.utils.GeneralUtils.Companion.round

// TODO: refaktorering etter ferdig implementert
class WeatherDetails{
    var maxTemperature: MutableState<Double?> = mutableStateOf(null)
    var minTemperature: MutableState<Double> = mutableStateOf(0.0)
    var averageTemperature: MutableState<Double?> = mutableStateOf(null)
    var currentTemperature: MutableState<Double?> = mutableStateOf(null)
    var maxRain: MutableState<Double> = mutableStateOf(0.0)
    var totalRain: MutableState<Double> = mutableStateOf(0.0)
    var maxWind: MutableState<Double?> = mutableStateOf(null)
    var averageWind: MutableState<Double?> = mutableStateOf(null)
    var windDirection: MutableState<Float> = mutableStateOf(0f)
    var isSuncremRecomended: MutableState<Boolean> = mutableStateOf(false)
    var isDark: MutableState<Boolean> = mutableStateOf(false)
    var isSlippery: MutableState<Boolean> = mutableStateOf(false)
    var isRaining: MutableState<Boolean> = mutableStateOf(false)
    var numberOfHours: MutableState<Int> = mutableStateOf(0)

    fun update(subList: MutableList<Timeseries>){
        maxTemperature.value        = getMaxTemperature(subList)
        minTemperature.value        = getMinTemperature(subList)
        averageTemperature.value    = getAverageTemperature(subList)
        maxRain.value               = getMaxRain(subList)
        totalRain.value             = getTotalRain(subList)
        maxWind.value               = getMaxWind(subList)
        averageWind.value           = getAverageWind(subList)
        windDirection.value         = getWindDirection(subList).toFloat()
        isSuncremRecomended.value   = findOutIfSuncreamIsRecomended(subList)
        isDark.value                = findOutIfItCouldBeDark(subList)
        isSlippery.value            = findOutIfSlippery(subList)
        isRaining.value             = findOutIfRaining(subList)
        numberOfHours.value         = findNumberOfHours(subList)
        currentTemperature.value    = getCurrentTemperature(subList)
    }

    private fun findNumberOfHours(subList: MutableList<Timeseries>) : Int {
        return subList.size
    }

    private fun findOutIfRaining(subList: MutableList<Timeseries>): Boolean {
        val rain = getMaxRain(subList)
        if(rain > 0.2){
            return true
        }
        return false
    }

    private fun getMaxTemperature(subList: MutableList<Timeseries>): Double? {
        return subList.maxOf { it.data?.instant?.details?.air_temperature?.toDouble() ?: return null}
    }

    private fun getCurrentTemperature(subList: MutableList<Timeseries>): Double? {
        if(subList.size>0){
            return subList.first().data?.next_1_hours?.details?.precipitation_amount?.toDouble()
        }
        return null;
    }

    private fun getMinTemperature(subList: MutableList<Timeseries>): Double {
        return subList.minOf { it.data?.instant?.details?.air_temperature?.toDouble() ?: return 0.0}
    }

    private fun getAverageTemperature(subList: MutableList<Timeseries>): Double? {
        return subList.sumOf { it.data?.instant?.details?.air_temperature?.toDouble()  ?: return null}.div(subList.size).round(1)
    }

    private fun getMaxRain(subList: MutableList<Timeseries>): Double {
        return subList.maxOf { it.data?.next_1_hours?.details?.precipitation_amount?.toDouble() ?: return 0.0}.round(1)
    }

    private fun getTotalRain(subList: MutableList<Timeseries>): Double {
        return subList.sumOf { it.data?.next_1_hours?.details?.precipitation_amount?.toDouble() ?: return 0.0}.round(1)
    }

    private fun getMaxWind(subList: MutableList<Timeseries>): Double? {
        return subList.maxOf { it.data?.instant?.details?.wind_speed?.toDouble() ?: return null}.round(1)
    }

    private fun getAverageWind(subList: MutableList<Timeseries>): Double? {
        return subList.sumOf { it.data?.instant?.details?.wind_speed?.toDouble() ?: return null}.div(subList.size).round(1)
    }

    private fun getWindDirection(subList: MutableList<Timeseries>): Double {
        return subList.sumOf { it.data?.instant?.details?.wind_from_direction?.toDouble() ?: return 0.0}.div(subList.size).round(2)
    }

    private fun findOutIfSuncreamIsRecomended(subList: MutableList<Timeseries>): Boolean {
        val averageCloudFraction = subList.sumOf { it.data?.instant?.details?.cloud_area_fraction?.toDouble() ?: return false}.div(subList.size).round(2)
        if(averageCloudFraction < 30 && subList.size > 3 && !findOutIfItCouldBeDark(subList)){
            return true
        }
        return false
    }

    private fun findOutIfItCouldBeDark(subList: MutableList<Timeseries>): Boolean {
        subList.forEach {
            if(it.data?.next_1_hours?.summary?.symbol_code?.contains("night") == true)
                return true
        }
        return false
    }

    private fun findOutIfSlippery(subList: MutableList<Timeseries>): Boolean {
        val tempraturAtLowest = getMaxTemperature(subList)
        if(tempraturAtLowest!= null){
            if(tempraturAtLowest <= 4){
                return true
            }
        }
        return false
    }
}

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

