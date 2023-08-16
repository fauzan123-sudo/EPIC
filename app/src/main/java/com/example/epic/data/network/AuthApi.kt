package com.example.epic.data.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthApi {

    @POST("")
    suspend fun login(
        @Field("username") username:String,
        @Field("password") password:String,
    ) :Response<Any>
}