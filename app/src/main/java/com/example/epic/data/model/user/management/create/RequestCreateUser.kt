package com.example.epic.data.model.user.management.create

data class RequestCreateUser(
    val nama_toko: String,
    val username: String,
    val password: String,
    val role: String = "2",
)
