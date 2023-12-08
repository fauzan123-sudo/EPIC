package com.example.epic.data.repository

import com.example.epic.data.model.user.management.create.RequestCreateUser
import com.example.epic.data.model.user.management.delete.RequestDeleteUser
import com.example.epic.data.model.user.management.update.RequestUpdateUser
import com.example.epic.data.network.UserManagementApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UserManagementRepository @Inject constructor(private val api: UserManagementApi) :
    BaseRepository() {

    suspend fun readUser() = safeApiCall {
        api.readUser()
    }

    suspend fun createUser(request: RequestCreateUser) = safeApiCall {
        api.createUser(request)
    }

    suspend fun updateProfilePhoto(userID: String, photoFile: File) = safeApiCall {
        val requestFile = photoFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val photoPart = MultipartBody.Part.createFormData("foto", photoFile.name, requestFile)
        api.updateProfilePhoto(userID, photoPart)
    }

    suspend fun deleteUser(request: RequestDeleteUser) = safeApiCall {
        api.deleteUser(request)
    }

    suspend fun updateUser(request: RequestUpdateUser) = safeApiCall {
        api.updateUser(request)
    }
}