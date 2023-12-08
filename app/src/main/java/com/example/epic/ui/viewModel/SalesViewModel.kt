package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.sales.create.CreateSalesResponse
import com.example.epic.data.model.sales.create.RequestCreateSales
import com.example.epic.data.model.sales.delete.DeleteSalesResponse
import com.example.epic.data.model.sales.read.ListSalesResponse
import com.example.epic.data.repository.SalesRepository
import com.example.epic.util.NetworkResult
import com.example.epic.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(private val repository: SalesRepository) : ViewModel() {

    private val _listSalesResponse = SingleLiveEvent<NetworkResult<ListSalesResponse>>()
    val listSalesResponse: LiveData<NetworkResult<ListSalesResponse>>
        get() = _listSalesResponse

    private val _createSalesResponse = SingleLiveEvent<NetworkResult<CreateSalesResponse>>()
    val createSalesResponse: LiveData<NetworkResult<CreateSalesResponse>>
        get() = _createSalesResponse

    private val _deleteSalesResponse = SingleLiveEvent<NetworkResult<DeleteSalesResponse>>()
    val deleteSalesResponse: LiveData<NetworkResult<DeleteSalesResponse>>
        get() = _deleteSalesResponse

    fun createSalesRequest(request: RequestCreateSales) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _createSalesResponse.postValue(NetworkResult.Loading())
                _createSalesResponse.postValue(repository.createSales(request))
            } else {
                _createSalesResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun listSalesRequest(userId:Int) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _listSalesResponse.postValue(NetworkResult.Loading())
                _listSalesResponse.postValue(repository.readSales(userId))
            }else{
                _listSalesResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun deleteSalesRequest(idSales:String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _deleteSalesResponse.postValue(NetworkResult.Loading())
                _deleteSalesResponse.postValue(repository.deleteSales(idSales))
            }else{
                _deleteSalesResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

}