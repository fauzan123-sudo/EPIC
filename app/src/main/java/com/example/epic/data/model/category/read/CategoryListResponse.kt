package com.example.epic.data.model.category.read

data class CategoryListResponse(
    val status: Boolean,
    val message: String,
    val data : List<Data>
)