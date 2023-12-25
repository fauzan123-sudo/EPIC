package com.example.epic.data.network

import com.example.epic.data.model.notification.check.CheckFcmResponse
import com.example.epic.data.model.notification.refill.WarningRefillResponse
import com.example.epic.data.model.notification.update.RequestUpdateToken
import com.example.epic.data.model.notification.update.UpdateFcmResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NotificationApi {

    @GET("check-token-firebase")
    suspend fun checkTokenFcm(
        @Query("token_fcm") token_fcm:String
    ): Response<CheckFcmResponse>

    @POST("update-token-firebase")
    suspend fun updateFcmToken(
        @Body request: RequestUpdateToken
    ): Response<UpdateFcmResponse>

    @GET("barang/warning-refill")
    suspend fun listRefill(
        @Query("id_user") userId: Int
    ): Response<WarningRefillResponse>
}