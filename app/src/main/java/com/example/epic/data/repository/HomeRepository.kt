package com.example.epic.data.repository

import com.example.epic.data.model.home.RequestHome
import com.example.epic.data.network.HomeApi
import javax.inject.Inject

class HomeRepository @Inject constructor(val api:HomeApi) : BaseRepository() {

    suspend fun homeApi(request: RequestHome) = safeApiCall {
        api.homeApi(request)
    }
}