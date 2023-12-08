package com.example.epic.data.model.user.management.update

data class RequestUpdateUser(
    val username: String,
    val password: String,
    val id_user: Int,
    val role: Int,
    val nama_toko: String
)
