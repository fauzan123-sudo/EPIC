package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.report.ReportProductResponse
import com.example.epic.data.repository.ReportRepository
import com.example.epic.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(val repository: ReportRepository) : ViewModel() {

    private val _readReport = MutableLiveData<NetworkResult<ReportProductResponse>>()
    val readReportResponse: LiveData<NetworkResult<ReportProductResponse>>
        get() = _readReport

    fun requestReport(userId: Int) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _readReport.postValue(NetworkResult.Loading())
                _readReport.postValue(repository.readReport(userId))
            } else {
                _readReport.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }
}