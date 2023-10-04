package com.example.epic.data.model.sales.read

data class ListSalesResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)