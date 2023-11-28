package com.example.epic.ui.fragment.store_management

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.epic.R
import com.example.epic.data.adapter.UserAdapter
import com.example.epic.data.model.user.management.read.Data
import com.example.epic.databinding.FragmentListStoreBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.UserManagementViewModel
import com.example.epic.util.NetworkResult
import com.example.epic.util.configureToolbarBackPress
import com.example.epic.util.setupMenu
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListStoreFragment :
    BaseFragment<FragmentListStoreBinding>(FragmentListStoreBinding::inflate), UserAdapter.ItemListener {

    private val viewModel: UserManagementViewModel by viewModels()

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
                        findNavController().navigate(R.id.action_listStoreFragment_to_createStoreFragment)
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
                    "Pengguna"
                )
            }
        }
    }

    private fun loadData() {
        viewModel.requestUserList()
        viewModel.listUserResponse.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResult.Success -> {
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                    binding.rvUser.apply {
                        val userAdapter = UserAdapter()
                        layoutManager =
                            GridLayoutManager(requireContext(), 3)
                        adapter = userAdapter

                        userAdapter.differ.submitList(data)
                        userAdapter.listener = this@ListStoreFragment
                    }
                }

                is NetworkResult.Loading -> {
                    showLoading()
                }

                is NetworkResult.Error -> {
                    hideLoading()
                }
            }
        }
    }

    override fun updateUser(data: Data) {
        val action = ListStoreFragmentDirections.actionListStoreFragmentToUpdateStoreFragment(data)
        findNavController().navigate(action)
    }

}