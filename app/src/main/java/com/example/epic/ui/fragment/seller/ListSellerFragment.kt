package com.example.epic.ui.fragment.seller

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.R
import com.example.epic.data.adapter.SellerAdapter
import com.example.epic.data.model.seller.Data
import com.example.epic.databinding.FragmentListSellerBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.SellerViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.getUserId
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListSellerFragment :
    BaseFragment<FragmentListSellerBinding>(FragmentListSellerBinding::inflate),
    SellerAdapter.ItemListener {

    private val viewModel: SellerViewModel by viewModels()

    @Inject
    lateinit var sellerAdapter: SellerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSellerData()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            if (savedUser?.role == "2") {
                setupMenu(R.menu.menu_action, { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_add -> {
                            findNavController().navigate(R.id.action_listSellerFragment_to_addSellerFragment)
                            true
                        }

                        else -> false
                    }
                }, MenuItem.SHOW_AS_ACTION_ALWAYS)
            }


            view?.let {
                configureToolbarBackPress(
                    toolbar.myToolbar,
                    it,
                    requireActivity(),
                    "Penjualan"
                )
            }
        }
    }

    private fun loadSellerData() {
        viewModel.requestListSeller(getUserId()?.toInt() ?: savedUser!!.id_user)
        viewModel.listSellerResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    binding.apply {
                        if (data.isEmpty()) {
                            imgNoImage.visibility = View.VISIBLE
                            tvNoData.visibility = View.VISIBLE
                        } else {
                            imgNoImage.visibility = View.GONE
                            tvNoData.visibility = View.GONE
                            sellerAdapter.listener = this@ListSellerFragment
                            sellerAdapter.differ.submitList(data)
                            with(binding.rvSeller) {
                                layoutManager =
                                    LinearLayoutManager(
                                        requireContext()
                                    )
                                adapter = sellerAdapter
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

    override fun deleteSeller(seller: Data) {
        showWarningMessage("Penjualan") {
            viewModel.requestDeleteSeller(seller.id_penjualan)
            viewModel.deleteSellerResponse.observe(viewLifecycleOwner) {
                when (it) {
                    is NetworkResult.Success -> {
                        hideLoading()
                        viewModel.deleteSellerResponse.removeObservers(viewLifecycleOwner)
                        val response = it.data!!
                        if (response.status) {
                            showSuccessDelete(response.data.message)
                            loadSellerData()
                        } else {
                            showErrorMessage(response.data.message)
                        }
                    }

                    is NetworkResult.Loading -> {
                        showLoading()
                    }

                    is NetworkResult.Error -> {
                        hideLoading()
                        viewModel.deleteSellerResponse.removeObservers(viewLifecycleOwner)
                        showErrorMessage(it.message!!)
                    }
                }
//                viewModel.deleteSellerResponse.removeObservers(viewLifecycleOwner)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.deleteSellerResponse.removeObservers(viewLifecycleOwner)
        viewModel.listSellerResponse.removeObservers(viewLifecycleOwner)
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "alert is appear: On Resume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "alert is appear: On Pause")
    }

}