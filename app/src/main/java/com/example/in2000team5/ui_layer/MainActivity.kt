package com.example.in2000team5.ui_layer

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.in2000team5.ui_layer.theme.IN2000Team5Theme
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.in2000team5.data_layer.repository.BigBikeRoute
import com.example.in2000team5.domain_layer.BicycleViewModel
import com.example.in2000team5.domain_layer.WeatherDataViewModel
import com.example.in2000team5.ui_layer.cardViewActivity.InfoRow
import com.example.in2000team5.ui_layer.cardViewActivity.SupportBox
import com.example.in2000team5.ui_layer.cardViewActivity.VisAlleRuter
import com.example.in2000team5.utils.routeUtils.Companion.routeColor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

//TODO flytte dette inn i BicycleViewModel, sende ViewModel med til de ulike compose-elementene
var bicycleRouteList = SnapshotStateList<SnapshotMutableState<BigBikeRoute>>()


class MainActivity : ComponentActivity() {
    private val viewModel: BicycleViewModel by viewModels()
    private val weatherModel: WeatherDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Display splash until viewModel init is not loading anymore
        // Splash screen shows only when app is started from launcher or phone, not from AS
        installSplashScreen().setKeepOnScreenCondition {
           !viewModel.isLoading.value
        }

        viewModel.makeApiRequest(this)
        //hardkodet til midt i oslo
        weatherModel.fetchWeather("59.91370670", "10.7526291")

        weatherModel.getTemperatureLiveData().observe(this) {
            Log.e("temperatur", it.toString())
        }

        setContent {
            IN2000Team5Theme {
                BottomNavigation(weatherModel, viewModel)
            }
        }
        bicycleRouteList = viewModel.getRoutes()
/*        viewModel.getRoutes().observe(this) {
            bicycleRouteList = it as mutableStateListOf<BigBikeRoute>

        }*/
    }
}

@Composable
fun BottomNavigation(model:WeatherDataViewModel, bicycleViewModel: BicycleViewModel) {
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
        Navigation(navController = navController, model, bicycleViewModel)
    }
}

@Composable
fun MapScreen() {

    val oslo = LatLng(59.9139, 10.7522)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(oslo, 12f)
    }
    GoogleMap(
        modifier = Modifier.padding(bottom = 50.dp),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(compassEnabled = true, myLocationButtonEnabled = true)
    ) {
        Marker(     // Adds marker to the map
            position = oslo,
            title = "Oslo",
            snippet = "Marker in Oslo"
        )

        for (storRute in bicycleRouteList) {

            for (liste in storRute.value.fragmentList) {
                liste.let {
                    Polyline(points = it!!, color = routeColor(storRute.value.id))
                }
            }
//
        }
    }
}


@Composable
fun Navigation(navController: NavHostController,
               model:WeatherDataViewModel,
               bicycleViewModel: BicycleViewModel) {
    NavHost(navController = navController, startDestination = "om" ) {
        composable("kart") {
            MapScreen()
            //MapProperties()

        }
        composable("ruter") {
            Column {
                InfoRow(model)

                VisNyRuteKnapp(bicycleViewModel = bicycleViewModel)

            }
        }
        composable("om") {
            //InfoRow(model)
            SupportBox(model)
            //AboutScreen()
        }
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
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
                        if (item.badgeCount > 0) {
                            BadgeBox(
                                badgeContent = {
                                    Text(text = item.badgeCount.toString())
                                }
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name
                                )
                            }
                        } else {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name
                            )
                        }
                        if (selected) {
                            Text(text = item.name, textAlign = TextAlign.Center, fontSize = 10.sp)
                        }
                    }
                }
            )

        }
    }
}


@Composable
fun AboutScreen() {
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "SykkelParadis. En app for deg og meg.")
    }
}

