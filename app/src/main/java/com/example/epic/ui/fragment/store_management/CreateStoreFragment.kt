package com.example.epic.ui.fragment.store_management

import android.os.Bundle
import android.view.View
import com.example.epic.databinding.FragmentCreateStoreBinding
import com.example.epic.ui.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateStoreFragment : BaseFragment<FragmentCreateStoreBinding>(FragmentCreateStoreBinding::inflate){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}