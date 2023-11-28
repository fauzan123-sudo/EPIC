package com.example.epic.ui.fragment.menu

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.epic.R
import com.example.epic.databinding.FragmentAllMenuBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.readLoginResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMenuFragment : BaseFragment<FragmentAllMenuBinding>(FragmentAllMenuBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        clickHandle()
        checkUser()

    }

    private fun checkUser() {
        val userSaved = readLoginResponse()
        if (userSaved?.user?.role == "1") {
            binding.fb3.visibility = View.VISIBLE
        } else {
            binding.fb3.visibility = View.GONE
        }
    }

    private fun clickHandle() {
        binding.apply {
            mcCategory.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_listCategoryFragment)
            }
            mcSales.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_listSalesFragment)
            }
            mcProduct.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_listProductFragment)
            }
            mcStock.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_listStockFragment)
            }
            mcSeller.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_listSellerFragment)
            }
            mcProductReturn.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_productReturnFragment)
            }
            mcChart.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_statisticSellerFragment)
            }
            mcReport.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_reportFragment)
            }
            mcUserManagement.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_listStoreFragment)
            }
        }
    }

    private fun setUpToolbar() {
        binding.apply {
            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Semua Menu"
                )
            }
        }
    }
}