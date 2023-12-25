package com.example.epic.ui.fragment.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.epic.databinding.FragmentDetailProfileBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ProfileViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.formatStringDate
import com.example.epic.util.readLoginResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailProfileFragment :
    BaseFragment<FragmentDetailProfileBinding>(FragmentDetailProfileBinding::inflate) {
    private val viewModel: ProfileViewModel by viewModels()
    private val args: DetailProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpData()
        setUpToolbar()
        getData()
    }

    private fun setUpData() {
        binding.apply {

            val userSaved = readLoginResponse()
            viewModel.requestProfile(userSaved!!.user.id_user)
            viewModel.profileResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        hideLoading()
                        val response = it.data!!
                        val data = response.data

                        edtFullName.text = data.nama ?: "-"
                        edtAddress.text = data.alamat ?: "-"
                        edtBirth.text = data.tempat_lahir ?: "-"
                        val birthday = data.ttl
                        if (birthday != null) {
                            edtDate.text = formatStringDate(data.ttl)
                        } else {
                            edtDate.text = "-"
                        }
                        val gender = data.jk
                        if (gender == null) {
                            edtGender.text = "-"
                        }else if (data.jk == "1") {
                            edtGender.text = "Perempuan"
                        } else if (data.jk == "2") {
                            edtGender.text = "Laki-laki"
                        }
                        edtNoTlp.text = data.no_tlp ?: "-"
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

    private fun getData() {
        binding.apply {
            mcFullName.setOnClickListener {
                gotoUpdateProfile(edtFullName.text.toString().trim(), "Nama Lengkap", "nama")
            }
            mcBirth.setOnClickListener {
                gotoUpdateProfile(edtBirth.text.toString().trim(), "Tempat Lahir", "tempat_lahir")
            }
            mcDate.setOnClickListener {
                gotoUpdateProfile(edtDate.text.toString().trim(), "Tanggal Lahir", "ttl")
            }
            mcGender.setOnClickListener {
                goToUpdateGender(edtGender.text.toString().trim())
            }
            mcNoTlp.setOnClickListener {
                gotoUpdateProfile(edtNoTlp.text.toString().trim(), "No Telepon", "no_tlp")
            }
            mcAddress.setOnClickListener {
                gotoUpdateProfile(edtAddress.text.toString().trim(), "Alamat", "alamat")
            }

        }
    }

    private fun goToUpdateGender(value: String) {
        val action =
            DetailProfileFragmentDirections.actionDetailProfileFragmentToUpdateSpinnerFragment(
                value,
                "jk"
            )
        findNavController().navigate(action)
    }

    private fun gotoUpdateProfile(value: String, s: String, name: String) {
        val action =
            DetailProfileFragmentDirections.actionDetailProfileFragmentToEditProfileFragment(
                value,
                s,
                name,
                args.detailProfile.id_user.toString()
            )
        findNavController().navigate(action)
    }

    private fun setUpToolbar() {
        binding.apply {
            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Profil Lengkap"
                )
            }
        }
    }

}