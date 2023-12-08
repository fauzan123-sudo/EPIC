package com.example.epic.ui.fragment.product_return

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.epic.data.model.returnProduct.RequestAddReturn
import com.example.epic.databinding.FragmentAddProductReturnBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.ui.viewModel.ProductReturnViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getCurrentDateTime
import com.example.epic.util.getUserId
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductReturnFragment :
    BaseFragment<FragmentAddProductReturnBinding>(FragmentAddProductReturnBinding::inflate) {

    private val viewModel: ProductReturnViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var selectedCategoryId = ""
    private var productCode = ""
    private var productId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpToolbar()
        if (savedUser?.role == "1") {
            setUpSpinner(getUserId()!!.toInt())
        } else {
            setUpSpinner(savedUser!!.id_user)
        }

    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            setupMenu(com.example.epic.R.menu.menu_action_text, { menuItem ->
                when (menuItem.itemId) {
                    com.example.epic.R.id.add_text_action -> {
                        if (savedUser?.role == "1") {
                            val userId = getUserId()?.toInt()!!
                            handleAddProduct(userId)
                        } else {
                            handleAddProduct(savedUser!!.id_user)
                        }
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
                    "Tambah Pengembalian"
                )
            }
        }
    }

    private fun handleAddProduct(userId: Int) {
        val salesInput = binding.etSalesInput.text.toString()
        if (selectedCategoryId == "") {
            showErrorMessage("harap pilih kategori dulu!!")
        } else if (productCode == "") {
            showErrorMessage("harap pilih barang dulu!!")
        } else if (salesInput.isEmpty()) {
            showErrorMessage("harap isi jumlah barang dulu!!")
        } else {
            addDataReturn(userId)
        }
    }

    private fun setUpSpinner(idUser: Int) {
        categoryViewModel.requestListCategory(idUser)
        categoryViewModel.listCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
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
                                    Toast.makeText(
                                        requireContext(),
                                        "$categoryId",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
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

    private fun showProduct(categoryId: Int) {
        categoryViewModel.requestProductByCategory(categoryId)
        categoryViewModel.basedCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
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
                            android.R.layout.simple_dropdown_item_1line,
                            productNames
                        )

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

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

    private fun addDataReturn(userId: Int) {
        val amountSeller = binding.etSalesInput.text.toString()
        val date = getCurrentDateTime()
        viewModel.createProductReturnRequest(
            RequestAddReturn(
                productId,
                amountSeller,
                date,
                userId
            )
        )
        viewModel.createReturnResponse.observe(viewLifecycleOwner) {
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

                else -> {}
            }
        }
    }


}