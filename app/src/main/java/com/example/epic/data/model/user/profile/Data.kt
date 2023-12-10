package com.example.epic.data.model.user.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val id_user: Int,
    val nama_toko: String? = "-",
    val nama: String? = "-",
    val email: String? = "-",
    val username: String? = "-",
    val password_show: String? = "-",
    val role: String? = "-",
    val foto: String? = "-",
    val alamat: String? = "-",
    val ttl: String? = "-",
    val tempat_lahir: String? = "-",
    val jk: String? = "-",
    val no_tlp: String? = "-",
    val alamat_toko:String? = "-"
) : Parcelable