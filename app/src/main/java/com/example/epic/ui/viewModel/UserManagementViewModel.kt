package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.user.management.create.CreateUserResponse
import com.example.epic.data.model.user.management.create.RequestCreateUser
import com.example.epic.data.model.user.management.delete.DeleteUserResponse
import com.example.epic.data.model.user.management.delete.RequestDeleteUser
import com.example.epic.data.model.user.management.read.UserListResponse
import com.example.epic.data.model.user.management.update.RequestUpdateUser
import com.example.epic.data.model.user.management.update.UpdateUserResponse
import com.example.epic.data.model.user.profile.image.ImageChangeResponse
import com.example.epic.data.repository.UserManagementRepository
import com.example.epic.util.NetworkResult
import com.example.epic.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import java.io.File
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

    private val _updateImageUserResponse = SingleLiveEvent<NetworkResult<ImageChangeResponse>>()
    val updateProfilePhoto: LiveData<NetworkResult<ImageChangeResponse>>
        get() = _updateImageUserResponse

    private val _deleteUserResponse = SingleLiveEvent<NetworkResult<DeleteUserResponse>>()
    val deleteUserResponse: LiveData<NetworkResult<DeleteUserResponse>>
        get() = _deleteUserResponse

    private val _updateUserResponse = SingleLiveEvent<NetworkResult<UpdateUserResponse>>()
    val updateUserResponse : LiveData<NetworkResult<UpdateUserResponse>>
        get() = _updateUserResponse

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

    fun requestUpdatePhoto(userId: String, photoFile: File) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _updateImageUserResponse.postValue(NetworkResult.Loading())
                _updateImageUserResponse.postValue(repository.updateProfilePhoto(userId, photoFile))
            } else {
                _updateImageUserResponse.postValue(NetworkResult.Loading())
            }
        }
    }



    fun requestDeleteUser(request:RequestDeleteUser) =
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _deleteUserResponse.postValue(NetworkResult.Loading())
                _deleteUserResponse.postValue(repository.deleteUser(request))
            } else {
                _deleteUserResponse.postValue(NetworkResult.Loading())
            }
        }

    fun requestUpdateUser(request: RequestUpdateUser) =
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _updateUserResponse.postValue(NetworkResult.Loading())
                _updateUserResponse.postValue(repository.updateUser(request))
            } else {
                _updateUserResponse.postValue(NetworkResult.Loading())
            }
        }

}