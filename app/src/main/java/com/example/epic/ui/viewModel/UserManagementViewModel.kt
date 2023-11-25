package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.user.management.read.UserListResponse
import com.example.epic.data.repository.UserManagementRepository
import com.example.epic.util.NetworkResult
import com.example.epic.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class UserManagementViewModel @Inject constructor(private val repository: UserManagementRepository) :
    ViewModel() {

    private val _userResponse = SingleLiveEvent<NetworkResult<UserListResponse>>()
    val listUserResponse: LiveData<NetworkResult<UserListResponse>>
        get() = _userResponse


    fun requestUserList() {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _userResponse.postValue(NetworkResult.Loading())
                _userResponse.postValue(repository.readUser())
            } else {
                _userResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

}