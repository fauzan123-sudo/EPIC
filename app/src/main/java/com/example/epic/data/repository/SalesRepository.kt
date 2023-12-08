package com.example.epic.data.repository

import com.example.epic.data.model.sales.create.RequestCreateSales
import com.example.epic.data.network.SalesApi
import javax.inject.Inject

class SalesRepository @Inject constructor(private val api: SalesApi) : BaseRepository() {

    suspend fun createSales(requestAddSales: RequestCreateSales) =
        safeApiCall {
            api.createSales(requestAddSales)
        }

    suspend fun readSales(userId:Int) =
        safeApiCall {
            api.readSales(userId)
        }

    suspend fun deleteSales(idSales: String) =
        safeApiCall {
            api.deleteSales(idSales)
        }

}