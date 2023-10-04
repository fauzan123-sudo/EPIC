package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.stock.StockProductResponse
import com.example.epic.data.repository.StockRepository
import com.example.epic.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(private val repository: StockRepository) : ViewModel() {

    private val _listStockResponse = MutableLiveData<NetworkResult<StockProductResponse>>()
    val listStockResponse: LiveData<NetworkResult<StockProductResponse>>
        get() = _listStockResponse

    fun requestListStock() {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _listStockResponse.postValue(NetworkResult.Loading())
                _listStockResponse.postValue(repository.getListStock())
            } else {
                _listStockResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }

        }
    }
}