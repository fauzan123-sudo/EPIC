package com.example.epic.data.model.seller

data class ListSellerResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)