package com.fd.fakestore.data.repository.product

import com.fd.fakestore.data.model.Product
import kotlinx.coroutines.flow.Flow

interface IProductRepository {

    fun getProducts(): Flow<Result<List<Product>>>

    fun getFilteredProducts(categories: List<String>? = null, query: String? = null): Flow<Result<List<Product>>>

    fun getProductById(productId: Int): Flow<Result<Product>>

}