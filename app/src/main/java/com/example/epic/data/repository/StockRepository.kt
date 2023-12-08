package com.example.epic.data.repository

import com.example.epic.data.network.StockApi
import javax.inject.Inject

class StockRepository @Inject constructor(private val api:StockApi) : BaseRepository() {

    suspend fun getListStock(userId: Int) =
        safeApiCall {
            api.listStock(userId)
        }
}