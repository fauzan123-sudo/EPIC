package com.example.epic.ui.fragment.stock

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.data.adapter.StockProductAdapter
import com.example.epic.databinding.FragmentListCategoryByStockBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListCategoryByStockFragment :
    BaseFragment<FragmentListCategoryByStockBinding>(FragmentListCategoryByStockBinding::inflate) {

    private val args: ListCategoryByStockFragmentArgs by navArgs()

    private val categoryViewModel: CategoryViewModel by viewModels()

    @Inject
    lateinit var stockCategoryAdapter: StockProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpData()

    }

    private fun setUpData() {
        categoryViewModel.requestSearchByCategory(args.dataCategory.id_kategori)
        categoryViewModel.searchCategory.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    stockCategoryAdapter.differ.submitList(data)
                    with(binding.rvProduct) {
                        layoutManager =
                            LinearLayoutManager(
                                requireContext()
                            )
                        adapter = stockCategoryAdapter
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

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Persediaan Barang"
                )
            }
        }
    }

}