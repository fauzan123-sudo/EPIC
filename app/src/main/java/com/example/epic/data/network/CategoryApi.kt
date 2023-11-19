package com.example.epic.data.network

import com.example.epic.data.model.category.add.AddCategoryResponse
import com.example.epic.data.model.category.add.RequestAddCategory
import com.example.epic.data.model.category.based.SpinnerCategoryResponse
import com.example.epic.data.model.category.delete.DeleteCategoryResponse
import com.example.epic.data.model.category.read.CategoryListResponse
import com.example.epic.data.model.category.spinner.DropDownCategoryResponse
import com.example.epic.data.model.category.update.RequestEditCategory
import com.example.epic.data.model.category.update.UpdateCategoryResponse
import com.example.epic.data.model.stock.search.SearchCategoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CategoryApi {

    @POST("kategori/create")
    suspend fun addCategory(
        @Body requestAddCategory: RequestAddCategory
    ): Response<AddCategoryResponse>

    @GET("kategori/list")
    suspend fun listCategory(): Response<CategoryListResponse>

    @GET("kategori/list")
    suspend fun spinnerCategory(): Response<DropDownCategoryResponse>

    @POST("kategori/update")
    suspend fun updateCategory(@Body request: RequestEditCategory): Response<UpdateCategoryResponse>

    @FormUrlEncoded
    @POST("kategori/delete")
    suspend fun deleteCategory(@Field("id_kategori") categoryId: String): Response<DeleteCategoryResponse>

    @GET("barang/list-by-kategori/{id_kategori}")
    suspend fun basedIdCategory(
        @Path("id_kategori") idCategory: Int
    ): Response<SpinnerCategoryResponse>

    @GET("persediaan/list-by-kategori/{categoryId}")
    suspend fun searchCategory(
        @Path("categoryId") idCategory: Int
    ): Response<SearchCategoryResponse>

    @GET("barang/dropdown-search")
    suspend fun dropDownSearch(
        @Query("q") query:String
    ) : Response<DropDownCategoryResponse>

}