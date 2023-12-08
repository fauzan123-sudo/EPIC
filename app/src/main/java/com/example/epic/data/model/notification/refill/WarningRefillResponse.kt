package com.example.epic.data.model.notification.refill

data class WarningRefillResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)