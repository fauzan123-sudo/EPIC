package com.example.epic.ui.fragment.product

import android.os.Bundle
import android.util.Log
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
import com.example.epic.util.getUserId
import com.example.epic.util.hideKeyboard
import com.example.epic.util.setupMenu
import com.mancj.materialsearchbar.MaterialSearchBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListProductFragment :
    BaseFragment<FragmentListProductBinding>(FragmentListProductBinding::inflate),
    ProductAdapter.ItemListener {

    private val productViewModel: ProductViewModel by viewModels()

    @Inject
    lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpData()
        searchProduct()

    }

    private fun searchProduct() {
        with(binding) {
            searchView.setHint("Cari Disini")
            searchView.setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
                override fun onSearchStateChanged(enabled: Boolean) {}

                override fun onSearchConfirmed(text: CharSequence?) {
                    view?.let { hideKeyboard(it) }
                    productViewModel.searchProduct(
                        text.toString(),
                        getUserId()?.toInt() ?: savedUser!!.id_user
                    )
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

    private fun setUpData() {
        productViewModel.readProduct(getUserId()?.toInt() ?: savedUser!!.id_user)
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
            productAdapter.listener = this@ListProductFragment
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

    override fun updateProduct(data: Data) {
        try {
            Log.d("TAG", "updateProduct: $data")
            val action =
                ListProductFragmentDirections.actionListProductFragmentToUpdateProductFragment(
                    data
                )
            findNavController().navigate(action)

        } catch (e: Exception) {
            Log.e("error", "updateCategory: ", e)
            showErrorMessage(e.toString())
        }
    }

    override fun deleteProduct(data: Data) {
        showWarningMessage("Barang") {
            productViewModel.deleteProduct(data.kode_barang.toString())
            productViewModel.deleteProductResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        hideLoading()
                        productViewModel.deleteProductResponse.removeObservers(viewLifecycleOwner)
                        val response = it.data!!
                        if (response.status) {
                            showSuccessDelete(response.data.message)

//                            val position = productAdapter.differ.currentList.indexOf(data)
//                            if (position != -1) {
//                                productAdapter.differ.currentList.toMutableList().removeAt(position)
//                                productAdapter.notifyItemRemoved(position)
//                            }else{
//                                Toast.makeText(requireContext(), "position is 1", Toast.LENGTH_SHORT).show()
//                            }
                        } else {
                            showErrorMessage(response.message)
                        }
                    }

                    is NetworkResult.Loading -> {
                        showLoading()
                    }

                    is NetworkResult.Error -> {
                        hideLoading()
                        productViewModel.deleteProductResponse.removeObservers(viewLifecycleOwner)
                        showErrorMessage(it.message!!)
                    }
                }
            }

        }
    }

}