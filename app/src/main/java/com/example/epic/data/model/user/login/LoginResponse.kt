package com.example.epic.data.model.user.login

data class LoginResponse(
    val status: Boolean,
    val message: String,
    val access_token: String,
    val token_type: String,
    val user: User
)