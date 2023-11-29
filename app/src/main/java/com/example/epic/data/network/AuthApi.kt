package com.example.epic.data.network

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


}