package com.example.in2000team5.utils

import com.example.in2000team5.domain_layer.WeatherDataViewModel

class supportInfo {
    companion object {

        fun getWeatherDetailsInfo(model: WeatherDataViewModel, start: Int, end: Int): String? {
            var str = model.weaterTimes[start].data?.next_1_hours?.details?.precipitation_amount.toString() + " mm regn neste time \r\n"
            str += model.weaterTimes[start].data?.instant?.details?.wind_speed.toString() + "m/s "
            return str
        }

        fun getRecommendedClothing(model: WeatherDataViewModel, start: Int, end: Int): String? {
            var str = "Hjelm!";
            val rain = model.weaterTimes[start].data?.next_1_hours?.details?.precipitation_amount?.toDouble()
            if (rain != null) {
                if (rain > 1) {
                    str += "\r\n Regnjakke \r\nRegnbukse"
                } else if (rain > 0) {
                    str += "\r\n Vannavstøtende jakke"
                } else {
                    //str += "Nebør usansynlig, anbefaler ikke regntøy"
                }

            }

            val temp = model.weaterTimes[start].data?.instant?.details?.air_temperature?.toDouble()
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

            val sky = model.weaterTimes[start].data?.instant?.details?.cloud_area_fraction?.toDouble()
            if (sky != null) {
                if (sky < 0.0) {
                    str += "\r\nSolbriller \r\nSolkrem"
                }
            }
            return str
        }

        fun getBikeConditions(model: WeatherDataViewModel, start: Int, end: Int): String? {
            val temp = model.weaterTimes[start].data?.instant?.details?.air_temperature?.toDouble()
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