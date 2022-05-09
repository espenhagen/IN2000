package com.example.in2000team5.data_layer.datasource

import com.google.android.gms.maps.model.LatLng
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class BicycleServiceLocalDataSource {

    fun readTextFile(stream: InputStream): MutableList<String> {
        val lines: MutableList<String> = mutableListOf()

        var string: String? = ""
        val `is`: InputStream = stream
        val reader = BufferedReader(InputStreamReader(`is`))
        while (true) {
            try {
                if (reader.readLine().also {
                        string = it
                        lines.add(string.toString())
                    } == null) break
            } catch (e: IOException) {

                e.printStackTrace()
            }


        }
        `is`.close()

        return lines
    }
}

data class ServiceStation(val name: String, val coordinates: LatLng)