package com.example.epic.data.model.stock.search

data class SearchCategoryResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)