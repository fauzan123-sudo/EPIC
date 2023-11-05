package com.example.epic.data.model.report

data class ReportProductResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)