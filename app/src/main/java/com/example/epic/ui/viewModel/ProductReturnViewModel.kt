package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.returnProduct.RequestAddReturn
import com.example.epic.data.model.returnProduct.RequestDeleteReturn
import com.example.epic.data.model.returnProduct.create.CreateReturnProductResponse
import com.example.epic.data.model.returnProduct.delete.DeleteReturnProductResponse
import com.example.epic.data.model.returnProduct.read.ReturnProductResponse
import com.example.epic.data.repository.ProductReturnRepository
import com.example.epic.util.NetworkResult
import com.example.epic.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class ProductReturnViewModel @Inject constructor(val repository: ProductReturnRepository) :
    ViewModel() {

    private val _createReturnResponse =
        SingleLiveEvent<NetworkResult<CreateReturnProductResponse>>()
    val createReturnResponse: LiveData<NetworkResult<CreateReturnProductResponse>>
        get() = _createReturnResponse

    private val _listReturnResponse =
        SingleLiveEvent<NetworkResult<ReturnProductResponse>>()
    val listReturnResponse: LiveData<NetworkResult<ReturnProductResponse>>
        get() = _listReturnResponse

    private val _deleteReturnResponse =
        SingleLiveEvent<NetworkResult<DeleteReturnProductResponse>>()
    val deleteReturnResponse: LiveData<NetworkResult<DeleteReturnProductResponse>>
        get() = _deleteReturnResponse

    fun createProductReturnRequest(request:RequestAddReturn) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected){
                _createReturnResponse.postValue(NetworkResult.Loading())
                _createReturnResponse.postValue(repository.createReturnProduct(request))
            }else{
                _createReturnResponse.postValue(NetworkResult.Loading())
            }
        }
    }

    fun requestdeleteReturnProduct(request:RequestDeleteReturn) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected){
                _deleteReturnResponse.postValue(NetworkResult.Loading())
                _deleteReturnResponse.postValue(repository.deleteReturnProduct(request))
            }else{
                _deleteReturnResponse.postValue(NetworkResult.Loading())
            }
        }
    }

    fun listProductReturnRequest(userId: Int) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected){
                _listReturnResponse.postValue(NetworkResult.Loading())
                _listReturnResponse.postValue(repository.readReturnProduct(userId))
            }else{
                _listReturnResponse.postValue(NetworkResult.Loading())
            }
        }
    }
}