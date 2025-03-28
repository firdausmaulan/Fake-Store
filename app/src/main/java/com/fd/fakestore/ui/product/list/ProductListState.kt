package com.fd.fakestore.ui.product.list

import com.fd.fakestore.data.model.Category
import com.fd.fakestore.data.model.Product

sealed class ProductListState {
    data object Loading : ProductListState()
    data class Success(val products: List<Product>, val categories: List<Category>) : ProductListState()
    data class Error(val message: String) : ProductListState()
}