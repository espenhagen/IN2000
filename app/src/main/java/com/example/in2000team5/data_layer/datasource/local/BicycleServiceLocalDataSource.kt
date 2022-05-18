package com.example.in2000team5.data_layer.datasource.local

import com.google.android.gms.maps.model.LatLng
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/*
  Reads the service stations from a local file, and returns a list with service station-objects.
 */
class BicycleServiceLocalDataSource {

    fun readTextFile(stream: InputStream): MutableList<String> {
        val lines: MutableList<String> = mutableListOf()

        var string: String?
        val inputStream: InputStream = stream
        val reader = BufferedReader(InputStreamReader(inputStream))
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
        inputStream.close()

        return lines
    }
}

// Represents a service station
data class ServiceStation(val name: String, val coordinates: LatLng)