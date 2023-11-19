package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.seller.ListSellerResponse
import com.example.epic.data.model.seller.create.CreateSellerResponse
import com.example.epic.data.model.seller.create.RequestCreateSeller
import com.example.epic.data.model.seller.delete.DeleteSellerResponse
import com.example.epic.data.repository.SellerRepository
import com.example.epic.util.NetworkResult
import com.example.epic.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class SellerViewModel @Inject constructor(private val repository: SellerRepository) : ViewModel() {

    private val _listSellerResponse = SingleLiveEvent<NetworkResult<ListSellerResponse>>()
    val listSellerResponse: LiveData<NetworkResult<ListSellerResponse>>
        get() = _listSellerResponse

    private val _createSellerResponse = SingleLiveEvent<NetworkResult<CreateSellerResponse>>()
    val createSellerResponse: LiveData<NetworkResult<CreateSellerResponse>>
        get() = _createSellerResponse

    private val _deleteSellerResponse = SingleLiveEvent<NetworkResult<DeleteSellerResponse>>()
    val deleteSellerResponse: LiveData<NetworkResult<DeleteSellerResponse>>
        get() = _deleteSellerResponse

    fun requestListSeller() {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _listSellerResponse.postValue(NetworkResult.Loading())
                _listSellerResponse.postValue(repository.listSeller())
            }else{
                _listSellerResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun requestCreateSeller(request:RequestCreateSeller) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _createSellerResponse.postValue(NetworkResult.Loading())
                _createSellerResponse.postValue(repository.createSeller(request))
            }else{
                _createSellerResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }

        }
    }

    fun requestDeleteSeller(request:Int) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _deleteSellerResponse.postValue(NetworkResult.Loading())
                _deleteSellerResponse.postValue(repository.deleteSeller(request))
            }else{
                _deleteSellerResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }

        }
    }
}