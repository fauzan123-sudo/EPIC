package com.example.epic.data.model.user.management.read

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.epic.util.Constants.STORE_NAME
import kotlinx.parcelize.Parcelize

@Entity(tableName = STORE_NAME)
@Parcelize
data class Data(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var alamat: String? = "-",
    var email: String? = "-",
    var foto: String? = "-",
    var id_user: Int = 0,
    var jk: String? = "-",
    var nama: String? = "-",
    var nama_toko: String? = "-",
    var no_tlp: String? = "-",
    var role: String? = "-",
    var tempat_lahir: String? = "-",
    var ttl: String? = "-",
    var username: String? = "-",
    var password_show: String? = "-"
) : Parcelable