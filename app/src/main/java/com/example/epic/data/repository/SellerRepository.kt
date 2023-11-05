package com.example.epic.data.repository

import com.example.epic.data.model.seller.create.RequestCreateSeller
import com.example.epic.data.network.SellerApi
import javax.inject.Inject

class SellerRepository @Inject constructor(private val api: SellerApi) : BaseRepository() {

    suspend fun listSeller() =
        safeApiCall {
            api.listSeller()
        }

    suspend fun createSeller(request: RequestCreateSeller) =
        safeApiCall {
            api.createSeller(request)
        }

    suspend fun deleteSeller(request:Int) =
        safeApiCall {
            api.deleteSeller(request)
        }
}