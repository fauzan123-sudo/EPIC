package com.example.epic.data.model.error

data class ErrorResponse(
    val `data`: Data,
    val message: String,
    val status: Boolean
)