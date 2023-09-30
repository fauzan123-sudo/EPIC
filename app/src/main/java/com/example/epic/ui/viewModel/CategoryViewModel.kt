package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.category.add.AddCategoryResponse
import com.example.epic.data.model.category.add.RequestAddCategory
import com.example.epic.data.model.category.read.CategoryListResponse
import com.example.epic.data.repository.CategoryRepository
import com.example.epic.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: CategoryRepository) :
    ViewModel() {

    private val _listCategoryResponse = MutableLiveData<NetworkResult<CategoryListResponse>>()
    val listCategoryResponse: LiveData<NetworkResult<CategoryListResponse>>
        get() = _listCategoryResponse

    private val _addCategoryResponse = MutableLiveData<NetworkResult<AddCategoryResponse>>()
    val addCategoryResponse: LiveData<NetworkResult<AddCategoryResponse>>
        get() = _addCategoryResponse

    private val _updateCategoryResponse = MutableLiveData<NetworkResult<AddCategoryResponse>>()
    val updateCategoryResponse: LiveData<NetworkResult<AddCategoryResponse>>
        get() = _updateCategoryResponse

    private val _deleteCategoryResponse = MutableLiveData<NetworkResult<AddCategoryResponse>>()
    val deleteCategoryResponse: LiveData<NetworkResult<AddCategoryResponse>>
        get() = _deleteCategoryResponse

    fun requestListCategory() {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _listCategoryResponse.postValue(NetworkResult.Loading())
                _listCategoryResponse.postValue(repository.listCategory())
            } else
                _listCategoryResponse.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }

    fun requestAddCategory(request: RequestAddCategory) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _addCategoryResponse.postValue(NetworkResult.Loading())
                _addCategoryResponse.postValue(repository.addCategory(request))
            } else
                _addCategoryResponse.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }
}