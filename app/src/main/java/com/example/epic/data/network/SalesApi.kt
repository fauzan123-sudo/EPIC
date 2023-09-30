package com.example.epic.data.network

import com.example.epic.data.model.sales.RequestAddSales
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface SalesApi {

    @POST("")
    suspend fun createSales(
        requestAddSales: RequestAddSales
    ) : Response<Any>

    @GET("sales/list")
    suspend fun readSales(): Response<Any>

    @POST("")
    suspend fun updateSales():Response<Any>

    @POST("")
    suspend fun deleteSales():Response<Any>

}