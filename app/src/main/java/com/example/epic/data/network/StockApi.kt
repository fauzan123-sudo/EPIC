package com.example.epic.data.network

import com.example.epic.data.model.stock.StockProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface StockApi {

    @GET("persediaan/list")
    suspend fun listStock(

    ): Response<StockProductResponse>
}