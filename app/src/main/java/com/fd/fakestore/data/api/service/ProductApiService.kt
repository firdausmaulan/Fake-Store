package com.fd.fakestore.data.api.service

import com.fd.fakestore.data.api.core.AppHttpClient
import com.fd.fakestore.data.model.Product

class ProductApiService {

    suspend fun getProducts(): Result<List<Product>> {
        return AppHttpClient.get("products")
    }

}