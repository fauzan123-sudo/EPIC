package com.example.epic.data.network

import com.example.epic.data.model.resetLogin.RequestResetLogin
import com.example.epic.data.model.resetLogin.ResetLoginResponse
import com.example.epic.data.model.user.login.LoginResponse
import com.example.epic.data.model.user.login.RequestLogin
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth")
    suspend fun loginUser(
        @Body request: RequestLogin
    ): Response<LoginResponse>

    @POST("reset-is-login")
    suspend fun resetLogin(
        @Body request : RequestResetLogin
    ) : Response<ResetLoginResponse>

}