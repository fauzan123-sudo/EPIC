package com.example.epic.data.repository

import com.example.epic.data.model.home.RequestHome
import com.example.epic.data.model.user.management.read.Data
import com.example.epic.data.network.HomeApi
import com.example.epic.data.room.StoreDao
import javax.inject.Inject

class HomeRepository @Inject constructor(val api: HomeApi, private val dao: StoreDao) :
    BaseRepository() {

    suspend fun homeApi(request: RequestHome) = safeApiCall {
        api.homeApi(request)
    }

    fun saveStore(data: Data) = dao.upsert(data)

    fun getSingleStore() = dao.getSingleStore()

    fun deleteStore(data: Data) = dao.delete(data)


}