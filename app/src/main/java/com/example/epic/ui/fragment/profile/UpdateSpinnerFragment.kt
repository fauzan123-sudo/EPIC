package com.example.epic.ui.fragment.profile

import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.epic.data.model.user.profile.update.RequestUpdateProfile
import com.example.epic.databinding.FragmentUpdateSpinnerBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ProfileViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateSpinnerFragment :
    BaseFragment<FragmentUpdateSpinnerBinding>(FragmentUpdateSpinnerBinding::inflate) {

    private val args: UpdateSpinnerFragmentArgs by navArgs()
    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpData()
        setUpToolbar()
        genderSpinner()

    }

    private fun setUpData() {
        val title = args.dataTitle
        val dataValue = args.dataValue
        binding.tvTitle.text = "Jenis Kelamin"
        if (dataValue.isEmpty() || dataValue == "0") {
            binding.edtStoreName.hint = "pilih jenis kelamin"
        } else {
            if (dataValue == "1") {
                binding.edtStoreName.hint = "Perempuan"
            } else {
                binding.edtStoreName.hint = "Laki-laki"
            }
        }
    }

    private fun genderSpinner() {
        binding.apply {
            edtStoreName.inputType = InputType.TYPE_NULL
            val data = listOf("Perempuan", "Laki-laki")
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, data)
            edtStoreName.setAdapter(adapter)

            edtStoreName.setOnItemClickListener { _, _, position, _ ->
                val selectedGender = when (position) {
                    0 -> "1"
                    1 -> "2"
                    else -> ""
                }
            }
        }
    }

    private fun handleEdit(s: String) {
        val value = binding.edtStoreName.text.toString().trim()
        viewModel.updateProfileRequest(savedUser?.id_user.toString(), RequestUpdateProfile("jk", s))
        viewModel.updateProfileResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    if (response.status) {
                        showSuccessUpdate(response.data.message)
                    } else {
                        showErrorMessage(response.data.message)
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

    private fun showSuccessUpdate(message: String) {
        SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Sukses")
            .setContentText(message)
            .setConfirmClickListener {
                it.dismissWithAnimation()
            }
            .show()
    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            setupMenu(com.example.epic.R.menu.menu_action_text, { menuItem ->
                when (menuItem.itemId) {
                    com.example.epic.R.id.add_text_action -> {
                        val value = edtStoreName.text.toString().trim()
                        if (value.isEmpty()) {
                            showErrorMessage("harap isi ${args.dataTitle} dulu")
                        } else {
                            if (value == "Perempuan") handleEdit("1")
                            else handleEdit("2")
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
                    "Edit Profile"
                )
            }
        }
    }


}