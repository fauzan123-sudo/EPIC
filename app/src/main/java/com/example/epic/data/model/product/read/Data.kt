package com.example.epic.data.model.product.read

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val id_barang: Int,
    val kode_barang: String? = "-",
    val nama_barang: String? = "-",
    val satuan: String? = "-",
    val id_kategori: Int? = 0,
    val nama_kategori: String? = "-",
    val minimal_persediaan: String? = "-",
    val persediaan: String? = "-"

) : Parcelable