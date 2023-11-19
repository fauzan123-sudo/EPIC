package com.example.epic.data.model.category.read

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val id_kategori: Int,
    val kode_kategori: String,
    val nama_kategori: String,
) : Parcelable