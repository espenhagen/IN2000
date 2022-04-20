package com.example.in2000team5.utils

import androidx.compose.ui.graphics.Color

class routeUtils {

    companion object {
        fun routeNames(): HashMap<Int, List<String>> {
            val nameMap: HashMap<Int, List<String>> = hashMapOf(0 to listOf("ingen", "rutenr"), 1 to listOf("Torshov", "Vippetangen"), 2 to listOf("Smestad", "Tøyen/Galgeberg"), 3 to listOf("Rådhusplassen", "Dælenga/Ring 2"), 4 to listOf("Frogner", "Tiedemannsjordet"), 5 to listOf("Frognerparken", "Galgeberg"), 6 to listOf("Skøyen", "Carl Berners plass"), 7 to listOf("Gaustad", "Storo"), 8 to listOf("Nydalen", "St. Olavs plass"))
            return nameMap
        }

        fun routeColor(ruteid:Int): Color {
            val colorMap: HashMap<Int, Color> = hashMapOf(0 to Color.Red, 1 to Color.Blue, 2 to Color.Magenta, 3 to Color.Yellow, 4 to Color.Green, 5 to Color.Black, 6 to Color.Cyan, 7 to Color.LightGray)
            return colorMap.get(ruteid)?:Color.White
        }
    }
}