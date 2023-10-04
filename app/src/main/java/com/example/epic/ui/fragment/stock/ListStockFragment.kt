package com.example.epic.ui.fragment.stock

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.data.adapter.StockAdapter
import com.example.epic.databinding.FragmentListStockBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.StockViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListStockFragment : BaseFragment<FragmentListStockBinding>(FragmentListStockBinding::inflate){

    private val viewModel : StockViewModel by viewModels()
    @Inject
    lateinit var stockAdapter: StockAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadStock()
        setUpToolbar()

    }

    private fun setUpToolbar() {
        binding.apply {
            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Data Persediaan"
                )
            }
        }
    }

    private fun loadStock() {
        viewModel.requestListStock()
        viewModel.listStockResponse.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Success ->{
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    stockAdapter.differ.submitList(data)
                    binding.rvStock.apply {
                        layoutManager =
                            LinearLayoutManager(
                                requireContext()
                            )
                        adapter = stockAdapter
                    }
                }

                is NetworkResult.Loading ->{
                    showLoading()
                }

                is NetworkResult.Error ->{
                    hideLoading()
                    showErrorMessage(it.message!!)
                }
            }
        }
    }

}