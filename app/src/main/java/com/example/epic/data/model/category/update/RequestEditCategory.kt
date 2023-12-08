package com.example.epic.data.model.category.update

data class RequestEditCategory(
    val id_kategori: String,
    val kode_kategori: String,
    val nama_kategori: String,
    val id_user: Int
)