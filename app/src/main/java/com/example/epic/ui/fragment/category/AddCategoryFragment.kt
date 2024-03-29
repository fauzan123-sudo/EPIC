package com.example.epic.ui.fragment.category

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.epic.R
import com.example.epic.data.model.category.add.RequestAddCategory
import com.example.epic.databinding.FragmentAddCategoryBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getUserId
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCategoryFragment :
    BaseFragment<FragmentAddCategoryBinding>(FragmentAddCategoryBinding::inflate) {

    private val viewModel: CategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()

    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            setupMenu(R.menu.menu_action_text, { menuItem ->
                when (menuItem.itemId) {
                    R.id.add_text_action -> {
                        addData()
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
                    "Tambah Kategori"
                )
            }
        }
    }

    private fun addData() {
        binding.apply {

            val codeCategory = etCodeCategory.text.toString()
            val nameCategory = etNameCategory.text.toString()

            if (codeCategory.isEmpty()) {
                showErrorMessage("Harap isi kode kategori dulu!!")
            } else if (nameCategory.isEmpty()) {
                showErrorMessage("Harap isi nama kategori dulu!!")
            } else if (codeCategory.isEmpty() && nameCategory.isEmpty()) {
                showErrorMessage("Harap isi kode kategori dan nama kategori dulu!!")
            } else {
                viewModel.requestAddCategory(
                    RequestAddCategory(
                        codeCategory,
                        nameCategory,
                        getUserId()?.toInt() ?: savedUser!!.id_user
                    )
                )
                viewModel.addCategoryResponse.observe(viewLifecycleOwner) {
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

                        else -> {}
                    }
                }
            }
        }
    }

}