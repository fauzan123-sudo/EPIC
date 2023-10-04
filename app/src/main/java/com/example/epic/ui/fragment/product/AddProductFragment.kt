package com.example.epic.ui.fragment.product

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.epic.R
import com.example.epic.data.model.product.create.RequestAddProduct
import com.example.epic.databinding.FragmentAddProductBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.ui.viewModel.ProductViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddProductFragment :
    BaseFragment<FragmentAddProductBinding>(FragmentAddProductBinding::inflate) {

    val viewModel: ProductViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var selectedCategoryId = ""
    private var productCode = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpSpinner()
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
                            android.R.layout.simple_dropdown_item_1line,
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
        categoryViewModel.RequestProductByCategory(categoryId)
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
                            productCode = selectedProduct.kode_barang.toString()
                            Toast.makeText(requireContext(), productCode, Toast.LENGTH_SHORT)
                                .show()
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
            setupMenu(R.menu.menu_action_text, { menuItem ->
                when (menuItem.itemId) {
                    R.id.add_text_action -> {
                        handleAddProduct()
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
                    "Data Barang"
                )
            }
        }
    }

    private fun handleAddProduct() {
        val nameProduct = binding.etNameProduct.text.toString()
        val unitProduct = binding.etUnitProduct.text.toString()
        val minimumProduct = binding.etMinimumProduct.text.toString()
        if (selectedCategoryId == "") {
            showErrorMessage("harap pilih kategori dulu!!")
        } else if (productCode == "") {
            showErrorMessage("harap pilih barang dulu!!")
        } else if (nameProduct.isEmpty()) {
            showErrorMessage("harap isi nama barang dulu!!")
        } else if (minimumProduct.isEmpty()) {
            showErrorMessage("harap isi minimum barang dulu!!")
        } else if (unitProduct.isEmpty()) {
            showErrorMessage("harap isi satuan barang dulu!!")
        } else {
            addData(selectedCategoryId, productCode, nameProduct, unitProduct, minimumProduct)
        }
    }

    private fun addData(
        categoryId: String,
        productCode: String,
        nameProduct: String,
        unitProduct: String,
        minimumProduct: String
    ) {
        viewModel.createProduct(
            RequestAddProduct(
                productCode,
                nameProduct,
                unitProduct,
                categoryId,
                minimumProduct,
                )
        )
        viewModel.createProductResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    if (response.status) {
                        showSuccessMessage(response.message)
                    } else {
                        showErrorMessage(response.message)
                    }
                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    showErrorMessage(it.message ?: "error found")
                }
            }
        }
    }

}