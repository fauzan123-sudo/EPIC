package com.example.epic.data.network

import com.example.epic.data.model.user.management.read.UserListResponse
import retrofit2.Response
import retrofit2.http.POST

interface UserManagementApi {

    @POST("user/list")
    suspend fun readUser(): Response<UserListResponse>
}