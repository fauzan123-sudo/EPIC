package com.example.epic.data.repository

import com.example.epic.data.network.ReportApi
import javax.inject.Inject

class ReportRepository @Inject constructor(val api: ReportApi) : BaseRepository() {

    suspend fun readReport(userId: Int) = safeApiCall {
        api.readReportProduct(userId)
    }
}