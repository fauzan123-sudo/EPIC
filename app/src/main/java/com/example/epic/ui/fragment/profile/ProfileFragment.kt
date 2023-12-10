package com.example.epic.ui.fragment.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.epic.R
import com.example.epic.databinding.FragmentProfileBinding
import com.example.epic.ui.activity.LoginActivity
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ProfileViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.TokenManager
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.deleteStoreName
import com.example.epic.util.deleteUserData
import com.example.epic.util.deleteUserId
import com.example.epic.util.readLoginResponse
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var uriImage: Uri

    @Inject
    lateinit var tokenManager: TokenManager
//    private val userSaved = readLoginResponse()!!

//    private val resultGallery =
//        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
//            result?.let { uri ->
//                try {
//                    val inputStream = requireContext().contentResolver.openInputStream(uri)
//                    val file = File(requireContext().cacheDir, "user_image.png")
//                    val outputStream = FileOutputStream(file)
//                    inputStream?.use { input ->
//                        outputStream.use { output ->
//                            input.copyTo(output)
//                        }
//                    }
//                    viewModel.requestUpdatePhoto(userSaved.user.id_user.toString(), file)
//                    loadDataUpdatePhoto(result)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    Log.e("Error", "Failed to create a temporary file")
//                }
//
//            } ?: Log.e("Error", "Result is null")
//        }
//
//    private val resultCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
//        try {
//            val inputStream = requireContext().contentResolver.openInputStream(uriImage)
//            val file = File(requireContext().cacheDir, "user_image.png")
//            val outputStream = FileOutputStream(file)
//            inputStream?.use { input ->
//                outputStream.use { output ->
//                    input.copyTo(output)
//                }
//            }
//            viewModel.requestUpdatePhoto(userSaved.user.id_user.toString(), file)
//            loadDataUpdatePhoto(uriImage)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e("Error", "Failed to create a temporary file")
//        }
//
//    }

    private fun logOut() {
        binding.btnLogOut.setOnClickListener {
            SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Konfirmasi Logout")
                .setContentText("Apakah Anda yakin ingin keluar?")
                .setConfirmText("Ya")
                .setConfirmClickListener {
                    viewModel.logOut()
                    viewModel.logOutResponse.observe(viewLifecycleOwner) {
                        when (it) {
                            is NetworkResult.Success -> {
                                hideLoading()
                                val response = it.data!!
                                if (response.status) {
                                    requireActivity().startActivity(
                                        Intent(
                                            requireActivity(),
                                            LoginActivity::class.java
                                        )
                                    )
                                    deleteUserData()
                                    tokenManager.deleteToken()

                                    val isDeleted: Boolean = deleteUserId()
                                    val isNameStoreDelete: Boolean = deleteStoreName()

                                    if (isDeleted && isNameStoreDelete) {
                                        println("Penghapusan berhasil.")
                                    } else {
                                        println("Penghapusan tidak berhasil atau data ID dan Nama Pengguna tidak ditemukan.")
                                    }

                                    requireActivity().finish()
                                }
                            }

                            is NetworkResult.Loading -> {
                                showLoading()
                            }

                            is NetworkResult.Error -> {
                                hideLoading()
                                showErrorMessage(it.message!!)
                                Log.e("TAG", "${it.message}")
                            }
                        }
                    }
                    it.dismissWithAnimation()
                }
                .setCancelText("Tidak")
                .setCancelClickListener { it.dismissWithAnimation() }
                .show()


        }
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

    private fun createImageUri(): Uri? {
        val image = File(requireContext().filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(requireContext(), "com.example.epic.provider", image)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uriImage = createImageUri()!!
        loadData()
        setUpToolbar()
//        touchImageCamera()
        logOut()
        gotoStoreAddress()
    }

    private fun gotoStoreAddress() {
        binding.mcStoreAddress.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_storeAddressFragment)
        }
    }

//    private fun touchImageCamera() {
//        binding.imgChangePhoto.setOnClickListener {
//            SweetAlertDialog(requireContext(), SweetAlertDialog.NORMAL_TYPE)
//                .setTitleText("Pilih Aksi")
//                .setContentText("Ambil gambar dari")
//                .setConfirmText("Galleri")
//                .setCancelText("Kamera")
//                .setConfirmClickListener {
//                    resultGallery.launch("image/*")
//                    it.dismissWithAnimation()
//                }
//                .setCancelClickListener {
//                    resultCamera.launch(uriImage)
////                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////                    resultCamera.launch(intent)
//                    it.dismissWithAnimation()
//                }
//                .show()
//        }
//
//    }

    private fun setUpToolbar() {
        binding.apply {
            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Profil"
                )
            }
        }
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

                    binding.mcFullProfile.setOnClickListener {
                        val action =
                            ProfileFragmentDirections.actionProfileFragmentToDetailProfileFragment(
                                data
                            )
                        findNavController().navigate(action)
                    }

                    Glide.with(requireContext())
                        .load(data.foto)
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.ic_no_image)
                        .into(binding.userImage)
                    binding.nameUser.text = data.nama_toko
                    if (data.role == "1") {
                        binding.tvRole.text = "Pemilik"
                    } else {
                        binding.tvRole.text = "Admin"
                    }
                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    showErrorMessage(it.message!!)
                }
            }
        }
    }


}