package com.example.epic.ui.fragment.report

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.epic.data.adapter.ReportAdapter
import com.example.epic.databinding.FragmentReportBinding
import com.example.epic.ui.fragment.BaseFragment
import com.example.epic.ui.viewModel.ReportViewModel
import com.example.epic.util.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding>(FragmentReportBinding::inflate) {

    private val viewModel: ReportViewModel by viewModels()
    @Inject
    lateinit var adapter:ReportAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpData()

    }

    private fun setUpData() {
        viewModel.requestReport()
        viewModel.readReportResponse.observe(viewLifecycleOwner){
            when(it){
                is NetworkResult.Success ->{
                    hideLoading()
                    val response = it.data!!
                    val data = response.data
                }

                is NetworkResult.Loading ->{
                    showLoading()
                }

                is NetworkResult.Error ->{
                    hideLoading()
                    showErrorMessage(it.message!!)
                }
            }
        }
    }

}