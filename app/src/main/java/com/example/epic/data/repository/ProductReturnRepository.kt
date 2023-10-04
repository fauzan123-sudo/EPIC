package com.example.epic.data.repository

import com.example.epic.data.network.ProductReturnApi
import javax.inject.Inject

class ProductReturnRepository @Inject constructor(private val api: ProductReturnApi) :
    BaseRepository() {
}