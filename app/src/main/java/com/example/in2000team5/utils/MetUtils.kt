package com.example.in2000team5.utils

import java.util.*

class MetUtils {

    companion object {
        //return the current time as a string on specific format
        fun getCurrentTimeAsString() : String{

            //paddStart makes values under 10 get 0 in front. (01,02...)
            //Calender.XXXX is an index for placement of the value

            val year = Calendar.getInstance()[Calendar.YEAR]

            //value of january starts at 0
            val month = Calendar.getInstance()[Calendar.MONTH].plus(1).toString().padStart(2, '0')
            val date = Calendar.getInstance()[Calendar.DAY_OF_MONTH].toString().padStart(2, '0')
            val hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY].toString().padStart(2, '0')

            //Standard reftime for met-API
            return  "${year}-${month}-${date}T${hour}:00:00Z"
        }

        //check if parameter is current time
        fun isNowTime(time: String) : Boolean {

            if (time == getCurrentTimeAsString()){
                return true
            }
            return false
        }

        fun getDateAndHour(time: String?) : String{
            if (time.isNullOrEmpty())return "Ukjent"
            if(isNowTime(time)){
                return "Nå"
            }

            val hour = time.substring(11, 13)

            return "kl: $hour"
        }

        fun getWindDirectionDescription(degFloat:Float) : String {
            val deg = degFloat.toLong()
            return when {
                LongRange(12,33).contains(deg) -> "NNE"
                LongRange(34,56).contains(deg) -> "NE"
                LongRange(57,78).contains(deg) -> "ENE"
                LongRange(79,101).contains(deg) -> "E"
                LongRange(102,123).contains(deg) -> "ESE"
                LongRange(124,146).contains(deg) -> "SE"
                LongRange(147,168).contains(deg) -> "SSE"
                LongRange(169,191).contains(deg) -> "S"
                LongRange(192,213).contains(deg) -> "SSW"
                LongRange(214,236).contains(deg) -> "SW"
                LongRange(237,258).contains(deg) -> "WSW"
                LongRange(259,281).contains(deg) -> "W"
                LongRange(282,303).contains(deg) -> "WNW"
                LongRange(304,326).contains(deg) -> "NW"
                LongRange(327,348).contains(deg) -> "NNW"
                else -> {
                    "N"
                }
            }
        }
    }
}


