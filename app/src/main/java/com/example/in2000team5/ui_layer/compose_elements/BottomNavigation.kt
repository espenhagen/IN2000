package com.example.in2000team5.ui_layer.compose_elements

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.in2000team5.ui_layer.viewmodels.BicycleInformationViewModel
import com.example.in2000team5.ui_layer.viewmodels.WeatherDataViewModel

//Shows the apps navigation bar
@Composable
fun BottomNavigation(weatherDataViewModel: WeatherDataViewModel, bicycleInformationViewModel: BicycleInformationViewModel) {
    val navController = rememberNavController()
    Scaffold(
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Navigation(navController = navController, weatherDataViewModel, bicycleInformationViewModel)
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

//Handles the user input (clicks) and
@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    androidx.compose.material.BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.DarkGray,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = false, // update this to selected when we want to have actionable buttons
                onClick = { onItemClick(item) },
                selectedContentColor = Color.Green,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name
                        )
                        if (selected) {
                            Text(text = item.name, textAlign = TextAlign.Center, fontSize = 10.sp)
                        }
                    }
                }
            )
        }
    }
}

//Calls the composable-elements for the different screens that one can click in the nav-bar
@Composable
fun Navigation(navController: NavHostController,
               weatherDataViewModel: WeatherDataViewModel,
               bicycleInformationViewModel: BicycleInformationViewModel
) {
    NavHost(navController = navController, startDestination = "om" ) {
        composable("kart") {
            Column {
                WeatherInformationTopBar(weatherDataViewModel)
                MapScreen(bicycleInformationViewModel)
                //MapProperties()
            }
        }
        composable("ruter") {
            Column {
                WeatherInformationTopBar(weatherDataViewModel)
                Scaffold(
                    content = { padding ->
                        Column(modifier = Modifier.padding(padding)) {
                            ShowAllRoutes(ruter = bicycleInformationViewModel.getRoutes())
                            } },
                    floatingActionButton = { NewRouteButton(bicycleInformationViewModel = bicycleInformationViewModel) }
                )
            }
        }
        composable("om") {
            SupportScreen(weatherDataViewModel)
        }
    }
}

//data class used by the navigation bar
data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)
