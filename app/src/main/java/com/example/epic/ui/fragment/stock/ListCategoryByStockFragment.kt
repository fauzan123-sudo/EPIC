package com.example.epic.ui.fragment.stock

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.R
import com.example.epic.data.adapter.StockProductAdapter
import com.example.epic.databinding.FragmentListCategoryByStockBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.ui.viewModel.NotificationViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getUserId
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListCategoryByStockFragment :
    BaseFragment<FragmentListCategoryByStockBinding>(FragmentListCategoryByStockBinding::inflate) {

    private val args: ListCategoryByStockFragmentArgs by navArgs()

    private val notificationViewModel: NotificationViewModel by viewModels()

    private val viewModel: NotificationViewModel by viewModels()

    private val categoryViewModel: CategoryViewModel by viewModels()

    @Inject
    lateinit var stockCategoryAdapter: StockProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpData()
        loadData()

    }

    private fun setUpData() {
        categoryViewModel.requestSearchByCategory(args.dataCategory.id_kategori)
        categoryViewModel.searchCategory.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    binding.apply {
                        if (data.isEmpty()) {
                            imgNoImage.visibility = View.VISIBLE
                            tvNoCategory.visibility = View.VISIBLE
                        } else {
                            imgNoImage.visibility = View.GONE
                            tvNoCategory.visibility = View.GONE
                            stockCategoryAdapter.differ.submitList(data)
                            with(binding.rvProduct) {
                                layoutManager =
                                    LinearLayoutManager(
                                        requireContext()
                                    )
                                adapter = stockCategoryAdapter
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
            toolbar.imageBadgeView.setOnClickListener {
                try {
                    findNavController().navigate(R.id.action_listCategoryByStockFragment_to_listWarningRefillFragment)
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "something error $e", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Persediaan Barang"
                )
            }
        }
    }

    private fun loadData() {
        viewModel.requestWarningRefill(getUserId()?.toInt() ?: savedUser!!.id_user)
        viewModel.warningRefillResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    val totalData = data.size
                    binding.toolbar.imageBadgeView.badgeValue = totalData
                    Log.d("TAG", "total notification: $totalData")
                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    Log.e("TAG", "loadData: ${it.message}")
                    showErrorMessage(it.message!!)
                }
            }
        }
    }

}