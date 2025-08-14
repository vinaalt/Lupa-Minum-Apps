package edu.unikom.lupaminum.repository

import edu.unikom.lupaminum.model.Identity
import edu.unikom.lupaminum.network.ApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: ApiService
) {
    //menggunakan coroutine
    suspend fun getWeather(lat: String, long: String, appid: String, units: String) =
        apiService.getWeather(
            lat,
            long,
            appid,
            units
        ) //3. UserRepository ini bilang: Aku butuh ApiService nih! (balik lagi ke file AppModule)
}