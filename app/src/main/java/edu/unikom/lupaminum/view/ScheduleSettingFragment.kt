package edu.unikom.lupaminum.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import edu.unikom.lupaminum.R
import edu.unikom.lupaminum.databinding.FragmentScheduleSettingBinding
import edu.unikom.lupaminum.utils.NeedWaterLevel.needWaterLevel
import edu.unikom.lupaminum.utils.PermissionAndLocation
import edu.unikom.lupaminum.viewModel.UserViewModel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleSettingFragment : Fragment() {

    private var _binding: FragmentScheduleSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScheduleSettingBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radioGroupOptions.setOnCheckedChangeListener { _, checkedId ->
            binding.inputScheduleRange.visibility = if (checkedId == R.id.lainnya) View.VISIBLE else View.GONE
        }

        binding.btnSimpanSchedule.setOnClickListener {
            Toast.makeText(requireContext(), "TERCLICK", Toast.LENGTH_SHORT).show()

            val selectedId = binding.radioGroupOptions.checkedRadioButtonId

            if (selectedId != -1) {
                val selectedRadioButton = binding.root.findViewById<RadioButton>(selectedId)
                val value: String = if (selectedId == R.id.lainnya) {
                    binding.txtInputScheduleRange.text.toString().trim()
                } else {
                    selectedRadioButton.tag?.toString() ?: selectedRadioButton.text.toString()
                }
                Toast.makeText(requireContext(), "${value}", Toast.LENGTH_SHORT).show()

                viewModel.saveSchedule(value)

                Toast.makeText(requireContext(), "BEHASIL SAMPAI SINI", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "${viewModel.isScheduleSaved()}", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "${viewModel.getSchedule()}", Toast.LENGTH_SHORT).show()

                if (viewModel.isScheduleSaved()) {
                    val scheduleValue = viewModel.getSchedule()?.time?.toLong()
                    if (scheduleValue != null) {
                        Toast.makeText(requireContext(), "Value saved", Toast.LENGTH_SHORT).show()

                        PermissionAndLocation.getCurrentLocation(fusedLocationClient) { lat, lon ->
                            val appid = "2469d458b7ad46b9632c1497d5c2d9a2"
                            val units = "imperial" //Inget ini vin, imperial untuk fahrenheit

                            // panggil fetchWeather setelah lokasi tersedia
                            viewModel.fetchWeather(lat.toString(), lon.toString(), appid, units)

                            viewModel.weatherData.observe(viewLifecycleOwner) { result ->
                                result?.let {
                                    val temp = it.main.temp
                                    val humidity = it.main.humidity

                                    val level = needWaterLevel(temp, humidity)

                                    loadFragment(AfterScheduled.newInstance(level))

                                    Log.d("WEATHER", "Temperature: $temp, Humidity: $humidity")
                                } ?: run {
                                    Log.d("WEATHER", "Failed to fetch weather")
                                }
                            }
                            Toast.makeText(requireContext(), "Lat: $lat, Lon: $lon", Toast.LENGTH_LONG).show()
                            Log.d("LATLONG", "$lat, $lon")
                        }
                    } else {
                        Toast.makeText(requireContext(), "Waktu tidak tersedia", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Pilih salah satu opsi terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
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