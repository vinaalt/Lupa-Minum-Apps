package edu.unikom.lupaminum.view

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import edu.unikom.lupaminum.R
import edu.unikom.lupaminum.databinding.FragmentCollectIdentityTempBinding
import edu.unikom.lupaminum.viewModel.UserViewModel

@AndroidEntryPoint //harus ada ini vin! Bug cannot create an instance of class. Harus ada ini karena oake Hilt! Kalau tidak ada anotasi ini, Hilt tidak akan inject ViewModel
class CollectIdentityTempFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentCollectIdentityTempBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        // Agar tidak bisa ditutup dengan klik luar
        dialog.setCanceledOnTouchOutside(false)
        isCancelable = false

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectIdentityTempBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSimpan.setOnClickListener {
            val identity = binding.txtInputIdentity.text.toString()
            viewModel.saveIdentity(identity)

            if (viewModel.isIdentitySaved()) {
                Toast.makeText(requireContext(), "Identity sudah tersimpan", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Belum ada identity", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}