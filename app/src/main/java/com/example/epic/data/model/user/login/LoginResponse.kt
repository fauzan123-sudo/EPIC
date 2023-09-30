package com.example.epic.data.model.user.login

data class LoginResponse(
    val access_token: String,
    val message: String,
    val status: Boolean,
    val token_type: String
)