package com.example.epic.ui.fragment.sales

import android.os.Bundle
import android.view.View
import com.example.epic.databinding.FragmentAddSalesBinding
import com.example.epic.ui.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSalesFragment : BaseFragment<FragmentAddSalesBinding>(FragmentAddSalesBinding::inflate){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}