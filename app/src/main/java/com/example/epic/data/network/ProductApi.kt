package com.example.epic.data.network

import com.example.epic.data.model.product.create.AddProductResponse
import com.example.epic.data.model.product.create.RequestAddProduct
import com.example.epic.data.model.product.delete.DeleteProductResponse
import com.example.epic.data.model.product.read.ProductListResponse
import com.example.epic.data.model.product.update.RequestEditProduct
import com.example.epic.data.model.product.update.UpdateProductResponse
import com.example.epic.data.model.statistic.StatisticResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ProductApi {

    @POST("barang/create")
    suspend fun addProduct(
        @Body request: RequestAddProduct
    ): Response<AddProductResponse>

    @POST("barang/update")
    suspend fun updateProduct(
        @Body request: RequestEditProduct
    ): Response<UpdateProductResponse>

    @FormUrlEncoded
    @POST("barang/delete")
    suspend fun deleteProduct(
        @Field("kode_barang") codeProduct: String
    ): Response<DeleteProductResponse>

    @GET("barang/list")
    suspend fun getProduct(): Response<ProductListResponse>

    @GET("barang/dropdown-search?q=premium")
    suspend fun searchProduct(
        @Query("q") q: String? = null,
    ): Response<ProductListResponse>

    @GET("statistik/penjualan")
    suspend fun statisticSeller(
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Response<StatisticResponse>
}