package com.example.epic.data.network

import com.example.epic.data.model.sales.create.CreateSalesResponse
import com.example.epic.data.model.sales.create.RequestCreateSales
import com.example.epic.data.model.sales.delete.DeleteSalesResponse
import com.example.epic.data.model.sales.read.ListSalesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SalesApi {

    @POST("sales/create")
    suspend fun createSales(
       @Body requestAddSales: RequestCreateSales
    ) : Response<CreateSalesResponse>

    @GET("sales/list")
    suspend fun readSales(
        @Query("id_user") userId: Int
    ): Response<ListSalesResponse>

    @POST("")
    suspend fun updateSales():Response<Any>

    @GET("sales/delete/{id_sales}")
    suspend fun deleteSales(
        @Path("id_sales") idSales:String
    ):Response<DeleteSalesResponse>

}