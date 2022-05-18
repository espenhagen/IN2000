package com.example.in2000team5.data_layer.datasource.local

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/*
  Room does not allow complex data to be stored in the database. Therefore, we have to convert
  between lists and other complex types when reading to and from the database.
 */
class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromFragmentList(value: MutableList<List<LatLng>?>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun fromStringToFragmentList(listString: String): MutableList<List<LatLng>?> {
        return gson.fromJson(
            listString,
            object : TypeToken<MutableList<List<LatLng>?>>() {}.type
        )
    }

    @TypeConverter
    fun fromMutableStateDouble(value: MutableState<Double?>): Double {
        return value.value ?: 0.0
    }

    @TypeConverter
    fun fromDoubleToMutableState(AQI: Double?): MutableState<Double?> {
        return mutableStateOf(AQI)
    }
}