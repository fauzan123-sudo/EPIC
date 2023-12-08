package com.example.epic.data.model.product.create

data class RequestAddProduct(
    val kode_barang:String,
    val nama_barang:String,
    val satuan:String,
    val id_kategori:String,
    val minimal_persediaan:String,
    val is_create:Int =1,
    val id_user:Int
)