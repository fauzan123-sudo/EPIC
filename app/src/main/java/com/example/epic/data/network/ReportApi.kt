package com.example.epic.data.network

import com.example.epic.data.model.report.ReportProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportApi {

    @GET("report/barang")
    suspend fun readReportProduct(
        @Query("id_user") userId: Int
    ):Response<ReportProductResponse>
}