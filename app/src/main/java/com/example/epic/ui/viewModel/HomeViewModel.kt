package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.home.HomeResponse
import com.example.epic.data.model.home.RequestHome
import com.example.epic.data.model.user.management.read.Data
import com.example.epic.data.repository.HomeRepository
import com.example.epic.util.DataStatus
import com.example.epic.util.NetworkResult
import com.example.epic.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _historyTransaction = SingleLiveEvent<NetworkResult<HomeResponse>>()
    val historyTransaction: LiveData<NetworkResult<HomeResponse>>
        get() = _historyTransaction

    private val _getAllStore = MutableLiveData<DataStatus<Data>>()
    val getAllStore: LiveData<DataStatus<Data>>
        get() = _getAllStore


    fun requestHome(request: RequestHome) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _historyTransaction.postValue(NetworkResult.Loading())
                _historyTransaction.postValue(repository.homeApi(request))
            } else {
                _historyTransaction.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun getAllStore() = viewModelScope.launch {
        _getAllStore.postValue(DataStatus.loading())
        repository.getSingleStore()
            .observeForever { data ->
                _getAllStore.postValue(DataStatus.success(data, data == null))
            }
    }

    fun saveStoreName(data:Data) = viewModelScope.launch {
        repository.saveStore(data)
    }

    fun deleteStoreName(data:Data) = viewModelScope.launch {
        repository.deleteStore(data)
    }

}