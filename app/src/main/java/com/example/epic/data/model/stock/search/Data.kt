package com.example.epic.data.model.stock.search

data class Data(
    val id_barang:Int,
    val kode_barang:String,
    val nama_barang:String,
    val satuan:String,
    val id_kategori:String,
    val nama_kategori:String,
    val minimal_persediaan:String,
    val persediaan:String,
)