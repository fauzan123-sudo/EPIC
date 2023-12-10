package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.user.logOut.LogOutResponse
import com.example.epic.data.model.user.profile.UserProfileResponse
import com.example.epic.data.model.user.profile.image.ImageChangeResponse
import com.example.epic.data.model.user.profile.update.RequestUpdateProfile
import com.example.epic.data.model.user.profile.update.UpdateProfileResponse
import com.example.epic.data.repository.UserProfileRepository
import com.example.epic.util.NetworkResult
import com.example.epic.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: UserProfileRepository) :
    ViewModel() {

    private val _profileResponse = SingleLiveEvent<NetworkResult<UserProfileResponse>>()
    val profileResponse: LiveData<NetworkResult<UserProfileResponse>>
        get() = _profileResponse

    private val _updateProfilePhoto = SingleLiveEvent<NetworkResult<ImageChangeResponse>>()
    val updateProfilePhoto: LiveData<NetworkResult<ImageChangeResponse>>
        get() = _updateProfilePhoto

    private val _logOutResponse = SingleLiveEvent<NetworkResult<LogOutResponse>>()
    val logOutResponse: LiveData<NetworkResult<LogOutResponse>>
        get() = _logOutResponse

    private val _updateProfileResponse = SingleLiveEvent<NetworkResult<UpdateProfileResponse>>()
    val updateProfileResponse: LiveData<NetworkResult<UpdateProfileResponse>>
        get() = _updateProfileResponse

    fun requestProfile(userId: Int) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _profileResponse.postValue(NetworkResult.Loading())
                _profileResponse.postValue(repository.readProfile(userId))
            } else {
                _profileResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun requestUpdatePhoto(userId: String, photoFile: File) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _updateProfilePhoto.postValue(NetworkResult.Loading())
                _updateProfilePhoto.postValue(repository.updateProfilePhoto(userId, photoFile))
            } else {
                _updateProfilePhoto.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun logOut() {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _logOutResponse.postValue(NetworkResult.Loading())
                _logOutResponse.postValue(repository.logOut())
            } else {
                _logOutResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun updateProfileRequest(userId: String, request: RequestUpdateProfile) = viewModelScope.launch {
        val connected = CheckInternet().check()
        if (connected) {
            _updateProfileResponse.postValue(NetworkResult.Loading())
            _updateProfileResponse.postValue(repository.updateProfile(userId, request))
        } else {
            _updateProfileResponse.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }
}