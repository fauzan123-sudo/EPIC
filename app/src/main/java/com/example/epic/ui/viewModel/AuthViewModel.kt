package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.resetLogin.RequestResetLogin
import com.example.epic.data.model.resetLogin.ResetLoginResponse
import com.example.epic.data.model.user.login.LoginResponse
import com.example.epic.data.model.user.login.RequestLogin
import com.example.epic.data.repository.AuthRepository
import com.example.epic.util.NetworkResult
import com.example.epic.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _userResponse = SingleLiveEvent<NetworkResult<LoginResponse>>()
    val userResponse: LiveData<NetworkResult<LoginResponse>>
        get() = _userResponse

    private val _resetLoginResponse = SingleLiveEvent<NetworkResult<ResetLoginResponse>>()
    val resetLoginResponse: LiveData<NetworkResult<ResetLoginResponse>>
        get() = _resetLoginResponse

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

    fun requestResetLogin(request: RequestResetLogin) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _resetLoginResponse.postValue(NetworkResult.Loading())
                _resetLoginResponse.postValue(repository.restLogin(request))
            } else
                _resetLoginResponse.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }


}