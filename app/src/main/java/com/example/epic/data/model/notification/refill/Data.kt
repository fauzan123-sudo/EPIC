package com.example.epic.data.model.notification.refill

data class Data(
    val created_at: String,
    val created_by: String,
    val id_barang: Int,
    val id_kategori: String,
    val kode_barang: String,
    val minimal_persediaan: String,
    val nama_barang: String,
    val satuan: String,
    val token: String?,
    val updated_at: String,
    val updated_by: String
)