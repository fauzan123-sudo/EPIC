package com.example.epic.ui.fragment.sales


import android.R
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
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
import com.example.epic.util.getUserId
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
        if (savedUser?.role == "1") {
            val userId = getUserId()?.toInt()!!
            setUpSpinner(userId)
        } else {
            val userId = savedUser!!.id_user
            setUpSpinner(userId)
        }
        setUpToolbar()
    }

    private fun setUpSpinner(userId: Int) {
        categoryViewModel.requestListCategory(userId)
        categoryViewModel.listCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
//                    categoryViewModel.listCategoryResponse.removeObservers(viewLifecycleOwner)
                    val response = it.data!!
                    val categories = response.data
                    if (categories.isEmpty()) {
                        disableCategorySpinner()
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
                                }
                            }
                        }
                    }
                }

                is NetworkResult.Loading -> {
                    showLoading()
//                    categoryViewModel.listCategoryResponse.removeObservers(viewLifecycleOwner)
                }

                is NetworkResult.Error -> {
                    hideLoading()
//                    categoryViewModel.listCategoryResponse.removeObservers(viewLifecycleOwner)
                    showErrorMessage(it.message!!)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onResume: ")

    }

    private fun disableCategorySpinner() {
        with(binding) {
            val spinnerCategory = spPickCategory
            spinnerCategory.isEnabled = false
            spinnerCategory.text.clear()
            tilCategory.hint = "data kosong"
            spinnerCategory.setAdapter(null)
        }
    }

    private fun disableProductSpinner() {
        with(binding) {
            spProduct.text.clear()
            spProduct.isEnabled = false
            tilProduct.hint = "data kosong"
            spProduct.setAdapter(null)
            productCode = ""
            etUnitProduct.text.clear()
        }
    }

    private fun showProduct(categoryId: Int) {
        categoryViewModel.requestProductByCategory(categoryId)
        categoryViewModel.basedCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
//                    categoryViewModel.basedCategoryResponse.removeObservers(viewLifecycleOwner)
                    val response = it.data!!
                    val dataProduct = response.data
                    if (dataProduct.isEmpty()) {
                        disableProductSpinner()
                    } else {
                        binding.spProduct.text.clear()
                        binding.tilProduct.hint = "Pilih Barang"
                        binding.spProduct.isEnabled = true
                        binding.spProduct.isEnabled = true
                        val productNames = dataProduct.map { product ->
                            product.nama_barang
                        }.toTypedArray()

                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.simple_dropdown_item_1line,
                            productNames
                        )

                        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

                        binding.spProduct.setAdapter(adapter)

                        binding.spProduct.setOnItemClickListener { _, _, position, _ ->
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
//                    categoryViewModel.basedCategoryResponse.removeObservers(viewLifecycleOwner)
                }

                is NetworkResult.Error -> {
                    hideLoading()
//                    categoryViewModel.basedCategoryResponse.removeObservers(viewLifecycleOwner)
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
//            Toast.makeText(requireContext(), productCode, Toast.LENGTH_SHORT).show()
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
                getCurrentDateTime(),
                getUserId()?.toInt() ?: savedUser!!.id_user
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