package com.example.epic.ui.fragment.stock

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.data.adapter.WarningRefillAdapter
import com.example.epic.databinding.FragmentListWarningRefillBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.NotificationViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getUserId
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListWarningRefillFragment :
    BaseFragment<FragmentListWarningRefillBinding>(FragmentListWarningRefillBinding::inflate) {

    @Inject
    lateinit var refillAdapter: WarningRefillAdapter

    private val viewModel: NotificationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
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
                    "Pemesanan Barang Kembali"
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
                    binding.apply {
                        if (data.isEmpty()){
                            rvWarningRefill.visibility = View.GONE
                            imgNoImage.visibility = View.VISIBLE
                            tvNoData.visibility = View.VISIBLE
                        }else{
                            rvWarningRefill.visibility = View.VISIBLE
                            imgNoImage.visibility = View.GONE
                            tvNoData.visibility = View.GONE
                            refillAdapter.differ.submitList(data)
                            with(rvWarningRefill) {
                                layoutManager =
                                    LinearLayoutManager(
                                        requireContext()
                                    )
                                adapter = refillAdapter
                            }
                        }
                    }
                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                    Log.e("TAG", "loadData: ${it.message}")
                    showErrorMessage(it.message!!)
                }

                else -> {}
            }
        }
    }

}