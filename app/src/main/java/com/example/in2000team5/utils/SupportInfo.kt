package com.example.in2000team5.utils

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.in2000team5.data_layer.repository.WeatherTimeDetails
import com.example.in2000team5.utils.GeneralUtils.Companion.round



class SupportInfo {
    companion object {

        fun getRecommendedClothing2(timeSliderData: TimeSliderData, cloatingList: SnapshotStateList<String>){
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


    class TimeSliderData{

        var timeList = listOf<WeatherTimeDetails>()

        //Timeintervall values
        val maxTemperature: MutableState<Double?> = mutableStateOf(null)
        val minTemperature: MutableState<Double> = mutableStateOf(0.0)
        val averageTemperature: MutableState<Double?> = mutableStateOf(null)
        val maxRain: MutableState<Double> = mutableStateOf(0.0)
        val totalRain: MutableState<Double> = mutableStateOf(0.0)
        val maxWind: MutableState<Double?> = mutableStateOf(null)
        val averageWind: MutableState<Double?> = mutableStateOf(null)
        val windDirection: MutableState<Float> = mutableStateOf(0f)
        val isSuncremRecomended: MutableState<Boolean> = mutableStateOf(false)
        val isDark: MutableState<Boolean> = mutableStateOf(false)
        val isSlippery: MutableState<Boolean> = mutableStateOf(false)
        val isRaining: MutableState<Boolean> = mutableStateOf(false)
        val numberOfHours: MutableState<Int> = mutableStateOf(0)

        fun update(start : Int, end : Int){
            if(timeList.isEmpty())return
            if(timeList.size < end)return

            val subList = timeList.subList(start, end)
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
        }

        fun updateList(list: List<WeatherTimeDetails>){
            timeList = list
        }

        private fun findNumberOfHours(subList: List<WeatherTimeDetails>) : Int {
            return subList.size
        }

        private fun findOutIfRaining(subList: List<WeatherTimeDetails>): Boolean {
            val rain = getMaxRain(subList)
            if(rain > 0.2){
                return true
            }
            return false
        }

        private fun getMaxTemperature(subList: List<WeatherTimeDetails>): Double? {
            return subList.maxOf { it.currentTemperature.value ?: return null}
        }

        private fun getMinTemperature(subList: List<WeatherTimeDetails>): Double {
            return subList.minOf { it.currentTemperature.value ?: return 0.0}
        }

        private fun getAverageTemperature(subList: List<WeatherTimeDetails>): Double? {
            return subList.sumOf { it.currentTemperature.value  ?: return null}.div(subList.size).round(1)
        }

        private fun getMaxRain(subList: List<WeatherTimeDetails>): Double {
            return subList.maxOf { it.rainNextHour.value ?: return 0.0}.round(1)
        }

        private fun getTotalRain(subList: List<WeatherTimeDetails>): Double {
            return subList.sumOf { it.rainNextHour.value ?: return 0.0}.round(1)
        }

        private fun getMaxWind(subList: List<WeatherTimeDetails>): Double? {
            return subList.maxOf { it.windAmount.value ?: return null}.round(1)
        }

        private fun getAverageWind(subList: List<WeatherTimeDetails>): Double? {
            return subList.sumOf { it.windAmount.value ?: return null}.div(subList.size).round(1)
        }

        private fun getWindDirection(subList: List<WeatherTimeDetails>): Double {
            return subList.sumOf { it.windDirection.value ?: return 0.0}.div(subList.size).round(2)
        }

        private fun findOutIfSuncreamIsRecomended(subList: List<WeatherTimeDetails>): Boolean {
            val averageCloudFraction = subList.sumOf { it.precipitationAmount.value ?: return false}.div(subList.size).round(2)
            if(averageCloudFraction < 30 && subList.size > 3 && !findOutIfItCouldBeDark(subList)){
                return true
            }
            return false
        }

        private fun findOutIfItCouldBeDark(subList: List<WeatherTimeDetails>): Boolean {
            subList.forEach {
                if(it.currentWeatherSymbol.value?.contains("night") == true)
                    return true
            }
            return false
        }

        private fun findOutIfSlippery(subList: List<WeatherTimeDetails>): Boolean {
            val tempraturAtLowest = getMaxTemperature(subList)
            if(tempraturAtLowest!= null){
                if(tempraturAtLowest <= 4){
                    return true
                }
            }
            return false
        }

        fun getSizeOfList(): Int{
            return if(timeList.isEmpty()){
                0
            } else{
                timeList.size - 2
            }
        }

        fun getTimeOfIndex(index: Int): String?{
            if(timeList.isEmpty()) return null
            return timeList[index].timeAsString.value
        }
    }
}



