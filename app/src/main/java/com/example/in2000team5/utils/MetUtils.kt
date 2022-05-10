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
    }
}


