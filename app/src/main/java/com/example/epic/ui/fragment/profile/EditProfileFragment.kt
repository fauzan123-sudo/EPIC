package com.example.epic.ui.fragment.profile

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.epic.data.model.user.profile.update.RequestUpdateProfile
import com.example.epic.databinding.FragmentEditProfileBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ProfileViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.convertDateFormat
import com.example.epic.util.formatDate
import com.example.epic.util.isValidDateFormat
import com.example.epic.util.setupMenu
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment :
    BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate) {

    private val args: EditProfileFragmentArgs by navArgs()

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpData()

    }

    private fun setUpData() {
        val title = args.dataTitle
        val dataValue = args.dataValue
        setUpToolbar()
        binding.tvTitle.text = title
        if (dataValue.isEmpty()) {
            when (title) {
                "Tanggal Lahir" -> {
                    calendarDate()
                }

                else -> {
                    enableEditText()
                    binding.edtStoreName.hint = "Harap isi $title"
                }
            }
        } else {
            when (title) {
                "Tanggal Lahir" -> {
                    calendarDate()
                    binding.edtStoreName.setText(dataValue)
                }

                else -> {
                    enableEditText()
                    binding.edtStoreName.setText(dataValue)
                }
            }
        }
    }

    private fun enableEditText() {
        binding.edtStoreName.isClickable = false
        binding.edtStoreName.isFocusable = true
        binding.edtStoreName.isFocusableInTouchMode = true
    }

    private fun disableEditText() {
        binding.edtStoreName.isClickable = true
        binding.edtStoreName.isFocusable = false
        binding.edtStoreName.isFocusableInTouchMode = false
    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            setupMenu(com.example.epic.R.menu.menu_action_text, { menuItem ->
                when (menuItem.itemId) {
                    com.example.epic.R.id.add_text_action -> {
                        val value = edtStoreName.text.toString().trim()
                        if (value.isEmpty()) {
                            val title = args.dataTitle
                            showErrorMessage("harap isi $title dulu")
                        } else {
                            val storeName = binding.edtStoreName.text.toString().trim()
                            if (isValidDateFormat(storeName)) {
                                val date = convertDateFormat(storeName)
                                handleEdit(date)
//                                showToast("Tanggal: $date")
                            } else {
                                handleEdit(storeName)
                            }
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

    private fun handleEdit(value:String) {
        viewModel.updateProfileRequest(args.userId, RequestUpdateProfile(args.dataName, value))
        viewModel.updateProfileResponse.observe(viewLifecycleOwner) {
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
                    showErrorMessage(it.message!!)
                }
            }
        }
    }

    private fun calendarDate() {
        disableEditText()
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText("Pilih Tanggal")
        val datePicker = builder.build()

        binding.edtStoreName.setOnClickListener {
            val tag = "DatePickerDialog_${System.currentTimeMillis()}"
            datePicker.show(parentFragmentManager, tag)
        }

        datePicker.addOnPositiveButtonClickListener { selectedDateInMillis ->
            val selectedDate = formatDate(selectedDateInMillis)
            binding.edtStoreName.setText(selectedDate)
        }
    }


}