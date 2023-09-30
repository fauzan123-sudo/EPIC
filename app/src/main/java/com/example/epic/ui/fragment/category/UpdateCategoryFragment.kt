package com.example.epic.ui.fragment.category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.epic.databinding.FragmentUpdateCategoryBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateCategoryFragment :
    BaseFragment<FragmentUpdateCategoryBinding>(FragmentUpdateCategoryBinding::inflate) {

    private val viewModel:CategoryViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateData()

    }

    private fun updateData() {

    }

}