package com.fd.fakestore.data.repository.cart

import com.fd.fakestore.data.model.Cart
import com.fd.fakestore.data.model.CartWithProduct
import kotlinx.coroutines.flow.Flow

interface ICartRepository {

    suspend fun saveCartItem(cartItem: Cart)

    suspend fun deleteCartByProductId(productId: Int, userId: Int)

    fun getCartItemsWithProducts(userId: Int): Flow<List<CartWithProduct>>

}