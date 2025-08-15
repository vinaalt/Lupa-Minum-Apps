package edu.unikom.lupaminum.repository

import android.util.Log
import edu.unikom.lupaminum.model.Identity
import edu.unikom.lupaminum.model.WeatherResponse
import edu.unikom.lupaminum.network.ApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: ApiService
) {
    //menggunakan coroutine
    suspend fun getWeather(lat: String, long: String, appid: String, units: String): WeatherResponse {
        Log.d("CEK YAAA", "Fetching weather for lat=$lat, lon=$long, units=$units")
        val response = apiService.getWeather(lat, long, appid, units)
        Log.d("WeatherRepository", "API Response: $response")
        return response
    }//3. UserRepository ini bilang: Aku butuh ApiService nih! (balik lagi ke file AppModule)
}