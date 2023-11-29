package com.example.epic.data.model.user.logOut

data class LogOutResponse(
    val access_token: String? = null,
    val `data`: String?=null,
    val message: String,
    val status: Boolean
)