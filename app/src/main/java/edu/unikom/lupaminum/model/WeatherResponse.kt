package edu.unikom.lupaminum.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @field:SerializedName("main")
    val main: Main,

    @field:SerializedName("location")
    val location: String
)

data class Main(
    @field:SerializedName("temp")
    val temp: Double,

    @field:SerializedName("humidity")
    val humidity: Int
)

