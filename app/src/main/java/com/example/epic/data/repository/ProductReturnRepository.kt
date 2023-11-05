package com.example.epic.data.repository

import com.example.epic.data.model.returnProduct.RequestAddReturn
import com.example.epic.data.network.ProductReturnApi
import javax.inject.Inject

class ProductReturnRepository @Inject constructor(private val api: ProductReturnApi) :
    BaseRepository() {

        suspend fun readReturnProduct() = safeApiCall {
            api.getListReturnProduct()
        }

    suspend fun createReturnProduct(request: RequestAddReturn) = safeApiCall {
        api.createReturnProduct(request)
    }

}