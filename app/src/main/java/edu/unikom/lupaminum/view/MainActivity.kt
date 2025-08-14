package edu.unikom.lupaminum.view

import android.util.Log
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import edu.unikom.lupaminum.adapter.UserAdapter
import edu.unikom.lupaminum.databinding.ActivityMainBinding
import edu.unikom.lupaminum.viewModel.UserViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: UserViewModel by viewModels()
    private val adapter = UserAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        // Inisialisasi launcher untuk request permission
        super.onCreate(savedInstanceState)

        requestLocationPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                // Callback setelah user memilih izinkan/tolak
                val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

                if (fineLocationGranted || coarseLocationGranted) {
                    Toast.makeText(this, "GPS Permission Granted", Toast.LENGTH_SHORT).show()
                    Log.d("GRANTED","Ini masuk")

                    content()
                } else {
                    Toast.makeText(this, "GPS Permission Denied", Toast.LENGTH_SHORT).show()
                    Log.d("GRANTED","Ini tidak masuk")
                    uiWithoutGPSAccess()
                }
            }


        // Cek dan minta izin
        if (!checkLocationPermissions()) {
            requestLocationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            Log.d("GRANTED","Sudah masuk sebelumnya")
            content()
        }
    }

    fun checkLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, // or requireContext() if in a Fragment
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this, // or requireContext() if in a Fragment
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    fun content() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        // Fetch data dari halaman 2
//        binding.rvUsers.layoutManager = LinearLayoutManager(this)
//        binding.rvUsers.adapter = adapter

//        viewModel.fetchUsers("2")

        getCurrentLocation { lat, long ->
            val appid = "2469d458b7ad46b9632c1497d5c2d9a2"
            val units = "imperial"
            viewModel.fetchWeather(lat.toString(),long.toString(), appid, units)
            Log.d("LATLONG","${lat.toString()} + ${long.toString()}")
        }


//        viewModel.weatherData.observe(this) { userList ->
//            userList?.let {
//                adapter.updateData(it)
//            }
//        }
    }

    fun uiWithoutGPSAccess() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @SuppressLint("MissingPermission") // mematikan peringatan perizinan karena perizinan akses lokasi sudah dipastikan sebelumnya
    private fun getCurrentLocation(callback: (Double, Double) -> Unit) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, // Gunakan akurasi tinggi
            1000 // Interval 1 detik
        ).setMaxUpdates(1) // Hanya ambil sekali lalu stop
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    Toast.makeText(this@MainActivity, "Lat: $lat, Lon: $lon", Toast.LENGTH_LONG).show()
                    callback(lat, lon)
                } else {
                    Toast.makeText(this@MainActivity, "Location not available", Toast.LENGTH_SHORT).show()
                }

                // Stop request setelah dapat data
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }
}
