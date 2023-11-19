package com.example.epic.data.repository

import com.example.epic.data.model.category.add.RequestAddCategory
import com.example.epic.data.model.category.update.RequestEditCategory
import com.example.epic.data.network.CategoryApi
import javax.inject.Inject

class CategoryRepository @Inject constructor(private val api: CategoryApi) : BaseRepository() {

    suspend fun listCategory() = safeApiCall {
        api.listCategory()
    }

    suspend fun spinnerCategory() = safeApiCall {
        api.spinnerCategory()
    }

    suspend fun addCategory(request:RequestAddCategory) = safeApiCall {
        api.addCategory(request)
    }

    suspend fun updateCategory(requestEditCategory: RequestEditCategory) =
        safeApiCall {
            api.updateCategory(requestEditCategory)
        }

    suspend fun deleteCategory(idCategory:String) =
        safeApiCall {
            api.deleteCategory(idCategory)
        }

    suspend fun baseCategory(basedIdCategory:Int) =
        safeApiCall {
            api.basedIdCategory(basedIdCategory)
        }

    suspend fun searchCategory(categoryId:Int) =
        safeApiCall {
            api.searchCategory(categoryId)
        }

    suspend fun dropDownCategory(q:String) =
        safeApiCall {
            api.dropDownSearch(q)
        }

}