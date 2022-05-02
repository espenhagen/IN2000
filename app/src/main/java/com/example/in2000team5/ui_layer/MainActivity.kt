package com.example.in2000team5.ui_layer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import com.example.in2000team5.ui_layer.theme.IN2000Team5Theme
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.in2000team5.data_layer.repository.BicycleRoute
import com.example.in2000team5.ui_layer.compose_screen_elements.*
import com.example.in2000team5.ui_layer.viewmodels.BicycleRouteViewModel
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel


class MainActivity : ComponentActivity() {
    private val bicycleRouteViewModel: BicycleRouteViewModel by viewModels()
    private val weatherDataViewModel: WeatherDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Display splash until viewModel init is not loading anymore
        // Splash screen shows only when app is started from launcher or phone, not from AS
        installSplashScreen().setKeepOnScreenCondition {
            //TODO: bestemme hvilken betingelse som skal settes (bicycle eller weather-viewmodel?)
            !bicycleRouteViewModel.isLoading.value
        }

        // TODO: sjekk om dette kan dyttes i en init-blokk i viewmodel-klassen, og om det må endres etter posisjon hentes
        // weatherDataViewModel.fetchWeather("59.91370670", "10.7526291")

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
        bottomBar = {
            BottomNavigationBar(
                items = listOf(
                    BottomNavItem(name = "Kart", route = "kart", icon = Icons.Default.Place),
                    BottomNavItem(name = "Ruter", route = "ruter", icon = Icons.Default.ArrowForward),
                    BottomNavItem(name = "Om", route = "om", icon = Icons.Default.Info)
                ),
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route)
                }
            )
        }
    ) {
        Navigation(navController = navController, weatherDataViewModel, bicycleRouteViewModel)
    }
}

@Composable
fun Navigation(navController: NavHostController,
               weatherDataViewModel: WeatherDataViewModel,
               bicycleRouteViewModel: BicycleRouteViewModel
) {
    NavHost(navController = navController, startDestination = "om" ) {
        composable("kart") {
            MapScreen(bicycleRouteViewModel)
            //MapProperties()
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
