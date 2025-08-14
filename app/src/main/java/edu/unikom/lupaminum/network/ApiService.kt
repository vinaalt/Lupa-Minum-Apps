package edu.unikom.lupaminum.network

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.unikom.lupaminum.model.ResponseUser
import edu.unikom.lupaminum.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //Ambil list data keseluruhan
    @GET("/data/2.5/weather")
    suspend fun getWeather(@Query("lat") lat: String,
                           @Query("lon") long: String,
                           @Query("appid") appid: String,
                           @Query("units") units: String): WeatherResponse //(model di model belum ada tadinya ini diisi ResponseUser)
}