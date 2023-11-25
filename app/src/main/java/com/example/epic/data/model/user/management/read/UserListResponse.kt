package com.example.epic.data.model.user.management.read

data class UserListResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Boolean
)