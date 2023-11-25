package com.example.epic.data.network

import com.example.epic.data.model.home.HomeResponse
import com.example.epic.data.model.home.RequestHome
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface HomeApi {

    @POST("user/home")
    suspend fun homeApi(
        @Body request:RequestHome
    ): Response<HomeResponse>
}