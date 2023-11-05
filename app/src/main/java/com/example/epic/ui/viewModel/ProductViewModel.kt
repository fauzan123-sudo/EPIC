package com.example.epic.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epic.data.model.product.create.AddProductResponse
import com.example.epic.data.model.product.create.RequestAddProduct
import com.example.epic.data.model.product.delete.DeleteProductResponse
import com.example.epic.data.model.product.read.ProductListResponse
import com.example.epic.data.model.product.update.RequestEditProduct
import com.example.epic.data.model.product.update.UpdateProductResponse
import com.example.epic.data.model.statistic.StatisticResponse
import com.example.epic.data.repository.ProductRepository
import com.example.epic.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import xyz.teamgravity.checkinternet.CheckInternet
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductRepository) :
    ViewModel() {

    private val _createProductResponse = MutableLiveData<NetworkResult<AddProductResponse>>()
    val createProductResponse: LiveData<NetworkResult<AddProductResponse>>
        get() = _createProductResponse

    private val _readProductResponse = MutableLiveData<NetworkResult<ProductListResponse>>()
    val readProductResponse: LiveData<NetworkResult<ProductListResponse>>
        get() = _readProductResponse

    private val _updateProductResponse = MutableLiveData<NetworkResult<UpdateProductResponse>>()
    val updateProductResponse: LiveData<NetworkResult<UpdateProductResponse>>
        get() = _updateProductResponse

    private val _deleteProductResponse = MutableLiveData<NetworkResult<DeleteProductResponse>>()
    val deleteProductResponse: LiveData<NetworkResult<DeleteProductResponse>>
        get() = _deleteProductResponse

    private val _searchProductResponse = MutableLiveData<NetworkResult<ProductListResponse>>()
    val searchProductResponse: LiveData<NetworkResult<ProductListResponse>>
        get() = _searchProductResponse

    private val _readStatisticSeller = MutableLiveData<NetworkResult<StatisticResponse>>()
    val readStatisticSeller: LiveData<NetworkResult<StatisticResponse>>
        get() = _readStatisticSeller

    fun createProduct(request: RequestAddProduct) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _createProductResponse.postValue(NetworkResult.Loading())
                _createProductResponse.postValue(repository.createProduct(request))
            } else {
                _createProductResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun readProduct() {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _readProductResponse.postValue(NetworkResult.Loading())
                _readProductResponse.postValue(repository.readProduct())
            } else {
                _readProductResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun updateProuct(request: RequestEditProduct) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _updateProductResponse.postValue(NetworkResult.Loading())
                _updateProductResponse.postValue(repository.updateProduct(request))
            } else {
                _updateProductResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun deleteProuct(codeProduct: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _deleteProductResponse.postValue(NetworkResult.Loading())
                _deleteProductResponse.postValue(repository.deleteProduct(codeProduct))
            } else {
                _deleteProductResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun searchProduct(q: String) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _searchProductResponse.postValue(NetworkResult.Loading())
                _searchProductResponse.postValue(repository.searchProduct(q))
            } else {
                _searchProductResponse.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

    fun requestStatisticSeller(month: Int, year: Int) {
        viewModelScope.launch {
            val connected = CheckInternet().check()
            if (connected) {
                _readStatisticSeller.postValue(NetworkResult.Loading())
                _readStatisticSeller.postValue(repository.statisticSeller(month, year))
            } else {
                _readStatisticSeller.postValue(NetworkResult.Error("No Internet Connection"))
            }
        }
    }

}