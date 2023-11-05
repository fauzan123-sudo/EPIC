package com.example.epic.ui.fragment.stock

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.epic.data.adapter.StockAdapter
import com.example.epic.databinding.FragmentListStockBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.StockViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.hideKeyboard
import com.mancj.materialsearchbar.MaterialSearchBar
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
        searchProduct()

    }

    private fun searchProduct() {
        with(binding){
            searchView.setHint("Cari Disini")
            searchView.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
                override fun onSearchStateChanged(enabled: Boolean) {

                }

                override fun onSearchConfirmed(text: CharSequence?) {
                    view?.let { hideKeyboard(it) }
                    Toast.makeText(requireContext(), "search Confirm", Toast.LENGTH_SHORT).show()
                }

                override fun onButtonClicked(buttonCode: Int) {

                }

            })
        }
    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.toolbar)
            view?.let {
                configureToolbarBackPress(
                    toolbar.toolbar,
                    it,
                    requireActivity(),
                    "Persediaan Barang"
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
                            GridLayoutManager(
                                requireContext(),2
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