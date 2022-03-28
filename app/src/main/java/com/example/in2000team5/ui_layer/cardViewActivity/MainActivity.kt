package com.example.in2000team5.ui_layer.cardViewActivity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import com.example.in2000team5.data_layer.BicycleRoute
import com.example.in2000team5.domain_layer.BicycleViewModel
import com.example.in2000team5.ui_layer.theme.IN2000Team5Theme


class MainActivity : ComponentActivity() {
    private val viewModel: BicycleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.makeApiRequest(this)

        var bicycleRouteList = mutableListOf<BicycleRoute>()

        viewModel.getRoutes().observe(this) {
            bicycleRouteList = it as MutableList<BicycleRoute>
            setContent {
                IN2000Team5Theme() {
                    Column(){
                        InfoRow(uv = 24, sykkelfore = "godt" ) //hardkodet testverdi.
                        VisAlleRuter(bicycleRouteList)
                    }

                }
            }
        }
    }
}
