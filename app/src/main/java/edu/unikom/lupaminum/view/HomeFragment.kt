package edu.unikom.lupaminum.view

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import edu.unikom.lupaminum.R
import edu.unikom.lupaminum.databinding.ActivityMainBinding
import edu.unikom.lupaminum.databinding.FragmentCollectIdentityTempBinding
import edu.unikom.lupaminum.databinding.FragmentHomeBinding
import edu.unikom.lupaminum.utils.PermissionAndLocation
import edu.unikom.lupaminum.viewModel.UserViewModel
import edu.unikom.lupaminum.worker.WaterReminderWorker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var requestLocationPermissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupPermissionLauncher()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val identity = viewModel.getIdentity()

        if (viewModel.getSchedule() != null) {
            val schedule = viewModel.getSchedule()

            val scheduleTime = schedule?.time?.toLongOrNull() ?: 15L // default 15 menit

            binding.cardAlarm.visibility = View.VISIBLE
            binding.alarm.text = "Alarm terjadwal setiap ${scheduleTime} menit"
        } else {
            binding.cardAlarm.visibility = View.GONE
        }

        binding.txtSapa.text = "Hi, ${identity?.name ?: "Bro"}!"
        binding.btnSchedule.setOnClickListener {
            binding.btnSchedule.setImageResource(R.drawable.btn_schedule_hint)

            lifecycleScope.launch {
                delay(150)
                binding.btnSchedule.setImageResource(R.drawable.btn_schedule)
            }

            checkLocationPermissions(this, requestLocationPermissionLauncher)

        }
    }

    private fun setupPermissionLauncher() {
        requestLocationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

                if (fineGranted || coarseGranted) {
                    Toast.makeText(requireContext(), "Ijin GPS Diberikan", Toast.LENGTH_SHORT).show()
                    // lakukan sesuatu kalau permission granted
                } else {
                    // kalau ditolak
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // user centang "Don't ask again"
                        showGoToSettingsDialog(this)
                    } else {
                        Toast.makeText(requireContext(), "Ijin GPS tidak diberikan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun checkLocationPermissions(fragment: Fragment, launcher: ActivityResultLauncher<Array<String>>) {
        val context = fragment.requireContext()

        if (PermissionAndLocation.hasLocationPermission(context)) {
            Toast.makeText(context, "GPS Telah Aktif", Toast.LENGTH_SHORT).show()

            loadFragment(ScheduleSettingFragment())
        } else {
            when {
                fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) ||
                        fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                    // Tampilkan dialog penjelasan
                    showPermissionExplanationDialog(fragment, launcher)
                }
                else -> {
                    // User menolak + centang "Don't ask again" atau pertama kali request
                    launcher.launch(arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                }
            }
        }
    }

    private fun showPermissionExplanationDialog(
        fragment: Fragment,
        launcher: ActivityResultLauncher<Array<String>>
    ) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("Location Permission Needed")
            .setMessage("This app needs location access to show your current weather and nearby info.")
            .setPositiveButton("Allow") { _, _ ->
                launcher.launch(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showGoToSettingsDialog(fragment: Fragment) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("Permission Required")
            .setMessage("Location permission was denied with 'Don't ask again'. Please enable it in Settings to use this feature.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", fragment.requireContext().packageName, null)
                intent.data = uri
                fragment.startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun loadFragment(fragment : Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}