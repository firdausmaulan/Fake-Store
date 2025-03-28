package com.fd.fakestore.data.repository.cart

import com.fd.fakestore.data.model.Cart
import com.fd.fakestore.data.model.CartWithProduct
import com.fd.fakestore.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ICartRepository {

    suspend fun addToCart(product: Product)

    suspend fun updateCart(cartWithProduct: CartWithProduct)

    suspend fun isProductInCart(productId: Int): Boolean

    suspend fun deleteCartById(cartId: Int)

    suspend fun deleteCarts(cartIds : List<Int>)

    suspend fun getCartItemsWithProducts(): Flow<List<CartWithProduct>>

    suspend fun getCartItemsByIds(cartIds: List<Int>): Flow<List<CartWithProduct>>

}