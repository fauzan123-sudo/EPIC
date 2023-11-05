package com.example.epic.data.repository

import com.example.epic.data.model.product.create.RequestAddProduct
import com.example.epic.data.model.product.update.RequestEditProduct
import com.example.epic.data.network.ProductApi
import javax.inject.Inject

class ProductRepository @Inject constructor(private val api: ProductApi) :
    BaseRepository() {

    suspend fun createProduct(request: RequestAddProduct) =
        safeApiCall {
            api.addProduct(request)
        }

    suspend fun readProduct() =
        safeApiCall {
            api.getProduct()
        }

    suspend fun searchProduct(q:String) =
        safeApiCall {
            api.searchProduct(q)
        }

    suspend fun updateProduct(request: RequestEditProduct) =
        safeApiCall {
            api.updateProduct(request)
        }

    suspend fun deleteProduct(codeProduct: String) =
        safeApiCall {
            api.deleteProduct(codeProduct)
        }

    suspend fun statisticSeller(month: Int, year: Int) = safeApiCall {
        api.statisticSeller(month, year)
    }
}