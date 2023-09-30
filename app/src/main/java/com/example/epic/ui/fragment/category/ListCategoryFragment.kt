package com.example.epic.ui.fragment.category

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.R
import com.example.epic.data.adapter.CategoryAdapter
import com.example.epic.databinding.FragmentListCategoryBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListCategoryFragment :
    BaseFragment<FragmentListCategoryBinding>(FragmentListCategoryBinding::inflate) {

    private val viewModel: CategoryViewModel by viewModels()
    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLoading()
        loadDataCategory()
        movePage()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Kategori"
                )
            }
        }
    }

    private fun movePage() {
        binding.apply {
            mcAddCategory.setOnClickListener {
                findNavController().navigate(R.id.action_listCategoryFragment_to_addCategoryFragment)
            }
        }
    }

    private fun loadDataCategory() {
        binding.apply {
            viewModel.requestListCategory()
            viewModel.listCategoryResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Loading -> {
                        sweetAlertDialog.show()
                    }

                    is NetworkResult.Success -> {
                        sweetAlertDialog.dismiss()
                        val response = it.data!!
                        if (response.data.isNotEmpty()) {
                            Log.d("loadDataCategory", "$response")
                            categoryAdapter.differ.submitList(response.data)
                            with(rvCategory) {
                                layoutManager =
                                    LinearLayoutManager(
                                        requireContext()
                                    )
                                adapter = categoryAdapter
                            }
                            tvNoCategory.visibility = View.GONE

                        } else {
                            tvNoCategory.visibility = View.VISIBLE
                            Log.d("data is null", "loadDataCategory: ")
                        }
                    }

                    is NetworkResult.Error -> {
                        sweetAlertDialog.dismiss()
                        showErrorMessage(it.message!!)
                        Log.e("category error", "${it.message}")
                    }

                    else -> {

                    }
                }
            }
        }
    }

}