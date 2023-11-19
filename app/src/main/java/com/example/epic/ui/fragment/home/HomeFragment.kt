package com.example.epic.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bumptech.glide.Glide
import com.example.epic.R
import com.example.epic.data.adapter.MonthAdapter
import com.example.epic.data.model.month.Month
import com.example.epic.databinding.FragmentHomeBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ProfileViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.getMonth
import com.example.epic.util.readLoginResponse
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    MonthAdapter.ItemListener {

    @Inject
    lateinit var monthAdapter: MonthAdapter

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()

        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                if (navController.currentDestination?.id == R.id.homeFragment) {
                    showExitConfirmationDialog()
                } else {
                    navController.navigateUp()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callBack)

        binding.apply {

            hIncomingMenu.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_listSalesFragment)
            }


            tvLookAll.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_allMenuFragment)
            }

            imgLookAll.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_allMenuFragment)
            }

            hStockMenu.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_listStockFragment)
            }

            mcSeller.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_listSellerFragment)
            }

            val currentMonth = getMonth()
            val months = listOf(
                Month(1, "Januari"),
                Month(2, "Februari"),
                Month(3, "Maret"),
                Month(4, "April"),
                Month(5, "Mei"),
                Month(6, "Juni"),
                Month(7, "Juli"),
                Month(8, "Agustus"),
                Month(9, "September"),
                Month(10, "Oktober"),
                Month(11, "November"),
                Month(12, "Desember")
            )

            for (month in months) {
                if (month.monthNumber == currentMonth) {
                    month.isSelected = true
                }
            }

            monthAdapter.differ.submitList(months)
            rvMonth.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = monthAdapter
            }
            imgProfile.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
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
                    Glide.with(requireContext())
                        .load(data.foto)
                        .placeholder(R.drawable.progress_animation)
                        .into(binding.imgProfile)

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

    private fun showExitConfirmationDialog() {
        SweetAlertDialog(requireContext(), SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Apakah Anda yakin?")
            .setContentText("")
            .setConfirmText("Ya, Keluar")
            .setCancelText("Batal")
            .setConfirmClickListener {
                it.dismissWithAnimation()
                requireActivity().finish()
            }
            .setCancelClickListener {
                it.dismissWithAnimation()
            }
            .show()
    }

    override fun onClickItem(data: Month) {
        TODO("Not yet implemented")
    }


}