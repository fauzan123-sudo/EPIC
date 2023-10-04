package com.example.epic.data.model.stock

data class StockProductResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)