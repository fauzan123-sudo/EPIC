package com.example.epic.data.model.returnProduct.read

data class ReturnProductResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)