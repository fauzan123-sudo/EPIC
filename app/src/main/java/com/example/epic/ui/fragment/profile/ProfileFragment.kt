package com.example.epic.ui.fragment.profile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.epic.databinding.FragmentProfileBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ProfileViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.TokenManager
import com.example.epic.util.readLoginResponse
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var uriImage: Uri

    @Inject
    lateinit var tokenManager: TokenManager
    private val userSaved = readLoginResponse()!!

    private val resultGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            result?.let { uri ->
                try {
                    val inputStream = requireContext().contentResolver.openInputStream(uri)
                    val file = File(requireContext().cacheDir, "user_image.png")
                    val outputStream = FileOutputStream(file)
                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                    viewModel.requestUpdatePhoto(userSaved.user.id_user.toString(), file)
                    loadDataUpdatePhoto(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("Error", "Failed to create a temporary file")
                }

            } ?: Log.e("Error", "Result is null")
        }

    private fun loadDataUpdatePhoto(result: Uri) {
        viewModel.updateProfilePhoto.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    if (response.status) {
                        binding.userImage.setImageURI(result)
                    }
                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    showErrorMessage(it.message!!)
                }

                else -> {}
            }
        }
    }

    private val resultCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uriImage)
            val file = File(requireContext().cacheDir, "user_image.png")
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            viewModel.requestUpdatePhoto(userSaved.user.id_user.toString(), file)
            loadDataUpdatePhoto(uriImage)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Error", "Failed to create a temporary file")
        }

    }

    private fun createImageUri(): Uri? {
        val image = File(requireContext().filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(requireContext(), "com.example.epic.provider", image)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uriImage = createImageUri()!!
        loadData()
        setUpToolbar()
        touchImageCamera()
    }

    private fun touchImageCamera() {
        binding.imgChangePhoto.setOnClickListener {
            SweetAlertDialog(requireContext(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("Pilih Aksi")
                .setContentText("Ambil gambar dari")
                .setConfirmText("Galleri")
                .setCancelText("Kamera")
                .setConfirmClickListener {
                    resultGallery.launch("image/*")
                    it.dismissWithAnimation()
                }
                .setCancelClickListener {
                    resultCamera.launch(uriImage)
//                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    resultCamera.launch(intent)
                    it.dismissWithAnimation()
                }
                .show()
        }

    }

    private fun setUpToolbar() {

    }

    private fun loadData() {
        val userSaved = readLoginResponse()
        viewModel.requestProfile(userSaved!!.user.id_user)
        viewModel.profileResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    Glide.with(requireContext())
                        .load(data.foto)
                        .into(binding.userImage)
//                    binding.userImage.loadRoundedImage(data.foto)
                    binding.nameUser.text = data.nama


                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    showErrorMessage(it.message!!)
                }

                else -> {}
            }
        }
    }


}