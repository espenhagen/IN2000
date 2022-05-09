package com.example.in2000team5.utils

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.MutableState

const val FIVE_SECONDS: Long = 5000

class Timer {

    companion object {
        /* Util method that counts down and sets mutable state when finished.*/
        fun countDown(state: MutableState<Boolean>) {
            val timer = object: CountDownTimer(FIVE_SECONDS, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    Log.i("Seconds remaining: ", "${millisUntilFinished / 1000}")
                }
                override fun onFinish() {
                    state.value = false
                }
            }
            timer.start()
        }
    }
}