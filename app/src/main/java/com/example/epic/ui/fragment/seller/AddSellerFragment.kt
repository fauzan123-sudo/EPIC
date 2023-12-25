package com.example.epic.ui.fragment.seller

import android.graphics.Color
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
import com.example.epic.util.getUserId
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject

@AndroidEntryPoint
class AddSellerFragment :
    BaseFragment<FragmentAddSellerBinding>(FragmentAddSellerBinding::inflate) {

    private val sellerViewModel: SellerViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()

    private val listIdCategory = ArrayList<Int>()
    private val listIdProduct = ArrayList<Int>()
    private val listNameCategory = ArrayList<String>()
    private val listNameProduct = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSpinner()
        setUpToolbar()

    }

    private fun setUpSpinner() {
        categoryViewModel.requestListCategory(getUserId()?.toInt() ?: savedUser!!.id_user)
        categoryViewModel.listCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val categories = response.data
                    if (categories.isEmpty()) {
                        disableSpinnerCategory()
                    } else {
                        val spinnerCategory = binding.spPickCategory
                        categories.forEach { category ->
                            listIdCategory.add(category.id_kategori)
                            listNameCategory.add(category.nama_kategori)
                        }

                        spinnerCategory.setAdapter(
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_dropdown_item_1line, listNameCategory
                            )
                        )
                        var lastSelectedPosition = -1
                        spinnerCategory.setOnItemClickListener { _, _, position, _ ->
                            if (position != lastSelectedPosition) {
                                val selectedCategory = categories[position]
                                lastSelectedPosition = position
                                showProduct(selectedCategory.id_kategori)
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

    private fun disableSpinnerCategory() {
        val spinnerCategory = binding.spPickCategory
        listIdCategory.clear()
        listNameCategory.clear()
        spinnerCategory.isEnabled = false
        spinnerCategory.text.clear()
        binding.tilCategory.hint = "data kosong"
        spinnerCategory.setAdapter(null)
    }

    private fun showProduct(categoryId: Int) {
        categoryViewModel.requestProductByCategory(categoryId)
        categoryViewModel.basedCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val listProduct = response.data
                    val spinnerProduct = binding.spProduct
                    if (listProduct.isEmpty()) {
                        binding.etUnitProduct.text.clear()
                        listNameProduct.clear()
                        listIdProduct.clear()
                        spinnerProduct.text.clear()
                        spinnerProduct.isEnabled = false
                        binding.tilProduct.hint = "data kosong"
                        spinnerProduct.setAdapter(null)
                    } else {
                        spinnerProduct.text.clear()
                        binding.tilProduct.hint = "Pilih Barang"
                        spinnerProduct.isEnabled = true
                        listProduct.forEach { product ->
                            listNameProduct.add(product.nama_barang)
                            listIdProduct.add(product.id_barang)
                        }

                        spinnerProduct.setAdapter(
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                listNameProduct
                            )
                        )

                        spinnerProduct.setOnItemClickListener { _, _, position, _ ->
                            val productPosition = listProduct[position]
                            val unitProduct = productPosition.satuan

                            binding.etUnitProduct.setText(unitProduct)

                            Toast.makeText(
                                requireContext(),
                                "${productPosition.nama_barang} dan ${productPosition.satuan}",
                                Toast.LENGTH_SHORT
                            ).show()
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

    private fun handleAddProduct() {
        val selectedCategoryText = binding.spPickCategory.text.toString()
        val selectedProductText = binding.spProduct.text.toString()

        val selectedCategoryPosition = listNameCategory.indexOf(selectedCategoryText)
        val selectedProductPosition = listNameProduct.indexOf(selectedProductText)
        val salesInput = binding.etSalesInput.text.toString()
        if (selectedCategoryPosition == -1) {
            showErrorMessage("harap pilih kategori dulu!!")
        } else if (selectedProductPosition == -1) {
            showErrorMessage("harap pilih barang dulu!!")
        } else if (salesInput.isEmpty()) {
            showErrorMessage("harap isi satuan barang dulu!!")
        } else {
            val selectedCategoryId = listIdCategory[selectedCategoryPosition]
            val selectedProductId = listIdProduct[selectedProductPosition]
            addDataSeller(selectedProductId, salesInput)
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
                    "Tambah Penjualan"
                )
            }
        }

        binding.toolbar.myToolbar.setTitleTextColor(Color.parseColor("#0660C7"))
    }

    private fun addDataSeller(selectedProductId: Int, salesInput: String) {
        val date = getCurrentDateTime()
        sellerViewModel.requestCreateSeller(
            RequestCreateSeller(
                selectedProductId.toString(),
                salesInput,
                date,
                getUserId()?.toInt() ?: savedUser!!.id_user
            )
        )
        sellerViewModel.createSellerResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    sellerViewModel.createSellerResponse.removeObservers(viewLifecycleOwner)
                    val response = it.data!!
                    showSuccessMessage(response.data.message)
                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    sellerViewModel.createSellerResponse.removeObservers(viewLifecycleOwner)
                    try {
                        val errorResponse = JSONObject(it.message ?: "")
                        val errorMessage = errorResponse.getJSONObject("data")
                            .getJSONObject("error")
                            .getString("message")

                        showErrorMessage(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        showErrorMessage(it.message!!)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sellerViewModel.createSellerResponse.removeObservers(viewLifecycleOwner)
    }

}