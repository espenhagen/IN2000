package com.example.in2000team5.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class GeneralUtils {

    companion object{
        //found on internet (stackoverflow)
        fun Double.round(decimals: Int): Double {
            var multiplier = 1.0
            repeat(decimals) { multiplier *= 10 }
            return kotlin.math.round(this * multiplier) / multiplier
        }


        //a toast showing a message
        //to include context as parameter:val context = LocalContext.current
        @Composable
        fun ShowToast(msg:String, context: Context) {
            Column(
                content = {
                    Toast.makeText(
                        context,
                        msg,
                        Toast.LENGTH_SHORT
                    ).show()
                }, modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            )
        }
    }


}