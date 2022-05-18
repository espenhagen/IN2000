package com.example.in2000team5.ui_layer

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.in2000team5.ui_layer.compose_screen_elements.*
import com.example.in2000team5.ui_layer.theme.IN2000Team5Theme
import com.example.in2000team5.ui_layer.viewmodels.BicycleRouteViewModel
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel
import java.util.*


class MainActivity : ComponentActivity() {
    private lateinit var bicycleRouteViewModel: BicycleRouteViewModel
    private val weatherDataViewModel: WeatherDataViewModel by viewModels()

    //Kan brukes til å vise om man har internett eller ikke.
    /*private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bicycleRouteViewModel = ViewModelProvider(this)[BicycleRouteViewModel::class.java]

        // Display splash until viewModel init is not loading anymore
        // Splash screen shows only when app is started from launcher or phone, not from AS
        installSplashScreen().setKeepOnScreenCondition {
            bicycleRouteViewModel.isLoading.value
        }
        setContent {
            IN2000Team5Theme {
                BottomNavigation(weatherDataViewModel, bicycleRouteViewModel)
            }
        }
    }
}

@Composable
fun BottomNavigation(weatherDataViewModel: WeatherDataViewModel, bicycleRouteViewModel: BicycleRouteViewModel) {
    val navController = rememberNavController()
    Scaffold(
        content = { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Navigation(navController = navController, weatherDataViewModel, bicycleRouteViewModel)
        }},
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(name = "Kart", route = "kart", icon = Icons.Default.Place),
                    BottomNavItem(name = "Ruter", route = "ruter", icon = Icons.Default.Menu),
                    BottomNavItem(name = "Om", route = "om", icon = Icons.Default.Info)
                ),
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )
        }
    )
}

@Composable
fun Navigation(navController: NavHostController,
               weatherDataViewModel: WeatherDataViewModel,
               bicycleRouteViewModel: BicycleRouteViewModel
) {
    NavHost(navController = navController, startDestination = "om" ) {
        composable("kart") {
            Column() {
                InfoRow(weatherDataViewModel)
                MapScreen(bicycleRouteViewModel)
                //MapProperties()
            }

        }
        composable("ruter") {
            Column {
                InfoRow(weatherDataViewModel)
                ShowNewRouteButton(bicycleRouteViewModel)
            }
        }
        composable("om") {
            SupportScreen(weatherDataViewModel)
        }
    }
}
