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
import com.example.epic.data.model.category.read.Data
import com.example.epic.databinding.FragmentListCategoryBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getUserId
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListCategoryFragment :
    BaseFragment<FragmentListCategoryBinding>(FragmentListCategoryBinding::inflate),
    CategoryAdapter.ItemListener {

    private val viewModel: CategoryViewModel by viewModels()

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLoading()
        setUpData()
        movePage()
        setUpToolbar()
//        deleteCategory()
    }

//    private fun deleteCategory() {
//        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ) = false
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//
//                val data = categoryAdapter.differ.currentList[position]
//                val deletedCategory =
//                    categoryAdapter.differ.currentList[position].id_kategori
//
//                viewModel.deleteCategory(
//                    RequestDeleteCategory(
//                        deletedCategory,
//                        getUserId()?.toInt() ?: savedUser!!.id_user
//                    )
//                )
//                viewModel.deleteCategoryResponse.observe(viewLifecycleOwner) {
//                    when (it) {
//                        is NetworkResult.Success -> {
//                            hideLoading()
//                            setUpData()
//                        }
//
//                        is NetworkResult.Loading -> {
//                            showLoading()
//                        }
//
//                        is NetworkResult.Error -> {
//                            hideLoading()
//                            showErrorMessage(it.message!!)
////                            categoryAdapter.addItem(data)
//                        }
//                    }
//                }
//
//            }
//        }).attachToRecyclerView(binding.rvCategory)
//    }

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
        if (savedUser?.role == "2") {
            binding.apply {
                mcAddCategory.setOnClickListener {
                    findNavController().navigate(R.id.action_listCategoryFragment_to_addCategoryFragment)
                }
            }
        } else {
            binding.mcAddCategory.visibility = View.GONE
        }
    }

    private fun setUpData() {
        binding.apply {
            viewModel.requestListCategory(getUserId()?.toInt() ?: savedUser!!.id_user)
            viewModel.listCategoryResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Loading -> {
                        sweetAlertDialog.show()
                    }

                    is NetworkResult.Success -> {
                        sweetAlertDialog.dismiss()

                        imgNoImage.visibility = View.GONE
                        tvNoCategory.visibility = View.GONE

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
                                categoryAdapter.listener = this@ListCategoryFragment


                            }
                            tvNoCategory.visibility = View.GONE

                        } else {
                            imgNoImage.visibility = View.VISIBLE
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

    override fun updateCategory(data: Data) {
        Log.d("clicked", "bind: on fire too $data")
        try {
            val action =
                ListCategoryFragmentDirections.actionListCategoryFragmentToUpdateCategoryFragment(
                    data
                )
            findNavController().navigate(action)

        } catch (e: Exception) {
            Log.e("error", "updateCategory: ", e)
            showErrorMessage(e.toString())
        }

    }

}