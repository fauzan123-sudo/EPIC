package com.example.epic.ui.fragment.profile

import android.os.Bundle
import android.view.View
import com.example.epic.databinding.FragmentProfileBinding
import com.example.epic.ui.fragment.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        }


}