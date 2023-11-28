package com.example.epic.data.network

import com.example.epic.data.model.user.management.create.CreateUserResponse
import com.example.epic.data.model.user.management.create.RequestCreateUser
import com.example.epic.data.model.user.management.read.UserListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserManagementApi {

    @POST("user/create")
    suspend fun createUser(
        @Body request:RequestCreateUser
    ) : Response<CreateUserResponse>

    @GET("user/list")
    suspend fun readUser(): Response<UserListResponse>
}