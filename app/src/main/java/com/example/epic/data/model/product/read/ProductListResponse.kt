package com.example.epic.data.model.product.read

data class ProductListResponse(
    val data: List<Data>,
    val message: String,
    val status: Boolean
)