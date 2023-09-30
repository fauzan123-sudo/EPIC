package com.example.epic.data.repository

import com.example.epic.data.model.user.login.RequestLogin
import com.example.epic.data.network.AuthApi
import javax.inject.Inject

class AuthRepository @Inject constructor(private val api: AuthApi) : BaseRepository() {

    suspend fun userLogin(requestLogin: RequestLogin) = safeApiCall {
        api.loginUser(requestLogin)
    }

}