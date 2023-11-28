package com.example.epic.data.model.user.management.read

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val alamat: String? = "-",
    val email: String? = "-",
    val foto: String? = "-",
    val id_user: Int,
    val jk: String? = "-",
    val nama: String? = "-",
    val nama_toko: String? = "-",
    val no_tlp: String? = "-",
    val role: String? = "-",
    val tempat_lahir: String? = "-",
    val ttl: String? = "-",
    val username: String? = "-"
) : Parcelable