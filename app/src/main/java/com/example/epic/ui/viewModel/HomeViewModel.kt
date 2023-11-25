package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.home.HomeResponse
import com.example.epic.data.model.home.RequestHome
import com.example.epic.data.repository.HomeRepository
import com.example.epic.util.NetworkResult
import com.example.epic.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _listCategoryResponse = SingleLiveEvent<NetworkResult<HomeResponse>>()
    val listCategoryResponse: LiveData<NetworkResult<HomeResponse>>
        get() = _listCategoryResponse


    fun requestHome(request: RequestHome) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _listCategoryResponse.postValue(NetworkResult.Loading())
                _listCategoryResponse.postValue(repository.homeApi(request))
            } else {
                _listCategoryResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

}