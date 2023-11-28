package com.example.epic.data.model.user.management.create

data class RequestCreateUser(
    val nama_toko:String,
    val nama:String,
    val email:String,
    val username:String,
    val password:String,
    val role:String,
    val alamat:String,
    val ttl:String,
    val tempat_lahir:String,
    val jk:String,
    val no_tlp:String
)
