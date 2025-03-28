package com.fd.fakestore.ui.product.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fd.fakestore.data.repository.product.IProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: IProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ProductDetailState>(ProductDetailState.Loading)
    val state: StateFlow<ProductDetailState> = _state

    fun getProductDetail(productId: Int) {
        viewModelScope.launch {
            productRepository.getProductById(productId).collect { result ->
                result.fold(
                    onSuccess = { product ->
                        _state.update { ProductDetailState.Success(product) }
                    },
                    onFailure = { error ->
                        _state.update { ProductDetailState.Error(error.message ?: "Unknown Error") }
                    }
                )
            }
        }
    }
}