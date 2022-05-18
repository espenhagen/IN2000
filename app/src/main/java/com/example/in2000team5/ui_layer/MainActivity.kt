package com.example.in2000team5.ui_layer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.in2000team5.ui_layer.compose_elements.*
import com.example.in2000team5.ui_layer.theme.IN2000Team5Theme
import com.example.in2000team5.ui_layer.viewmodels.BicycleInformationViewModel
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel

class MainActivity : ComponentActivity() {
    private lateinit var bicycleInformationViewModel: BicycleInformationViewModel
    private val weatherDataViewModel: WeatherDataViewModel by viewModels()

    //Kan brukes til å vise om man har internett eller ikke.
    /*private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bicycleInformationViewModel = ViewModelProvider(this)[BicycleInformationViewModel::class.java]

        // Display splash until viewModel init is not loading anymore
        // Splash screen shows only when app is started from launcher or phone, not from AS
        installSplashScreen().setKeepOnScreenCondition {
            bicycleInformationViewModel.isLoading.value
        }
        setContent {
            IN2000Team5Theme {
                BottomNavigation(weatherDataViewModel, bicycleInformationViewModel)
            }
        }
    }
}