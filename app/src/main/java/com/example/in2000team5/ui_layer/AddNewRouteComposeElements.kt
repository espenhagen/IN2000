package com.example.in2000team5.ui_layer

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.in2000team5.data_layer.BicycleRoute
import com.example.in2000team5.data_layer.BigBikeRoute
import com.example.in2000team5.domain_layer.BicycleViewModel
import com.example.in2000team5.ui_layer.bicycleRouteList

@Composable
fun VisNyRuteKnapp(bicycleViewModel: BicycleViewModel) {
    var showForm = remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(24.dp, 64.dp),
                onClick = {
                    /*
                    val nyRute = mutableStateOf(BigBikeRoute(
                        200,
                        bicycleRouteList[2].value.fragmentList,
                        "Oslo",
                        "Tøyen",
                        200000.20,
                        mutableStateOf(2.0)
                    ))

                    bicycleViewModel.postRoutes(nyRute as SnapshotMutableState<BigBikeRoute>)
                     */
                    showForm.value = true
                }) {
                Icon(imageVector = Icons.Default.Add, "")
            }
        }
    ) {

    }
    if (showForm.value) VisNyRuteSkjema()
    // TODO: remove form showForm is true
}

@Composable
fun VisNyRuteSkjema() {
    // TODO: Create the form and make user input values
    Log.d("test", "test klikk")
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        Column(
            modifier = Modifier
                .height(200.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "This is a card view",
                style = MaterialTheme.typography.h4)
        }
    }
}