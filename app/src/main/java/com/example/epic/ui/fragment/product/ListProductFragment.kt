package com.example.epic.ui.fragment.product

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.R
import com.example.epic.data.adapter.ProductAdapter
import com.example.epic.data.model.product.read.Data
import com.example.epic.databinding.FragmentListProductBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ProductViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.hideKeyboard
import com.example.epic.util.setupMenu
import com.mancj.materialsearchbar.MaterialSearchBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListProductFragment :
    BaseFragment<FragmentListProductBinding>(FragmentListProductBinding::inflate) {

    private val productViewModel: ProductViewModel by viewModels()

    @Inject
    lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        loadProduct()
        searchProduct()

    }

    private fun searchProduct() {
        with(binding) {
            searchView.setHint("Cari Disini")
            searchView.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
                override fun onSearchStateChanged(enabled: Boolean) {}

                override fun onSearchConfirmed(text: CharSequence?) {
                    view?.let { hideKeyboard(it) }
                    productViewModel.searchProduct(text.toString())
                    productViewModel.searchProductResponse.observe(viewLifecycleOwner) {
                        when (it) {
                            is NetworkResult.Success -> {
                                hideLoading()
                                val response = it.data!!.data
                                loadRecyclerview(response)
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

                override fun onButtonClicked(buttonCode: Int) {
                }

            })
        }
    }

    private fun loadProduct() {
        productViewModel.readProduct()
        productViewModel.readProductResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    loadRecyclerview(response.data)

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

    private fun loadRecyclerview(data: List<Data>) {
        productAdapter.differ.submitList(data)
        with(binding.rvProduct) {
            layoutManager =
                LinearLayoutManager(
                    requireContext()
                )
            adapter = productAdapter
        }
    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            setupMenu(R.menu.menu_action, { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_add -> {
                        findNavController().navigate(R.id.action_listProductFragment_to_addProductFragment)
                        true
                    }

                    else -> false
                }
            }, MenuItem.SHOW_AS_ACTION_ALWAYS)

            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Data Barang"
                )
            }
        }
    }

}