package com.example.epic.data.repository

import com.example.epic.data.model.user.profile.update.RequestUpdateProfile
import com.example.epic.data.network.ProfileApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UserProfileRepository @Inject constructor(private val api: ProfileApi) : BaseRepository() {

    suspend fun readProfile(userId: Int) = safeApiCall {
        api.userProfile(userId)
    }

    suspend fun updateProfilePhoto(userID: String, photoFile: File) = safeApiCall {
        val requestFile = photoFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("foto", photoFile.name, requestFile)
        api.updateProfilePhoto(userID, photoPart)
    }

    suspend fun logOut() = safeApiCall {
        api.logOut()
    }

    suspend fun updateProfile(userId: String, request: RequestUpdateProfile) = safeApiCall {
        api.updateProfile(userId, request)
    }
}