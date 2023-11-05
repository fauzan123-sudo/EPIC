package com.example.epic.data.model.product.read

data class Data(
    val id_barang: Int,
    val kode_barang: Int,
    val minimal_persediaan: String,
    val nama_barang: String,
    val nama_kategori: String,
    val satuan: String,
    val id_kategori: String

)