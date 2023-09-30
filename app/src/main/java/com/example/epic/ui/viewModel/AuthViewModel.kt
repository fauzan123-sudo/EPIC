package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.user.login.LoginResponse
import com.example.epic.data.model.user.login.RequestLogin
import com.example.epic.data.repository.AuthRepository
import com.example.epic.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _userResponse = MutableLiveData<NetworkResult<LoginResponse>>()
    val userResponse: LiveData<NetworkResult<LoginResponse>>
        get() = _userResponse

    fun requestUserLogin(request: RequestLogin) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _userResponse.postValue(NetworkResult.Loading())
                _userResponse.postValue(repository.userLogin(request))
            } else
                _userResponse.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }
}