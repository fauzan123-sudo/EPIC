package com.example.epic.ui.fragment.store_management

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.epic.R
import com.example.epic.data.model.user.management.delete.RequestDeleteUser
import com.example.epic.data.model.user.management.update.RequestUpdateUser
import com.example.epic.databinding.FragmentUpdateStoreBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ProfileViewModel
import com.example.epic.ui.viewModel.UserManagementViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.TokenManager
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.readLoginResponse
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class UpdateStoreFragment :
    BaseFragment<FragmentUpdateStoreBinding>(FragmentUpdateStoreBinding::inflate) {

    private val args: UpdateStoreFragmentArgs by navArgs()

    private val profileViewModel: ProfileViewModel by viewModels()

    private lateinit var uriImage: Uri

    @Inject
    lateinit var tokenManager: TokenManager
    private val userSaved = readLoginResponse()!!

    private val viewModel: UserManagementViewModel by viewModels()

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
                    profileViewModel.requestUpdatePhoto(args.userData.id_user.toString(), file)
                    loadDataUpdatePhoto(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("Error", "Failed to create a temporary file")
                }

            } ?: Log.e("Error", "Result is null")
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
            profileViewModel.requestUpdatePhoto(userSaved.user.id_user.toString(), file)
            loadDataUpdatePhoto(uriImage)
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Error", "Failed to create a temporary file")
        }

    }

    private fun loadDataUpdatePhoto(result: Uri) {
        profileViewModel.updateProfilePhoto.observe(viewLifecycleOwner) {
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
                    Log.e("TAG", "${it.message}")
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
        touchImageCamera()
        setUpToolbar()
        deleteUser()
    }


    private fun deleteUser() {
        binding.btnDeleteUser.setOnClickListener {
            showWarningMessage("Pengguna") {
                viewModel.requestDeleteUser(RequestDeleteUser(args.userData.id_user))
                viewModel.deleteUserResponse.observe(viewLifecycleOwner) {
                    when (it) {
                        is NetworkResult.Success -> {
                            hideLoading()
                            val response = it.data!!
                            val data = response.data
                            if (response.status) {
                                showSuccessMessage(data.message)
                            } else {
                                showErrorMessage(data.message)
                            }
                        }

                        is NetworkResult.Loading -> {
                            showLoading()
                        }

                        is NetworkResult.Error -> {
                            hideLoading()
                            Log.e("TAG", "deleteUser: ${it.message}")
                            showErrorMessage(it.message!!)
                        }
                    }
                }
            }
        }
    }

    private fun loadData() {
        val data = args.userData
        binding.edtStoreName.setText(data.nama_toko)
        binding.edtUsername.setText(data.username)
        binding.edtPassword.setText(data.password_show)
        Glide.with(requireContext())
            .load(data.foto)
            .error(R.drawable.ic_no_image)
            .placeholder(R.drawable.progress_animation)
            .into(binding.userImage)
        if (data.role == "1") {
            binding.btnDeleteUser.visibility = View.GONE
        } else {
            binding.btnDeleteUser.visibility = View.VISIBLE
        }
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
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            setupMenu(R.menu.menu_action_text, { menuItem ->
                when (menuItem.itemId) {
                    R.id.add_text_action -> {
                        handleInput()
                        true
                    }

                    else -> false
                }
            }, MenuItem.SHOW_AS_ACTION_ALWAYS)


            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Edit Pengguna"
                )
            }
        }
    }

    private fun handleInput() {
        binding.apply {
            val getNameStore = edtStoreName.text.toString()
            val getUserName = edtUsername.text.toString()
            val getPassword = edtPassword.text.toString()
            if (getNameStore.isEmpty()) {
                edtStoreName.error = "Harap isi Nama"
            } else if (getUserName.isEmpty()) {
                edtUsername.error = "Harap isi Username"
            } else if (getPassword.isEmpty()) {
                edtPassword.error = "Harap isi Password"
            } else {
                handleButtonClick(getNameStore, getUserName, getPassword)
            }
        }
    }

    private fun handleButtonClick(
        nameStore: String,
        userName: String,
        password: String
    ) {
        viewModel.requestUpdateUser(
            RequestUpdateUser(
                userName,
                password,
                args.userData.id_user,
                2,
                nameStore
            )
        )
        viewModel.updateUserResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    if (response.status) {
                        showSuccessMessage(response.data.message)
                    } else {
                        showErrorMessage(response.data.message)
                    }
                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    Log.e("TAG", "error Update user: ${it.message}")
                    showErrorMessage(it.message!!)
                }
            }
        }

    }

}