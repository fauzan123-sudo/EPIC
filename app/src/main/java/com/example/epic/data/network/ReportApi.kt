package com.example.epic.data.network

import com.example.epic.data.model.report.ReportProductResponse
import retrofit2.Response
import retrofit2.http.GET

interface ReportApi {

    @GET("report/barang")
    suspend fun readReportProduct():Response<ReportProductResponse>
}