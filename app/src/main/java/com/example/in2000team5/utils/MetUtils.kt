package com.example.in2000team5.utils

import java.util.*

class MetUtils {

    companion object {
        /**Method that gets the current time and returns it represented as a string.*/
        fun getCurrentTimeAsString() : String{

            val year = Calendar.getInstance()[Calendar.YEAR]

            val month = Calendar.getInstance()[Calendar.MONTH].plus(1).toString().padStart(2, '0')
            val date = Calendar.getInstance()[Calendar.DAY_OF_MONTH].toString().padStart(2, '0')
            val hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY].toString().padStart(2, '0')

            return  "${year}-${month}-${date}T${hour}:00:00Z"
        }

        fun isNowTime(time: String) : Boolean {

            if (time == getCurrentTimeAsString()){
                return true
            }
            return false
        }

        fun getDateAndHour(time: String?) : String{
            if (time.isNullOrEmpty())return "ukjent"
            if(isNowTime(time)){
                return "Nå"
            }
            val hour = time.substring(11, 13)
            return "kl: $hour"
        }

        /**Function that takes in the wind direction as a number of degrees and returns a 1-3 letter description based on the number of degrees (22.5 degree increments)  */
        fun getWindDirectionDescription(degrees:Float) : String {
            val deg = degrees.toLong()
            return when {
                LongRange(12,33).contains(deg) -> "NNØ"
                LongRange(34,56).contains(deg) -> "NØ"
                LongRange(57,78).contains(deg) -> "ØNØ"
                LongRange(79,101).contains(deg) -> "Ø"
                LongRange(102,123).contains(deg) -> "ØSØ"
                LongRange(124,146).contains(deg) -> "SØ"
                LongRange(147,168).contains(deg) -> "SSØ"
                LongRange(169,191).contains(deg) -> "S"
                LongRange(192,213).contains(deg) -> "SSV"
                LongRange(214,236).contains(deg) -> "SV"
                LongRange(237,258).contains(deg) -> "VSV"
                LongRange(259,281).contains(deg) -> "V"
                LongRange(282,303).contains(deg) -> "VNV"
                LongRange(304,326).contains(deg) -> "NV"
                LongRange(327,348).contains(deg) -> "NNV"
                else -> {
                    "N"
                }
            }
        }
    }
}


