package com.example.in2000team5.utils

class GeneralUtils {

    companion object{
        //found on internet (stackoverflow)
        fun Double.round(decimals: Int): Double {
            var multiplier = 1.0
            repeat(decimals) { multiplier *= 10 }
            return kotlin.math.round(this * multiplier) / multiplier
        }
    }
}