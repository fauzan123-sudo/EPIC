package com.example.epic.ui.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.epic.databinding.FragmentImagePickerBinding
import com.example.epic.ui.viewModel.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePickerFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentImagePickerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by viewModels()


    private val resultContract = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
        if (result != null) {
            Log.d("image picker gallery ", ": is not null : $result")
            viewModel.selectedImage.value = result
        }
        dismiss()
    }


    private val takePictureContract = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Gambar dari kamera telah diambil, Anda dapat mengirimnya ke ProfileFragment
            // Anda perlu mengakses Uri gambar yang diambil dari kamera di sini.
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.buttonClickCamera.observe(viewLifecycleOwner) {

        }

        binding.imgPickGallery.setOnClickListener {
            resultContract.launch("image/*")
        }

        binding.imgPickCamera.setOnClickListener {

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}