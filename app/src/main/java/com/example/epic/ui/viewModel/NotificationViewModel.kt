package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.notification.check.CheckFcmResponse
import com.example.epic.data.model.notification.refill.WarningRefillResponse
import com.example.epic.data.model.notification.update.RequestUpdateToken
import com.example.epic.data.model.notification.update.UpdateFcmResponse
import com.example.epic.data.repository.NotificationsRepository
import com.example.epic.util.NetworkResult
import com.example.epic.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(private val repository: NotificationsRepository) :
    ViewModel() {

    private val _checkFcm = SingleLiveEvent<NetworkResult<CheckFcmResponse>>()
    val checkFcm: LiveData<NetworkResult<CheckFcmResponse>>
        get() = _checkFcm

    private val _updateFcmResponse = SingleLiveEvent<NetworkResult<UpdateFcmResponse>>()
    val updateFcmResponse: LiveData<NetworkResult<UpdateFcmResponse>>
        get() = _updateFcmResponse

    private val _warningRefillResponse = SingleLiveEvent<NetworkResult<WarningRefillResponse>>()
    val warningRefillResponse: LiveData<NetworkResult<WarningRefillResponse>>
        get() = _warningRefillResponse

    fun requestCheckFcm() {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected){
                _checkFcm.postValue(NetworkResult.Loading())
                _checkFcm.postValue(repository.checkNotification())
            }else{
                _checkFcm.postValue(NetworkResult.Error("No Internet Coonection"))
            }
        }
    }

    fun requestUpdateFcm(request: RequestUpdateToken) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected){
                _updateFcmResponse.postValue(NetworkResult.Loading())
                _updateFcmResponse.postValue(repository.updateFcmToken(request))
            }else{
                _updateFcmResponse.postValue(NetworkResult.Error("No Internet Coonection"))
            }
        }
    }

    fun requestWarningRefill(userId: Int) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected){
                _warningRefillResponse.postValue(NetworkResult.Loading())
                _warningRefillResponse.postValue(repository.warningRefill(userId))
            }else{
                _warningRefillResponse.postValue(NetworkResult.Error("No Internet Coonection"))
            }
        }
    }
}

//    private val _notifications: MutableStateFlow<Resource<List<Notification>>> =
//        MutableStateFlow(Resource.Loading())
//    val notifications: StateFlow<Resource<List<Notification>>> = _notifications
//
//    fun getNotificationsByNip(nip: String) {
//        viewModelScope.launch {
//            repository.getNotificationsByNip(nip).collect { result ->
//                _notifications.value = result
//            }
//        }
//    }
//
//}