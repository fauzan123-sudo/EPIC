package com.example.epic.ui.fragment.stock

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.epic.data.adapter.StockAdapter
import com.example.epic.data.model.category.read.Data
import com.example.epic.databinding.FragmentListStockBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListStockFragment :
    BaseFragment<FragmentListStockBinding>(FragmentListStockBinding::inflate),
    StockAdapter.ItemListener {

    private val viewModel: CategoryViewModel by viewModels()

    @Inject
    lateinit var stockAdapter: StockAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadStock()
        setUpToolbar()
//        searchProduct()

    }

//    private fun searchProduct() {
//        with(binding) {
//            searchView.setHint("Cari Disini")
//            searchView.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
//                override fun onSearchStateChanged(enabled: Boolean) {
//
//                }
//
//                override fun onSearchConfirmed(text: CharSequence?) {
//                    view?.let { hideKeyboard(it) }
//                    Toast.makeText(requireContext(), "search Confirm", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onButtonClicked(buttonCode: Int) {
//
//                }
//
//            })
//        }
//    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbars.toolbar)
            view?.let {
                configureToolbarBackPress(
                    toolbars.toolbar,
                    it,
                    requireActivity(),
                    "Persediaan Barang"
                )
            }
        }
    }

    private fun loadStock() {
        viewModel.requestListCategory()
        viewModel.listCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    stockAdapter.differ.submitList(data)
                    stockAdapter.listener = this@ListStockFragment
                    binding.rvStock.apply {
                        layoutManager =
                            GridLayoutManager(
                                requireContext(), 2
                            )
                        adapter = stockAdapter
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

    override fun pickCategory(data: Data) {
        val action =
            ListStockFragmentDirections.actionListStockFragmentToListCategoryByStockFragment(data)
        findNavController().navigate(action)
    }

}