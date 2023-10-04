package com.example.epic.ui.fragment.sales

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.epic.databinding.FragmentAddSalesBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import com.example.epic.ui.viewModel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSalesFragment : BaseFragment<FragmentAddSalesBinding>(FragmentAddSalesBinding::inflate) {

    val categoryViewModel: CategoryViewModel by viewModels()
    val productViewModel: ProductViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}