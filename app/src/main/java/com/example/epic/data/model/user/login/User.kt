package com.example.epic.data.model.user.login

data class User(
    val id_user: Int,
    val nama: String,
    val email: String,
    val nama_toko: String,
    val role: String,
    val username: String,
    val password: String,
    val foto: String,
    val created_at: String,
    val updated_at: String
)