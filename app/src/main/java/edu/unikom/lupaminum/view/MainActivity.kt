package edu.unikom.lupaminum.view

import android.util.Log
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
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
import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import edu.unikom.lupaminum.R
import edu.unikom.lupaminum.utils.NeedWaterLevel.needWaterLevel
import edu.unikom.lupaminum.utils.PermissionAndLocation
import edu.unikom.lupaminum.worker.WaterReminderWorker
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Inisialisasi launcher untuk request permission
        super.onCreate(savedInstanceState)

        if (viewModel.getSchedule() != null) {
            val schedule = viewModel.getSchedule()

            val intervalMinutes = schedule?.time?.toLongOrNull() ?: 15L // default 15 menit
            val waterReminderRequest = PeriodicWorkRequestBuilder<WaterReminderWorker>(
                intervalMinutes, TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "WaterReminder",
                ExistingPeriodicWorkPolicy.REPLACE,
                waterReminderRequest
            )
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupPermissionLauncher()
        checkLocationPermissions()
    }

    private fun setupPermissionLauncher() {
        requestLocationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

                if (fineGranted || coarseGranted) {
                    Toast.makeText(this, "GPS Permission Granted", Toast.LENGTH_SHORT).show()
                    content()
                } else {
                    Toast.makeText(this, "GPS Permission Denied", Toast.LENGTH_SHORT).show()
                    uiWithoutGPSAccess()
                }
            }
    }

    private fun checkLocationPermissions() {
        if (!PermissionAndLocation.hasLocationPermission(this)) {
            PermissionAndLocation.requestLocationPermission(requestLocationPermissionLauncher)
        } else {
            Toast.makeText(this, "GPS Permission Already Granted", Toast.LENGTH_SHORT).show()
            Log.d("GRANTED","Sudah masuk sebelumnya")
            content()
        }
    }

    fun content() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Fetch data dari halaman 2
//        binding.rvUsers.layoutManager = LinearLayoutManager(this)
//        binding.rvUsers.adapter = adapter

//        viewModel.fetchUsers("2")
        loadFragment(HomeFragment())

        PermissionAndLocation.getCurrentLocation(fusedLocationClient) { lat, lon ->
            val appid = "2469d458b7ad46b9632c1497d5c2d9a2"
            val units = "imperial"
            viewModel.fetchWeather(lat.toString(), lon.toString(), appid, units)

            Toast.makeText(this, "Lat: $lat, Lon: $lon", Toast.LENGTH_LONG).show()
            Log.d("LATLONG", "$lat, $lon")
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

        val identity = viewModel.getIdentity()

        Log.d("CEKDATA","${viewModel.getIdentity()}")

        loadFragment(HomeFragment())
    }

    private fun loadFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
