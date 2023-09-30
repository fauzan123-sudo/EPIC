package com.example.epic.data.network

import com.example.epic.data.model.product.create.AddProductResponse
import com.example.epic.data.model.product.create.RequestAddProduct
import com.example.epic.data.model.product.read.ProductListResponse
import com.example.epic.data.model.product.update.RequestEditProduct
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProductApi {

    @POST("barang/create")
    suspend fun addProduct(
        @Body request:RequestAddProduct
    ) : Response<AddProductResponse>

    @POST("barang/update")
    suspend fun updateProduct(
        @Body request:RequestEditProduct
    ) : Response<AddProductResponse>

    @POST("barang/delete")
    suspend fun deleteProduct(
        @Body request:RequestAddProduct
    ) : Response<AddProductResponse>

    @GET("barang/list")
    suspend fun getProduct() : Response<ProductListResponse>
}