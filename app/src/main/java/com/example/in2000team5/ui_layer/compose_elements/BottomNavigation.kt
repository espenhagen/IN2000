package com.example.in2000team5.ui_layer.compose_elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.in2000team5.ui_layer.viewmodels.BicycleRouteViewModel
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel


@Composable
fun BottomNavigation(weatherDataViewModel: WeatherDataViewModel, bicycleRouteViewModel: BicycleRouteViewModel) {
    val navController = rememberNavController()
    Scaffold(
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Navigation(navController = navController, weatherDataViewModel, bicycleRouteViewModel)
            }
        },
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
            Column {
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

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)
