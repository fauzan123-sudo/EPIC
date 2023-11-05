package com.example.epic.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.example.epic.R
import com.example.epic.data.adapter.MonthAdapter
import com.example.epic.data.model.month.Month
import com.example.epic.databinding.FragmentHomeBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.util.getMonth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    @Inject
    lateinit var monthAdapter: MonthAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

            val monthArray = resources.getStringArray(R.array.month)
            val currentMonth = getMonth()
            val months = ArrayList<Month>()
            for (i in monthArray.indices) {
                months.add(Month(i + 1, monthArray[i], false))

            }
            if (months.isNotEmpty()) {
                months[currentMonth - 1].isSelected = true
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


}