package com.example.epic.ui.fragment.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.epic.R
import com.example.epic.data.adapter.MonthAdapter
import com.example.epic.databinding.FragmentHomeBinding
import com.example.epic.ui.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    @Inject
    lateinit var monthAdapter: MonthAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            hIncomingMenu.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_listSalesFragment)
            }


            tvLookAll.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_allMenuFragment)
            }

            imgLookAll.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_allMenuFragment)
            }

            val monthArray = resources.getStringArray(R.array.month).toList()
            monthAdapter.differ.submitList(monthArray)
            rvMonth.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = monthAdapter
            }
        }

    }

}