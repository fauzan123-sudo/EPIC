package com.example.epic.ui.fragment.profile

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.epic.data.model.user.profile.update.RequestUpdateProfile
import com.example.epic.databinding.FragmentStoreAddressBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ProfileViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreAddressFragment :
    BaseFragment<FragmentStoreAddressBinding>(FragmentStoreAddressBinding::inflate) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpData()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            setupMenu(com.example.epic.R.menu.menu_action_text, { menuItem ->
                when (menuItem.itemId) {
                    com.example.epic.R.id.add_text_action -> {
                        if (edtStoreAddress.text.toString().trim().isEmpty()) {
                            showErrorMessage("harap isi alamat dulu")
                        } else {
                            updateData()
                        }

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
                    "Edit Alamat Toko"
                )
            }
        }
    }

    private fun updateData() {
        val storeAddress = binding.edtStoreAddress.text.toString().trim()
        viewModel.updateProfileRequest(
            savedUser?.id_user.toString(),
            RequestUpdateProfile("alamat_toko", storeAddress)
        )
        viewModel.updateProfileResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    if (response.status) {
                        showSuccessUpdate("Sukses mengubah alamat toko")
                    } else {
                        showErrorMessage("Gagal mengubah alamat toko")
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

    private fun setUpData() {
        viewModel.requestProfile(savedUser?.id_user ?: 0)
        viewModel.profileResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    binding.edtStoreAddress.setText(data.alamat_toko)
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

    private fun showSuccessUpdate(message: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Sukses")
            .setContentText(message)
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }
            .show()
    }

}