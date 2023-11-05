package com.example.epic.ui.fragment.sales

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.R
import com.example.epic.data.adapter.SalesAdapter
import com.example.epic.data.model.sales.read.Data
import com.example.epic.databinding.FragmentListSalesBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.SalesViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListSalesFragment :
    BaseFragment<FragmentListSalesBinding>(FragmentListSalesBinding::inflate),
    SalesAdapter.ItemListener {

    private val viewModel: SalesViewModel by viewModels()

    @Inject
    lateinit var salesAdapter: SalesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpData()

    }

    private fun setUpData() {
        Log.d("mulai", "mulai onViewCreated: ")
        viewModel.listSalesRequest()
        viewModel.listSalesResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
//                    viewModel.listSalesResponse.removeObservers(viewLifecycleOwner)
                    hideLoading()
                    val response = it.data!!
                    salesAdapter.listener = this
                    salesAdapter.differ.submitList(response.data)
                    with(binding.rvSales) {
                        layoutManager =
                            LinearLayoutManager(
                                requireContext()
                            )
                        adapter = salesAdapter
                    }
                }

                is NetworkResult.Loading -> {
                    showLoading()

                }

                is NetworkResult.Error -> {
//                    viewModel.listSalesResponse.removeObservers(viewLifecycleOwner)
                    hideLoading()
                    showErrorMessage(it.message!!)
                }
            }
        }
    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            setupMenu(R.menu.menu_action, { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_add -> {
                        findNavController().navigate(R.id.action_listSalesFragment_to_addSalesFragment)
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
                    "Sales"
                )
            }
        }
    }

    override fun deleteSales(data: Data) {
        showWarningMessage("Sales") {
            viewModel.deleteSalesRequest(data.id_sales.toString())
            viewModel.deleteSalesResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        hideLoading()
                        val response = it.data!!
                        if (response.status) {
                            showSuccessDelete(response.message)
                            setUpData()
                        } else {
                            showErrorMessage(response.message)
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

    }

}