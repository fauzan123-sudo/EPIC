package com.example.epic.data.network

import com.example.epic.data.model.seller.ListSellerResponse
import com.example.epic.data.model.seller.create.CreateSellerResponse
import com.example.epic.data.model.seller.create.RequestCreateSeller
import com.example.epic.data.model.seller.delete.DeleteSellerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SellerApi {

    @GET("penjualan/list")
    suspend fun listSeller(

    ) : Response<ListSellerResponse>

    @POST("penjualan/create")
    suspend fun createSeller(
        @Body request: RequestCreateSeller
    ) : Response<CreateSellerResponse>

    @GET("penjualan/delete/{sellerID}")
    suspend fun deleteSeller(
        @Path("sellerID") sellerID:Int
    ) : Response<DeleteSellerResponse>
}