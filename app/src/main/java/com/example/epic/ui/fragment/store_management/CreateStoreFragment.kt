package com.example.epic.ui.fragment.store_management

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.epic.R
import com.example.epic.data.model.user.management.create.RequestCreateUser
import com.example.epic.databinding.FragmentCreateStoreBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.UserManagementViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateStoreFragment :
    BaseFragment<FragmentCreateStoreBinding>(FragmentCreateStoreBinding::inflate) {

    private val viewModel: UserManagementViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        handleSpinner()

    }

    private fun handleSpinner() {
        val genderOptions = arrayOf(
            getString(R.string.female),
            getString(R.string.male),
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            genderOptions
        )



    }

    private fun handleButtonClick(
        getNameStore: String,
        getUserName: String,
        getPassword: String,
    ) {
        viewModel.requestCreateUser(
            RequestCreateUser(
                getNameStore,
                getUserName,
                getPassword,
            )
        )

        viewModel.createUserResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    if (response.status) {
                        showSuccessMessage(
                            data.message
                        )
                    } else {
                        showErrorMessage(data.message)
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


    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            setupMenu(R.menu.menu_action_text, { menuItem ->
                when (menuItem.itemId) {
                    R.id.add_text_action -> {
//                        handleInput()
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
                    "Tambah Pengguna"
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

}