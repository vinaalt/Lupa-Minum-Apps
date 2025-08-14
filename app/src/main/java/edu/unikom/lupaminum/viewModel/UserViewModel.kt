package edu.unikom.lupaminum.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.unikom.lupaminum.model.DataItem
import edu.unikom.lupaminum.model.Identity
import edu.unikom.lupaminum.model.WeatherResponse
import edu.unikom.lupaminum.repository.UserRepository
import edu.unikom.lupaminum.repository.WeatherRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val weatherRepository: WeatherRepository //1. Hei Hilt! aku butuh UserRepository dong di ViewModel ini.
) : ViewModel() {
    val weatherData = MutableLiveData<WeatherResponse>()

    fun saveIdentity(identity: String) {
        userRepository.saveIdentity(identity)
    }

    fun isIdentitySaved(): Boolean {
        return userRepository.isIdentitySaved()
    }

    fun getIdentity(): Identity? {
        return userRepository.getIdentity()
    }

    fun fetchWeather(lat: String, long: String, appid: String, units: String) {
        viewModelScope.launch {
            try {
                val result = weatherRepository.getWeather(lat,long,appid,units)
                result?.let {
                    weatherData.postValue(it)
                }
                Log.d("WeatherViewModel", "${result}")
            } catch (e: Exception) {
                Log.e("WeatherViewModel", e.message ?: "Unknown error")
            }
        }
    }
}
