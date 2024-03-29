package com.example.epic.ui.fragment.category

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.epic.R
import com.example.epic.data.model.category.delete.RequestDeleteCategory
import com.example.epic.data.model.category.update.RequestEditCategory
import com.example.epic.databinding.FragmentUpdateCategoryBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getUserId
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateCategoryFragment :
    BaseFragment<FragmentUpdateCategoryBinding>(FragmentUpdateCategoryBinding::inflate) {

    private val args: UpdateCategoryFragmentArgs by navArgs()

    private val viewModel: CategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data = args.dataCategory
        val codeCategory = data.kode_kategori
        val nameCategory = data.nama_kategori
        setUpToolbar()
        setUpData(codeCategory, nameCategory)
        deleteCategory()

    }

    private fun setUpData(codeCategory: String, nameCategory: String) {
        binding.apply {
            etCodeCategory.setText(codeCategory)
            etNameCategory.setText(nameCategory)
        }
    }

    private fun deleteCategory() {
        binding.btnDeleteCategory.setOnClickListener {
            showWarningMessage("Kategori") {
                viewModel.deleteCategory(
                    RequestDeleteCategory(
                        args.dataCategory.id_kategori,
                        getUserId()?.toInt() ?: savedUser!!.id_user
                    )
                )
                viewModel.deleteCategoryResponse.observe(viewLifecycleOwner) {
                    when (it) {
                        is NetworkResult.Success -> {
                            hideLoading()
                            val response = it.data!!
                            if (response.status) {
                                showSuccessMessage(response.data.message)
                            } else {
                                showErrorMessage(response.data.message)
                            }
                        }

                        is NetworkResult.Loading -> {
                            showLoading()
                        }

                        is NetworkResult.Error -> {
                            hideLoading()
                            showErrorMessage(it.message!!)
//                            categoryAdapter.addItem(data)
                        }
                    }
                }
            }
        }
    }

    private fun currentUpdateData() {
        val data = args.dataCategory
        val idCategory = data.id_kategori
        val codeCategory = binding.etCodeCategory.text.toString()
        val nameCategory = binding.etNameCategory.text.toString()

        if (codeCategory.isEmpty()) {
            showErrorMessage("Harap isi kode kategori dulu!!")
        } else if (nameCategory.isEmpty()) {
            showErrorMessage("Harap isi nama kategori dulu!!")
        } else if (codeCategory.isEmpty() && nameCategory.isEmpty()) {
            showErrorMessage("Harap isi kode kategori dan nama kategori dulu!!")
        } else {
            viewModel.updateCategory(
                RequestEditCategory(
                    idCategory.toString(),
                    codeCategory,
                    nameCategory,
                    savedUser!!.id_user
                )
            )
            viewModel.updateCategoryResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Loading -> {
                        showLoading()

                    }

                    is NetworkResult.Success -> {
                        hideLoading()
                        val response = it.data!!
                        showSuccessMessage(response.message)
                    }

                    is NetworkResult.Error -> {
                        hideLoading()
                        showErrorMessage(it.message!!)
                    }
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
                        currentUpdateData()
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
                    "Edit Kategori"
                )
            }
        }
    }

}