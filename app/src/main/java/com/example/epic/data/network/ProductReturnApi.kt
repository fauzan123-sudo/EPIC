package com.example.epic.data.network

import com.example.epic.data.model.returnProduct.RequestAddReturn
import com.example.epic.data.model.returnProduct.RequestDeleteReturn
import com.example.epic.data.model.returnProduct.create.CreateReturnProductResponse
import com.example.epic.data.model.returnProduct.delete.DeleteReturnProductResponse
import com.example.epic.data.model.returnProduct.read.ReturnProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductReturnApi {

    @GET("return/list")
    suspend fun getListReturnProduct(
        @Query("id_user") userId: Int
    ): Response<ReturnProductResponse>

    @POST("return/create")
    suspend fun createReturnProduct(
        @Body request: RequestAddReturn
    ): Response<CreateReturnProductResponse>

    @POST("return/delete")
    suspend fun deleteReturnProduct(
        @Body request: RequestDeleteReturn
    )  : Response<DeleteReturnProductResponse>


}