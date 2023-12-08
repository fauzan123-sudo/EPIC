package com.example.epic.data.repository

import com.example.epic.data.model.returnProduct.RequestAddReturn
import com.example.epic.data.model.returnProduct.RequestDeleteReturn
import com.example.epic.data.network.ProductReturnApi
import javax.inject.Inject

class ProductReturnRepository @Inject constructor(private val api: ProductReturnApi) :
    BaseRepository() {

    suspend fun readReturnProduct(userId: Int) = safeApiCall {
        api.getListReturnProduct(userId)
    }

    suspend fun createReturnProduct(request: RequestAddReturn) = safeApiCall {
        api.createReturnProduct(request)
    }

    suspend fun deleteReturnProduct(request: RequestDeleteReturn) = safeApiCall {
        api.deleteReturnProduct(request)
    }

}