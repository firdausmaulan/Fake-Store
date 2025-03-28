package com.fd.fakestore.data.repository.cart

import com.fd.fakestore.data.model.Cart
import com.fd.fakestore.data.model.CartWithProduct
import com.fd.fakestore.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ICartRepository {

    suspend fun saveToCart(product: Product)

    suspend fun isProductInCart(productId: Int): Boolean

    suspend fun deleteCartByProductId(productId: Int, userId: Int)

    fun getCartItemsWithProducts(userId: Int): Flow<List<CartWithProduct>>

}