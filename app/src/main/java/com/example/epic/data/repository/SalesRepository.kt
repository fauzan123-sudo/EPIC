package com.example.epic.data.repository

import com.example.epic.data.model.sales.RequestAddSales
import com.example.epic.data.network.SalesApi
import javax.inject.Inject

class SalesRepository @Inject constructor(private val api:SalesApi): BaseRepository() {

    suspend fun createSales(requestAddSales: RequestAddSales) =
        safeApiCall {
            api.createSales(requestAddSales)
        }

}