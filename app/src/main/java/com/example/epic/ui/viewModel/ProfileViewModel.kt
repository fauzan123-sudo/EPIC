package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.user.profile.UserProfileResponse
import com.example.epic.data.model.user.profile.image.ImageChangeResponse
import com.example.epic.data.repository.UserProfileRepository
import com.example.epic.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: UserProfileRepository) :
    ViewModel() {

    private val _profileResponse = MutableLiveData<NetworkResult<UserProfileResponse>>()
    val profileResponse: LiveData<NetworkResult<UserProfileResponse>>
        get() = _profileResponse

    private val _updateProfilePhoto = MutableLiveData<NetworkResult<ImageChangeResponse>>()
    val updateProfilePhoto : LiveData<NetworkResult<ImageChangeResponse>>
        get() = _updateProfilePhoto

    fun requestProfile(userId: Int) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _profileResponse.postValue(NetworkResult.Loading())
                _profileResponse.postValue(repository.readProfile(userId))
            } else {
                _profileResponse.postValue(NetworkResult.Loading())
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
                _updateProfilePhoto.postValue(NetworkResult.Loading())
            }
        }
    }
}