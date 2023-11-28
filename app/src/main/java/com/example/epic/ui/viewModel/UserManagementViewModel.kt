package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.user.management.create.CreateUserResponse
import com.example.epic.data.model.user.management.create.RequestCreateUser
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

    private val _createUserResponse = SingleLiveEvent<NetworkResult<CreateUserResponse>>()
    val createUserResponse: LiveData<NetworkResult<CreateUserResponse>>
        get() = _createUserResponse

    fun requestCreateUser(request:RequestCreateUser) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _createUserResponse.postValue(NetworkResult.Loading())
                _createUserResponse.postValue(repository.createUser(request))
            } else {
                _createUserResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

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