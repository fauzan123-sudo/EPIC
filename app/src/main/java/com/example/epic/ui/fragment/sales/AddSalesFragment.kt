package com.example.epic.ui.fragment.sales


import android.R
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.epic.data.model.sales.create.RequestCreateSales
import com.example.epic.databinding.FragmentAddSalesBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.ui.viewModel.SalesViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getCurrentDateTime
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSalesFragment : BaseFragment<FragmentAddSalesBinding>(FragmentAddSalesBinding::inflate) {

    private val categoryViewModel: CategoryViewModel by viewModels()
    private val salesViewModel: SalesViewModel by viewModels()
    private var selectedCategoryId = ""
    private var productCode = ""
    private var productId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSpinner()
        setUpToolbar()
    }

    private fun setUpSpinner() {
        binding.designSpinner.hint = "Pilih Barang"
        categoryViewModel.requestListCategory()
        categoryViewModel.listCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val categories = response.data
                    if (categories.isEmpty()) {
                        disableSpinner()
                    } else {
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
                                    Toast.makeText(
                                        requireContext(),
                                        "$categoryId",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else {
                                    disableSpinner()
                                }
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

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: ")

    }

    private fun disableSpinner() {
        with(binding) {
            designSpinner.hint = "Data Kosong"
            designSpinner.isEnabled = false
            designSpinner.isClickable = false
            designSpinner.isFocusable = false
            designSpinner.isFocusableInTouchMode = false
            designSpinner.dismissDropDown()
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
                    if (dataProduct.isEmpty()) {
                        disableSpinner()
                    } else {
                        binding.designSpinner.hint = "Pilih Barang"
                        binding.designSpinner.isEnabled = true
                        val productNames = dataProduct.map { product ->
                            product.nama_barang
                        }.toTypedArray()

                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.simple_dropdown_item_1line,
                            productNames
                        )

                        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

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
                                binding.etUnitProduct.setText(selectedProduct.satuan)
                            } else {
                                productCode = ""
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

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            setupMenu(com.example.epic.R.menu.menu_action_text, { menuItem ->
                when (menuItem.itemId) {
                    com.example.epic.R.id.add_text_action -> {
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
                    "Tambah Sales"
                )
            }
        }
    }

    private fun handleAddProduct() {
        val salesInput = binding.etSalesInput.text.toString()
        if (selectedCategoryId == "") {
            showErrorMessage("harap pilih kategori dulu!!")
        } else if (productCode == "") {
            showErrorMessage("harap pilih barang dulu!!")
        } else if (salesInput.isEmpty()) {
            showErrorMessage("harap isi satuan barang dulu!!")
        } else {
            addData(salesInput)
        }
    }

    private fun addData(
        salesInput: String
    ) {
        salesViewModel.createSalesRequest(
            RequestCreateSales(
                productId,
                salesInput,
                getCurrentDateTime()
            )
        )
        salesViewModel.createSalesResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    if (response.status) {
                        showSuccessMessage(response.data.message)
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