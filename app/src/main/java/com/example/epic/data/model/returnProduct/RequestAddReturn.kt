package com.example.epic.data.model.returnProduct

data class RequestAddReturn(
    val kode_barang:String,
    val jumlah_barang:String,
    val tanggal_pengembalian:String
)
