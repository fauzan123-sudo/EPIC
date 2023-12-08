package com.example.epic.data.model.category.add

data class RequestAddCategory(
    val kode_kategori: String,
    val nama_kategori: String,
    val id_user: Int
)