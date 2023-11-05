package com.example.epic.data.model.stock.search

data class SearchProductResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)