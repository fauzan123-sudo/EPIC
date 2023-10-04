package com.example.epic.ui.fragment.sales

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.R
import com.example.epic.data.adapter.SalesAdapter
import com.example.epic.databinding.FragmentListSalesBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.SalesViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListSalesFragment :
    BaseFragment<FragmentListSalesBinding>(FragmentListSalesBinding::inflate) {

    private val viewModel: SalesViewModel by viewModels()

    @Inject
    lateinit var salesAdapter: SalesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpData()

    }

    private fun setUpData() {
        viewModel.listSalesRequest()
        viewModel.listSalesResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    salesAdapter.differ.submitList(response.data)
                    with(binding.rvSales) {
                        layoutManager =
                            LinearLayoutManager(
                                requireContext()
                            )
                        adapter = salesAdapter
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
            setupMenu(R.menu.menu_action, { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_add -> {

                        Toast.makeText(requireContext(), "add has been clicked", Toast.LENGTH_SHORT)
                            .show()
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
                    "Sales"
                )
            }
        }
    }

}