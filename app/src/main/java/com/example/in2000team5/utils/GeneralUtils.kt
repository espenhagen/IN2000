package com.example.in2000team5.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.MutableState

class GeneralUtils {

    companion object{
        //This function was copied from the following link: https://discuss.kotlinlang.org/t/how-do-you-round-a-number-to-n-decimal-places/8843/2
        /*function to set a double's number of decimals by passing number of decimals as parameter.*/
        fun Double.round(decimals: Int): Double {
            var multiplier = 1.0
            repeat(decimals) { multiplier *= 10 }
            return kotlin.math.round(this * multiplier) / multiplier
        }

        /* Util method that counts down and sets mutable state when finished.*/
        const val FIVE_SECONDS: Long = 5000
        fun countDown(state: MutableState<Boolean>) {
            val timer = object : CountDownTimer(FIVE_SECONDS, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i("Seconds remaining: ", "${millisUntilFinished / 1000}")
                }
                override fun onFinish() {
                    state.value = false
                }
            }
            timer.start()
        }

        // Inspired by StackOverflow: https://stackoverflow.com/questions/68115487/how-to-get-network-info-android
        fun isInternetAvailable(applicationContext: Context): Boolean {
            var result = false
            val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            connectivityManager?.let {
                it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
            }
            return result
        }
    }
}