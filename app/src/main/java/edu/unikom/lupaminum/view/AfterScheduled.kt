package edu.unikom.lupaminum.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import edu.unikom.lupaminum.R
import edu.unikom.lupaminum.databinding.FragmentAfterScheduledBinding
import edu.unikom.lupaminum.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AfterScheduled : Fragment() {

    private var _binding: FragmentAfterScheduledBinding? = null
    private val binding get() = _binding!!

    private var level: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        level = arguments?.getString("LEVEL_KEY")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAfterScheduledBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtAfterschedule.text = level

        lifecycleScope.launch {
            delay(3000)
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance(level: String): AfterScheduled {
            val fragment = AfterScheduled()
            val args = Bundle()
            args.putString("LEVEL_KEY", level)
            fragment.arguments = args
            return fragment
        }
    }
}