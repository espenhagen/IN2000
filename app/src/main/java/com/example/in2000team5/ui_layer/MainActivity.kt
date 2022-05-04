package com.example.in2000team5.ui_layer

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import com.example.in2000team5.ui_layer.theme.IN2000Team5Theme
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.in2000team5.ui_layer.compose_screen_elements.*
import com.example.in2000team5.ui_layer.viewmodels.BicycleRouteViewModel
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class MainActivity : ComponentActivity() {
    private val bicycleRouteViewModel: BicycleRouteViewModel by viewModels()
    private val weatherDataViewModel: WeatherDataViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQ_CODE = 1000

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        getCurrentLocation()


        setContent {
            IN2000Team5Theme {
                BottomNavigation(weatherDataViewModel, bicycleRouteViewModel, latitude, longitude)

            }
        }
    }

    private fun getCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // request permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE);

            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location

                latitude = location.getLatitude()
                longitude = location.getLongitude()
                Log.d("GEODATA: ", "------------LAT / LONG : " + latitude + "  og " + longitude)

            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed on getting current location",
                    Toast.LENGTH_SHORT).show()
            }
    }

}

@Composable
fun BottomNavigation(
    weatherDataViewModel: WeatherDataViewModel,
    bicycleRouteViewModel: BicycleRouteViewModel,
    latitude: Double,
    longitude: Double
) {
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
        Navigation(navController = navController, weatherDataViewModel, bicycleRouteViewModel,latitude, longitude)
    }
}

@Composable
fun Navigation(navController: NavHostController,
               weatherDataViewModel: WeatherDataViewModel,
               bicycleRouteViewModel: BicycleRouteViewModel,
               latitude: Double,
               longitude: Double
) {
    NavHost(navController = navController, startDestination = "om" ) {
        composable("kart") {
            MapScreen(bicycleRouteViewModel,latitude, longitude)
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
