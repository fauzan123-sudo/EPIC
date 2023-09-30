package com.example.epic.ui.fragment.product

import android.os.Bundle
import android.view.View
import com.example.epic.databinding.FragmentListProductBinding
import com.example.epic.ui.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListProductFragment : BaseFragment<FragmentListProductBinding>(FragmentListProductBinding::inflate){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}