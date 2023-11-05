package com.example.epic.ui.fragment.product_return

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.R
import com.example.epic.data.adapter.ProductReturnAdapter
import com.example.epic.databinding.FragmentProductReturnBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ProductReturnViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductReturnFragment : BaseFragment<FragmentProductReturnBinding>(FragmentProductReturnBinding::inflate){

    private val viewModel:ProductReturnViewModel by viewModels()
    @Inject
    lateinit var productReturnAdapter: ProductReturnAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        setUpToolbar()

    }

    private fun setUpToolbar() {
        binding.apply {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar.myToolbar)
            setupMenu(R.menu.menu_action, { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_add -> {
                        findNavController().navigate(R.id.action_productReturnFragment_to_addProductReturnFragment)
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
                    "Penjualan"
                )
            }
        }
    }

    private fun loadData() {
        viewModel.listProductReturnRequest()
        viewModel.listReturnResponse.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Success ->{
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    productReturnAdapter.differ.submitList(data)
                    binding.rvProductReturn.apply {
                        layoutManager =
                            LinearLayoutManager(
                                requireContext()
                            )
                        adapter = productReturnAdapter
                    }
                }

                is NetworkResult.Loading ->{
                    showLoading()
                }

                is NetworkResult.Error ->{
                    hideLoading()
                    showErrorMessage(it.message ?: "Error occur")
                }
            }
        }
    }

}