package com.example.epic.data.network

import com.example.epic.data.model.sales.create.CreateSalesResponse
import com.example.epic.data.model.sales.create.RequestCreateSales
import com.example.epic.data.model.sales.delete.DeleteSalesResponse
import com.example.epic.data.model.sales.read.ListSalesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SalesApi {

    @POST("sales/create")
    suspend fun createSales(
        requestAddSales: RequestCreateSales
    ) : Response<CreateSalesResponse>

    @GET("sales/list")
    suspend fun readSales(): Response<ListSalesResponse>

    @POST("")
    suspend fun updateSales():Response<Any>

    @POST("sales/delete/{id_sales}")
    suspend fun deleteSales(
        @Path("id_sales") idSales:String
    ):Response<DeleteSalesResponse>

}