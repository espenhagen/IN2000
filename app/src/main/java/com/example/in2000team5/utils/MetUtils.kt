package com.example.in2000team5.utils

import com.example.in2000team5.R
import java.util.*

class MetUtils {

    companion object {
        fun getCurrentTimeAsString() : String{

            //paddStart sørger for at verdier får 0 foran seg selvom verdien er under 10
            //Calender.XXXX gir en index hvor verdien finnes i listen

            val year = Calendar.getInstance()[Calendar.YEAR]
            //Verdien fra måender starter på 0 (Januar er 0)
            val month = Calendar.getInstance()[Calendar.MONTH].plus(1).toString().padStart(2, '0')
            val date = Calendar.getInstance()[Calendar.DAY_OF_MONTH].toString().padStart(2, '0')
            val hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY].toString().padStart(2, '0')

            //Standard reftime for luftkvalitets-API
            return  "${year}-${month}-${date}T${hour}:00:00Z"
        }

        fun isNowTime(time: String) : Boolean {

            if (time == getCurrentTimeAsString()){
                return true
            }
            return false
        }

        fun getDateAndHour(time: String) : String{

            if(isNowTime(time)){
                return "Nå"
            }

            val date = time.substring(8, 10)
            val hour = time.substring(11, 13)

            //return "kl: " + hour + " den " + date
            return "kl: $hour"
        }
/*
        fun getWeatherIcon(description: String?): Int {
            return when (description) {
                "clearsky", "clearsky_day"-> R.drawable.clearsky_day
                "cloudy" -> R.drawable.cloudy
                "fair", "fair_day" -> R.drawable.fair_day
                "fog" -> R.drawable.fog
                "heavyrain" -> R.drawable.heavyrain
                "heavyrainandthunder" -> R.drawable.heavyrainandthunder
                "heavyrainshowers", "heavyrainshowers_day" -> R.drawable.heavyrainshowers_day
                "heavyrainshowersandthunder","heavyrainshowersandthunder_day" -> R.drawable.heavyrainshowersandthunder_day
                "heavysleet" -> R.drawable.heavysleet
                "heavysleetandthunder" -> R.drawable.heavysleetandthunder
                "heavysleetshowers", "heavysleetshowers_day" -> R.drawable.heavysleetshowers_day
                "heavysleetshowersandthunder","heavysleetshowersandthunder_day" -> R.drawable.heavysleetshowersandthunder_day
                "heavysnow" -> R.drawable.heavysnow
                "heavysnowandthunder" -> R.drawable.heavysnowandthunder
                "heavysnowshowers","heavysnowshowers_day" -> R.drawable.heavysnowshowers_day
                "heavysnowshowersandthunder","heavysnowshowersandthunder_day" -> R.drawable.heavysnowshowersandthunder_day
                "lightrain" -> R.drawable.lightrain
                "lightrainandthunder" -> R.drawable.lightrainandthunder
                "lightrainshowers","lightrainshowers_day" -> R.drawable.lightrainshowers_day
                "lightrainshowersandthunder","lightrainshowersandthunder_day" -> R.drawable.lightrainshowersandthunder_day
                "lightsleet" -> R.drawable.lightsleet
                "lightsleetandthunder" -> R.drawable.lightsleetandthunder
                "lightsleetshowers","lightsleetshowers_day" -> R.drawable.lightsleetshowers_day
                "lightsnow" -> R.drawable.lightsnow
                "lightsnowandthunder" -> R.drawable.lightsnowandthunder
                "lightsnowshowers","lightsnowshowers_day" -> R.drawable.lightsnowshowers_day
                "lightssleetshowersandthunder","lightssleetshowersandthunder_day" -> R.drawable.lightssleetshowersandthunder_day
                "lightssnowshowersandthunder","lightssnowshowersandthunder_day" -> R.drawable.lightssnowshowersandthunder_day
                "partlycloudy" ,"partlycloudy_day"-> R.drawable.partlycloudy_day
                "rain" -> R.drawable.rain
                "rainandthunder" -> R.drawable.rainandthunder
                "rainshowers","rainshowers_day" -> R.drawable.rainshowers_day
                "rainshowersandthunder","rainshowersandthunder_day" -> R.drawable.rainshowersandthunder_day
                "sleet" -> R.drawable.sleet
                "sleetandthunder" -> R.drawable.sleetandthunder
                "sleetshowers", "sleetshowers_day" -> R.drawable.sleetshowers_day
                "sleetshowersandthunder","sleetshowersandthunder_day" -> R.drawable.sleetshowersandthunder_day
                "snow" -> R.drawable.snow
                "snowandthunder" -> R.drawable.snowandthunder
                "snowshowers","snowshowers_day" -> R.drawable.snowshowers_day
                "snowshowersandthunder","snowshowersandthunder_day" -> R.drawable.snowshowersandthunder_day
                "heavyrainshowers_night" -> R.drawable.heavyrainshowers_night
                "heavyrainshowersandthunder_night" -> R.drawable.heavyrainshowersandthunder_night
                "heavysleetshowers_night" -> R.drawable.heavysleetshowers_night
                "heavysleetshowersandthunder_night" -> R.drawable.heavysleetshowersandthunder_night
                "heavysnowshowers_night" -> R.drawable.heavysnowshowers_night
                "heavysnowshowersandthunder_night" -> R.drawable.heavysnowshowersandthunder_night
                "lightrainshowers_night" -> R.drawable.lightrainshowers_night
                "lightrainshowersandthunder_night" -> R.drawable.lightrainshowersandthunder_night
                "lightsleetshowers_night" -> R.drawable.lightsleetshowers_night
                "lightsnowshowers_night" -> R.drawable.lightsnowshowers_night
                "lightssleetshowersandthunder_night" -> R.drawable.lightssleetshowersandthunder_night
                "lightssnowshowersandthunder_night" -> R.drawable.lightssnowshowersandthunder_night
                "partlycloudy_night" -> R.drawable.partlycloudy_night
                "rainshowers_night" -> R.drawable.rainshowers_night
                "rainshowersandthunder_night" -> R.drawable.rainshowersandthunder_night
                "sleetshowers_night" -> R.drawable.sleetshowers_night
                "sleetshowersandthunder_night" -> R.drawable.sleetshowersandthunder_night
                "snowshowers_night" -> R.drawable.snowshowers_night
                "snowshowersandthunder_night" -> R.drawable.snowshowersandthunder_night
                "clearsky_night" -> R.drawable.clearsky_night
                "fair_night" -> R.drawable.fair_night
                else -> {
                    R.drawable.unknown
                }
            }
        }

 */
    }
}


