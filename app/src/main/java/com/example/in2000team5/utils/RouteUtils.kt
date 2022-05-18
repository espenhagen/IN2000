package com.example.in2000team5.utils

import androidx.compose.ui.graphics.Color

class RouteUtils {

    companion object {
        fun routeNames(): HashMap<Int, List<String>> {
            return hashMapOf(
                0 to listOf("ingen", "rutenr"),
                1 to listOf("Torshov", "Vippetangen"),
                2 to listOf("Smestad", "Tøyen/Galgeberg"),
                3 to listOf("Rådhusplassen", "Dælenga/Ring 2"),
                4 to listOf("Frogner", "Tiedemannsjordet"),
                5 to listOf("Frognerparken", "Galgeberg"),
                6 to listOf("Skøyen", "Carl Berners plass"),
                7 to listOf("Gaustad", "Storo"),
                8 to listOf("Nydalen", "St. Olavs plass")
            )
        }

        fun routeColor(ruteid:Int): Color {
            val colorMap: HashMap<Int, Color> = hashMapOf(
                1 to Color.Blue,
                2 to Color.Magenta,
                3 to Color.DarkGray,
                4 to Color.Green,
                5 to Color.Black,
                6 to Color.Cyan,
                7 to Color.Red,
                8 to Color.Gray
            )
            return colorMap[ruteid]?:Color.White
        }
    }
}