package com.fd.fakestore.ui.product.detail

import com.fd.fakestore.data.model.Product

sealed class ProductDetailState {
    data object Loading : ProductDetailState()
    data class Success(val product: Product) : ProductDetailState()
    data class Error(val message: String) : ProductDetailState()
}