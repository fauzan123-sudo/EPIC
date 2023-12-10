package com.example.epic.data.network

import com.example.epic.data.model.user.logOut.LogOutResponse
import com.example.epic.data.model.user.profile.UserProfileResponse
import com.example.epic.data.model.user.profile.image.ImageChangeResponse
import com.example.epic.data.model.user.profile.update.RequestUpdateProfile
import com.example.epic.data.model.user.profile.update.UpdateProfileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ProfileApi {

    @GET("profile/{userId}")
    suspend fun userProfile(
        @Path("userId") userId: Int
    ): Response<UserProfileResponse>

    @Multipart
    @POST("profile/{userID}/update-foto-profile")
    suspend fun updateProfilePhoto(
        @Path("userID") userID: String,
        @Part foto: MultipartBody.Part
    ): Response<ImageChangeResponse>


    @POST("profile/{userId}/update")
    suspend fun updateProfile(
        @Path("userId") userID: String,
        @Body request: RequestUpdateProfile
    ): Response<UpdateProfileResponse>

    @GET("logout")
    suspend fun logOut(): Response<LogOutResponse>
}