package com.example.in2000team5.utils

import com.example.in2000team5.data_layer.datasource.Timeseries
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel
import com.example.in2000team5.utils.GeneralUtils.Companion.round

// TODO: refaktorering etter ferdig implementert
class SupportInfo {
    companion object {

        fun getWeatherDetailsInfo(subList: MutableList<Timeseries>): String? {

            val startObj = subList.first()
            val endObj = subList.last()

            val maxTemp = getMaxTemperature(subList)
            val minTemp = getMinTemperature(subList)

            //var str = model.weatherTimes[start].data?.next_1_hours?.details?.precipitation_amount.toString() + " mm regn neste time \r\n"
            //str += model.weatherTimes[start].data?.instant?.details?.wind_speed.toString() + "m/s "

            var isSlippery = "";
            if(getMinTemperature(subList)!=null){
                if(getMinTemperature(subList)!!<=4){
                    isSlippery = "!Det kan være glatt!"
                }
            }


            return "Max temp: " + maxTemp.toString() +
                    "\r\nMin temp: " + minTemp.toString() +
                    "\r\nGjen temp: " + getAverageTemperature(subList).toString() +
                    "\r\nMax nedbør pr time: " + getMaxRain(subList).toString() +
                    "\r\nTotal nedbør: " + getTotalRain(subList).toString() +
                    "\r\nVind fart max: " + getMaxWind(subList).toString() +
                    "\r\nVind fart gjen: " + getAverageWind(subList).toString() +
                    "\r\nVind retning: " + getWindDirection(subList).toString() +
                    isSlippery
        }

        fun getMaxTemperature(subList: MutableList<Timeseries>): Double? {
            return subList.maxOf { it.data?.instant?.details?.air_temperature?.toDouble() ?: return null}
        }
        fun getMinTemperature(subList: MutableList<Timeseries>): Double? {
            return subList.minOf { it.data?.instant?.details?.air_temperature?.toDouble() ?: return null}
        }

        fun getAverageTemperature(subList: MutableList<Timeseries>): Double? {
            return subList.sumOf { it.data?.instant?.details?.air_temperature?.toDouble()  ?: return null}.div(subList.size).round(1)
        }

        fun getMaxRain(subList: MutableList<Timeseries>): Double? {
            return subList.maxOf { it.data?.next_1_hours?.details?.precipitation_amount?.toDouble() ?: return null}.round(1)
        }

        fun getTotalRain(subList: MutableList<Timeseries>): Double? {
            return subList.sumOf { it.data?.next_1_hours?.details?.precipitation_amount?.toDouble() ?: return null}.round(1)
        }

        fun getMaxWind(subList: MutableList<Timeseries>): Double? {
            return subList.maxOf { it.data?.instant?.details?.wind_speed?.toDouble() ?: return null}.round(1)
        }

        fun getAverageWind(subList: MutableList<Timeseries>): Double? {
            return subList.sumOf { it.data?.instant?.details?.wind_speed?.toDouble() ?: return null}.div(subList.size).round(1)
        }

        fun getWindDirection(subList: MutableList<Timeseries>): Double? {
            return subList.sumOf { it.data?.instant?.details?.wind_from_direction?.toDouble() ?: return null}.div(subList.size).round(2)
        }



        fun getRecommendedClothing(model: WeatherDataViewModel, start: Int, end: Int): String? {
            var str = "Hjelm!";
            val rain = model.weatherTimes[start].data?.next_1_hours?.details?.precipitation_amount?.toDouble()
            if (rain != null) {
                if (rain > 1) {
                    str += "\r\n Regnjakke \r\nRegnbukse"
                } else if (rain > 0) {
                    str += "\r\n Vannavstøtende jakke"
                } else {
                    //str += "Nebør usansynlig, anbefaler ikke regntøy"
                }

            }

            val temp = model.weatherTimes[start].data?.instant?.details?.air_temperature?.toDouble()
            if (temp != null) {
                if (temp < 0) {
                    str += "\r\nJakke \r\nLang bukse \r\nHansker, \r\nBuff/Ørevarmer \r\nVintersko/Tykke sokker \r\nBriller"
                } else if (temp < 10) {
                    str += "\r\nTynn Jakke \r\nLang bukse \r\nHansker, \r\nBuff/Ørevarmer "
                } else if (temp < 20) {
                    str += "\r\nLangarmet/Tynn Jakke \r\nTynn bukse"
                } else {
                    str += "\r\nT-shorte/Tynn langarmet \r\nShorts/tynn bukse"
                }

            }

            val sky = model.weatherTimes[start].data?.instant?.details?.cloud_area_fraction?.toDouble()
            if (sky != null) {
                if (sky < 0.0) {
                    str += "\r\nSolbriller \r\nSolkrem"
                }
            }
            return str
        }

        fun getBikeConditions(model: WeatherDataViewModel, start: Int, end: Int): String? {
            val temp = model.weatherTimes[start].data?.instant?.details?.air_temperature?.toDouble()
            var str = ""
            if(temp != null){
                if(temp<=4){
                    str += "Kan være glatt på veien i dag\r\n"
                }
                else if (temp>=15){
                    str += "Mest sannsynlig gode forhold på veien\r\n"
                }
            }
            str += "Kanskje det er mørkt?"

            return str
        }

        fun getChecklist(): String? {
            val str = "Luft i dekkene \r\nStyreskruen er godt strammet \r\nHjulskrune sitter godt, \r\nKjedet er oljet \r\nDekkene har ingen sprekker"
            return str
        }
    }
}