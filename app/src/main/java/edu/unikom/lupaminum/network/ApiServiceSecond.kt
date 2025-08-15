package edu.unikom.lupaminum.network

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.unikom.lupaminum.model.ResponseUser
import edu.unikom.lupaminum.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiServiceSecond {

    //Ambil list data keseluruhan
    @GET("/data/2.5/weather")
    suspend fun getWeatherApi(@Query("lat") lat: String,
                           @Query("lon") long: String,
                           @Query("appid") appid: String,
                           @Query("units") units: String): Call<WeatherResponse> //(model di model belum ada tadinya ini diisi ResponseUser)

}