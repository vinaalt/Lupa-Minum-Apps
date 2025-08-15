package edu.unikom.lupaminum.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import edu.unikom.lupaminum.viewModel.UserViewModel

object PermissionAndLocation {
    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(launcher: ActivityResultLauncher<Array<String>>) {
        launcher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    @SuppressLint("MissingPermission") // mematikan peringatan perizinan karena perizinan akses lokasi sudah dipastikan sebelumnya
    fun getCurrentLocation(
        fusedLocationClient: FusedLocationProviderClient,
        callback: (Double, Double) -> Unit
    ) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, // Gunakan akurasi tinggi
            1000 // Interval 1 detik
        ).setMaxUpdates(1).build() // Hanya ambil sekali lalu stop

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    callback(location.latitude, location.longitude)
                }

                // Stop request setelah dapat data
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun hitAPIWeather(
        lat: Double,
        lon: Double,
        appid: String,
        units: String,
        viewModel: UserViewModel
    ) {
        viewModel.fetchWeather(lat.toString(), lon.toString(), appid, units)
    }
}