package com.example.epic.ui.fragment.product


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.epic.R
import com.example.epic.data.model.category.spinner.Data
import com.example.epic.data.model.product.update.RequestEditProduct
import com.example.epic.databinding.FragmentUpdateProductBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.ui.viewModel.ProductViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getUserId
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateProductFragment :
    BaseFragment<FragmentUpdateProductBinding>(FragmentUpdateProductBinding::inflate) {

    private val args: UpdateProductFragmentArgs by navArgs()

    private val viewModel: ProductViewModel by viewModels()

    private val categoryViewModel: CategoryViewModel by viewModels()
    private var selectedCategoryId = ""
    private lateinit var defaultData: Data

    private var categories: List<Data>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpSpinner()
        setUpData()
//        loadDefaultSpinner()

    }

//    private fun loadDefaultSpinner() {
//        categoryViewModel.requestDropDownCategory(args.dataProduct?.id_kategori.toString())
//        categoryViewModel.dropDownCategory.observe(viewLifecycleOwner) {
//            when (it) {
//                is NetworkResult.Success -> {
//                    hideLoading()
//                    val response = it.data!!
//                    val firstData = response.data.first()
//                    val defaultSpinner =
//                        Data(firstData.id_kategori, firstData.nama_kategori)
//
////                    setUpSpinner(defaultSpinner)
//                }
//
//                is NetworkResult.Loading -> {
//                    showLoading()
//                }
//
//                is NetworkResult.Error -> {
//                    hideLoading()
//                    showErrorMessage(it.message!!)
//                }
//            }
//        }
//    }

    private fun setUpData() {
        val data = args.dataProduct
        with(binding) {
            etCodeProduct.setText(data?.kode_barang.toString())
            etMinimumProduct.setText(data?.minimal_persediaan.toString())
            etNameProduct.setText(data?.nama_barang.toString())
            etUnitProduct.setText(data?.satuan.toString())
        }


    }

    private fun setUpSpinner() {
        categoryViewModel.requestShowCategory()
        categoryViewModel.showCategoryResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    categories = response.data

                    val categoryNames = categories!!.map { category ->
                        category.nama_kategori
                    }.toTypedArray()

                    if (response.status) {
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            categoryNames
                        )

                        defaultData =
                            Data(
                                args.dataProduct?.id_kategori ?: 0,
                                args.dataProduct?.nama_kategori ?: "-"
                            )

                        val defaultIndex = categories!!.indexOf(defaultData)

                        if (defaultIndex != -1) {
                            binding.spPickCategory.setText(defaultData.nama_kategori, false)
                        }

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spPickCategory.setAdapter(adapter)
                        binding.spPickCategory.setOnItemClickListener { _, _, position, _ ->
                            val selectedCategoryName: String =
                                adapter.getItem(position) ?: return@setOnItemClickListener

                            val selectedData =
                                categories!!.find { category ->
                                    category.nama_kategori == selectedCategoryName
                                }
                            if (selectedData != null) {
                                selectedCategoryId = selectedData.id_kategori.toString()
                                val categoryId = selectedData.id_kategori
                                Log.d("TAG", "pick spinner: $selectedData")
                            } else {
                                val categoryDefault = defaultData.id_kategori
                                Log.d("TAG", "pick spinner: $categoryDefault")
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
            setupMenu(R.menu.menu_action_text, { menuItem ->
                when (menuItem.itemId) {
                    R.id.add_text_action -> {
                        if (savedUser?.role == "1") {
                            val userId = getUserId()?.toInt()!!
                            handleUpdateProduct(userId)
                        } else {
                            handleUpdateProduct(savedUser!!.id_user)
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
                    "Edit Data Barang"
                )
            }
        }
    }

    private fun actionAdd() {
        val selectedText = binding.spPickCategory.text.toString()

        if (selectedText.isNotEmpty()) {
            val selectedData: Data? = categories?.find { it.nama_kategori == selectedText }
            val selectedId = selectedData?.id_kategori ?: -1
//            Toast.makeText(requireContext(), "Selected ID: $selectedId", Toast.LENGTH_SHORT)
//                .show()
        } else {
//            Toast.makeText(requireContext(), selectedCategoryId, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleUpdateProduct(userId: Int) {
        val nameProduct = binding.etNameProduct.text.toString()
        val productCode = binding.etCodeProduct.text.toString()
        val unitProduct = binding.etUnitProduct.text.toString()
        val minimumProduct = binding.etMinimumProduct.text.toString()
        val selectedText = binding.spPickCategory.text.toString()
        val selectedData: Data? = categories?.find { it.nama_kategori == selectedText }
        val selectedId = selectedData?.id_kategori ?: -1
        if (productCode == "") {
            showErrorMessage("harap pilih barang dulu!!")
        } else if (nameProduct.isEmpty()) {
            showErrorMessage("harap isi nama barang dulu!!")
        } else if (minimumProduct.isEmpty()) {
            showErrorMessage("harap isi minimum barang dulu!!")
        } else if (unitProduct.isEmpty()) {
            showErrorMessage("harap isi satuan barang dulu!!")
        } else {
            addData(
                selectedId.toString(),
                productCode,
                nameProduct,
                unitProduct,
                minimumProduct,
                args.dataProduct?.id_barang.toString(),
                userId
            )
            Log.d(
                "TAG",
                "result data:  $selectedCategoryId, $productCode, $nameProduct, $unitProduct, $minimumProduct"
            )
        }
    }

    private fun addData(
        categoryId: String,
        productCode: String,
        nameProduct: String,
        unitProduct: String,
        minimumProduct: String,
        productId: String,
        userId: Int
    ) {
        viewModel.updateProduct(
            RequestEditProduct(
                productId,
                productCode,
                nameProduct,
                unitProduct,
                categoryId,
                minimumProduct,
                0,
                userId
            )
        )
        viewModel.updateProductResponse.observe(viewLifecycleOwner) {
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