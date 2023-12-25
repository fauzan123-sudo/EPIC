package com.example.epic.data.repository

import com.example.epic.data.model.notification.update.RequestUpdateToken
import com.example.epic.data.network.NotificationApi
import javax.inject.Inject

class NotificationsRepository @Inject constructor(private val api: NotificationApi) :
    BaseRepository() {

    suspend fun checkNotification(request: String) = safeApiCall {
        api.checkTokenFcm(request)
    }

    suspend fun updateFcmToken(request: RequestUpdateToken) = safeApiCall {
        api.updateFcmToken(request)
    }

    suspend fun warningRefill (userId: Int) = safeApiCall {
        api.listRefill(userId)
    }
}