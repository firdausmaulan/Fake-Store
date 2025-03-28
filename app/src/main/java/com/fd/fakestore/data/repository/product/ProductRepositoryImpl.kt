package com.fd.fakestore.data.repository.product

import com.fd.fakestore.data.api.service.ProductApiService
import com.fd.fakestore.data.local.database.dao.ProductDao
import com.fd.fakestore.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApiService: ProductApiService,
    private val productDao: ProductDao
) : IProductRepository {

    override fun getProducts(categories: List<String>?, query: String?): Flow<Result<List<Product>>> = flow {
        try {
            val apiResult = productApiService.getProducts()
            if (apiResult.isSuccess) {
                val products = apiResult.getOrNull() ?: emptyList()
                productDao.insertProducts(products)
            } else {
                emit(Result.failure(apiResult.exceptionOrNull() ?: Exception("Network request failed")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }

        val localProducts = try {
            productDao.getProducts().firstOrNull() ?: emptyList()
        } catch (e: Exception) {
            emit(Result.failure(e))
            return@flow
        }

        var filteredProducts = localProducts

        if (!categories.isNullOrEmpty()) {
            filteredProducts = filteredProducts.filter { it.category in categories }
        }

        if (!query.isNullOrEmpty()) {
            filteredProducts = filteredProducts.filter { it.title.contains(query, ignoreCase = true) }
        }

        emit(Result.success(filteredProducts))
    }

    override fun getProductById(productId: Int): Flow<Result<Product>> = flow {
        try {
            val result = productDao.getProductById(productId)
            val product : Product? = result?.firstOrNull()
            if (product != null) {
                emit(Result.success(product))
            } else {
                emit(Result.failure(Exception("Product not found")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}