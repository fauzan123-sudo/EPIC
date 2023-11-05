package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.returnProduct.RequestAddReturn
import com.example.epic.data.model.returnProduct.create.CreateReturnProductResponse
import com.example.epic.data.model.returnProduct.read.ReturnProductResponse
import com.example.epic.data.repository.ProductReturnRepository
import com.example.epic.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class ProductReturnViewModel @Inject constructor(val repository: ProductReturnRepository) :
    ViewModel() {

    private val _createReturnResponse =
        MutableLiveData<NetworkResult<CreateReturnProductResponse>>()
    val createReturnResponse: LiveData<NetworkResult<CreateReturnProductResponse>>
        get() = _createReturnResponse

    private val _listReturnResponse =
        MutableLiveData<NetworkResult<ReturnProductResponse>>()
    val listReturnResponse: LiveData<NetworkResult<ReturnProductResponse>>
        get() = _listReturnResponse

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

    fun listProductReturnRequest() {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected){
                _listReturnResponse.postValue(NetworkResult.Loading())
                _listReturnResponse.postValue(repository.readReturnProduct())
            }else{
                _listReturnResponse.postValue(NetworkResult.Loading())
            }
        }
    }
}