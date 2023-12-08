package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.category.add.AddCategoryResponse
import com.example.epic.data.model.category.add.RequestAddCategory
import com.example.epic.data.model.category.based.SpinnerCategoryResponse
import com.example.epic.data.model.category.delete.DeleteCategoryResponse
import com.example.epic.data.model.category.delete.RequestDeleteCategory
import com.example.epic.data.model.category.read.CategoryListResponse
import com.example.epic.data.model.category.spinner.DropDownCategoryResponse
import com.example.epic.data.model.category.update.RequestEditCategory
import com.example.epic.data.model.category.update.UpdateCategoryResponse
import com.example.epic.data.model.stock.search.SearchCategoryResponse
import com.example.epic.data.repository.CategoryRepository
import com.example.epic.util.NetworkResult
import com.example.epic.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repository: CategoryRepository) :
    ViewModel() {

    private val _listCategoryResponse = SingleLiveEvent<NetworkResult<CategoryListResponse>>()
    val listCategoryResponse: LiveData<NetworkResult<CategoryListResponse>>
        get() = _listCategoryResponse

    private val _showCategoryResponse = SingleLiveEvent<NetworkResult<DropDownCategoryResponse>>()
    val showCategoryResponse: LiveData<NetworkResult<DropDownCategoryResponse>>
        get() = _showCategoryResponse

    private val _addCategoryResponse = SingleLiveEvent<NetworkResult<AddCategoryResponse>>()
    val addCategoryResponse: LiveData<NetworkResult<AddCategoryResponse>>
        get() = _addCategoryResponse

    private val _updateCategoryResponse = SingleLiveEvent<NetworkResult<UpdateCategoryResponse>>()
    val updateCategoryResponse: LiveData<NetworkResult<UpdateCategoryResponse>>
        get() = _updateCategoryResponse

    private val _deleteCategoryResponse = SingleLiveEvent<NetworkResult<DeleteCategoryResponse>>()
    val deleteCategoryResponse: LiveData<NetworkResult<DeleteCategoryResponse>>
        get() = _deleteCategoryResponse

    private val _basedCategoryResponse = SingleLiveEvent<NetworkResult<SpinnerCategoryResponse>>()
    val basedCategoryResponse: LiveData<NetworkResult<SpinnerCategoryResponse>>
        get() = _basedCategoryResponse

    private val _searchCategory = SingleLiveEvent<NetworkResult<SearchCategoryResponse>>()
    val searchCategory : LiveData<NetworkResult<SearchCategoryResponse>>
        get() = _searchCategory

    private val _dropDownCategory = SingleLiveEvent<NetworkResult<DropDownCategoryResponse>>()
    val dropDownCategory : LiveData<NetworkResult<DropDownCategoryResponse>>
        get() = _dropDownCategory

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

    fun requestListCategory(userId:Int) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _listCategoryResponse.postValue(NetworkResult.Loading())
                _listCategoryResponse.postValue(repository.listCategory(userId))
            } else
                _listCategoryResponse.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }

    fun requestShowCategory() {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _showCategoryResponse.postValue(NetworkResult.Loading())
                _showCategoryResponse.postValue(repository.spinnerCategory())
            } else
                _showCategoryResponse.postValue(NetworkResult.Error("No Internet Connection"))
        }
    }



    fun updateCategory(request: RequestEditCategory) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _updateCategoryResponse.postValue(NetworkResult.Loading())
                _updateCategoryResponse.postValue(repository.updateCategory(request))
            } else {
                _updateCategoryResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun deleteCategory(request: RequestDeleteCategory) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _deleteCategoryResponse.postValue(NetworkResult.Loading())
                _deleteCategoryResponse.postValue(repository.deleteCategory(request))
            } else {
                _deleteCategoryResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun requestProductByCategory(idCategory: Int) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _basedCategoryResponse.postValue(NetworkResult.Loading())
                _basedCategoryResponse.postValue(repository.baseCategory(idCategory))
            } else {
                _basedCategoryResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }


    fun requestSearchByCategory(idCategory: Int) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _searchCategory.postValue(NetworkResult.Loading())
                _searchCategory.postValue(repository.searchCategory(idCategory))
            } else {
                _searchCategory.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun requestDropDownCategory(q:String){
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _dropDownCategory.postValue(NetworkResult.Loading())
                _dropDownCategory.postValue(repository.dropDownCategory(q))
            }else{
                _dropDownCategory.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

}