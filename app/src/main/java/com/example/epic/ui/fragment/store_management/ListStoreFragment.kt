package com.example.epic.ui.fragment.store_management

import android.os.Bundle
import android.view.View
import com.example.epic.databinding.FragmentListStoreBinding
import com.example.epic.ui.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListStoreFragment : BaseFragment<FragmentListStoreBinding>(FragmentListStoreBinding::inflate){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}