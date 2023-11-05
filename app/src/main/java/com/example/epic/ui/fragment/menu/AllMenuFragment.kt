package com.example.epic.ui.fragment.menu

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.epic.R
import com.example.epic.databinding.FragmentAllMenuBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.util.configureToolbarBackPress
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllMenuFragment : BaseFragment<FragmentAllMenuBinding>(FragmentAllMenuBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()

        clickHandle()

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
            mcGrafic.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_statisticSellerFragment)
            }
            mcReport.setOnClickListener {
                findNavController().navigate(R.id.action_allMenuFragment_to_reportFragment)
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