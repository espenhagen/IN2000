package com.example.in2000team5.ui_layer.compose_screen_elements

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.in2000team5.ui_layer.viewmodels.BicycleRouteViewModel
import com.example.in2000team5.utils.RouteUtils
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun MapScreen(bicycleRouteViewModel: BicycleRouteViewModel) {

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

        for (storRute in bicycleRouteViewModel.getRoutes()) {

            for (liste in storRute.value.fragmentList) {
                liste.let {
                    Polyline(points = it!!, color = RouteUtils.routeColor(storRute.value.id))
                }
            }
//
        }
    }
}