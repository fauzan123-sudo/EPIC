package com.example.epic.data.repository

import com.example.epic.data.model.user.management.create.RequestCreateUser
import com.example.epic.data.network.UserManagementApi
import javax.inject.Inject

class UserManagementRepository @Inject constructor(private val api: UserManagementApi) :
    BaseRepository() {

    suspend fun readUser() = safeApiCall {
        api.readUser()
    }

    suspend fun createUser(request: RequestCreateUser) = safeApiCall {
        api.createUser(request)
    }
}