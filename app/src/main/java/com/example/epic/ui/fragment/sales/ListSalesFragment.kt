package com.example.epic.ui.fragment.sales

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
import com.example.epic.util.getUserId
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
        Toast.makeText(
            requireContext(),
            "${getUserId()?.toInt() ?: savedUser?.id_user}",
            Toast.LENGTH_SHORT
        ).show()
        viewModel.listSalesRequest(getUserId()?.toInt() ?: savedUser!!.id_user)
        viewModel.listSalesResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
//                    viewModel.listSalesResponse.removeObservers(viewLifecycleOwner)
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    salesAdapter.listener = this
                    salesAdapter.differ.submitList(data)
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
                        viewModel.deleteSalesResponse.removeObservers(viewLifecycleOwner)
                        val response = it.data!!
                        if (response.status) {
                            if (response.data.message == "Jumlah persediaan tidak mencukupi") {
                                showErrorMessage(response.data.message)
                            } else {
                                showSuccessDelete(response.data.message)
                                setUpData()
                            }
                        } else {
                            showErrorMessage(response.message)
                        }
                    }

                    is NetworkResult.Loading -> {
                        showLoading()
                    }

                    is NetworkResult.Error -> {
                        hideLoading()
                        viewModel.deleteSalesResponse.removeObservers(viewLifecycleOwner)
                        showErrorMessage(it.message!!)
                    }
                }
            }
        }

    }

}