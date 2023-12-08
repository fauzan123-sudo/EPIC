package com.example.epic.data.model.product.update

data class RequestEditProduct(
    val id_barang: String,
    val kode_barang: String,
    val nama_barang: String,
    val satuan: String,
    val id_kategori: String,
    val minimal_persediaan: String,
    val is_create: Int = 0,
    val id_user: Int
)