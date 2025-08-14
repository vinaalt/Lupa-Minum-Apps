package edu.unikom.lupaminum.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @field:SerializedName("lat")
    val lat: String? = null, //mapping parsing data dari retrofit

    @field:SerializedName("lon")
    val lon: String? = null
)

