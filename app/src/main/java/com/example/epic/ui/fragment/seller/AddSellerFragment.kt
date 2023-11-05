package com.example.epic.ui.fragment.seller

import android.R
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.epic.data.model.seller.create.RequestCreateSeller
import com.example.epic.databinding.FragmentAddSellerBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.ui.viewModel.SellerViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getCurrentDateTime
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSellerFragment :
    BaseFragment<FragmentAddSellerBinding>(FragmentAddSellerBinding::inflate) {

    private val sellerViewModel: SellerViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var selectedCategoryId = ""
    private var productCode = ""
    private var productId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpinner()
        setUpToolbar()

    }

    private fun setUpSpinner() {
        categoryViewModel.requestListCategory()
        categoryViewModel.listCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val categories = response.data

                    val categoryNames = categories.map { category ->
                        category.nama_kategori
                    }.toTypedArray()

                    if (response.status) {
                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.simple_dropdown_item_1line,
                            categoryNames
                        )

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        binding.spPickCategory.setAdapter(adapter)
                        binding.spPickCategory.setOnItemClickListener { _, _, position, _ ->
                            val selectedCategoryName: String =
                                adapter.getItem(position) ?: return@setOnItemClickListener

                            val selectedData =
                                categories.find { category ->
                                    category.nama_kategori == selectedCategoryName
                                }
                            if (selectedData != null) {
                                selectedCategoryId = selectedData.id_kategori.toString()
                                val categoryId = selectedData.id_kategori
                                showProduct(categoryId)
                                Toast.makeText(requireContext(), "$categoryId", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
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

    private fun showProduct(categoryId: Int) {
        categoryViewModel.requestProductByCategory(categoryId)
        categoryViewModel.basedCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val dataProduct = response.data
                    val productNames = dataProduct.map { product ->
                        product.nama_barang
                    }.toTypedArray()

                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        productNames
                    )

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    binding.designSpinner.setAdapter(adapter)

                    binding.designSpinner.setOnItemClickListener { _, _, position, _ ->
                        val selectedProductName =
                            adapter.getItem(position) ?: return@setOnItemClickListener
                        val selectedProduct = dataProduct.find { product ->
                            product.nama_barang == selectedProductName
                        }
                        if (selectedProduct != null) {
                            productCode = selectedProduct.kode_barang
                            productId = selectedProduct.id_barang.toString()
                        }
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
            setupMenu(com.example.epic.R.menu.menu_action_text, { menuItem ->
                when (menuItem.itemId) {
                    com.example.epic.R.id.add_text_action -> {
                        addDataSeller()
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
                    "Tambah Penjualan"
                )
            }
        }
    }

    private fun addDataSeller() {
        val amountSeller = binding.etMinimumProduct.text.toString()
        val date = getCurrentDateTime()
        sellerViewModel.requestCreateSeller(
            RequestCreateSeller(
                productId,
                amountSeller,
                date
            )
        )
        sellerViewModel.createSellerResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    showSuccessMessage(response.data.message)
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