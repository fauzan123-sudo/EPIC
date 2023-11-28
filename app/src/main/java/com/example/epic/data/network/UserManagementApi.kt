package com.example.epic.data.network

import com.example.epic.data.model.user.management.create.CreateUserResponse
import com.example.epic.data.model.user.management.create.RequestCreateUser
import com.example.epic.data.model.user.management.read.UserListResponse
import com.example.epic.data.model.user.profile.image.ImageChangeResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface UserManagementApi {

    @POST("user/create")
    suspend fun createUser(
        @Body request:RequestCreateUser
    ) : Response<CreateUserResponse>

    @GET("user/list")
    suspend fun readUser(): Response<UserListResponse>

    @Multipart
    @POST("user/update-foto-profile/{id_user}")
    suspend fun updateProfilePhoto(
        @Path("id_user") userID: String,
        @Part foto: MultipartBody.Part
    ): Response<ImageChangeResponse>

}