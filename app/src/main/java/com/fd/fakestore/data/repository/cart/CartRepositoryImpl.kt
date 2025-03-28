package com.fd.fakestore.data.repository.cart

import com.fd.fakestore.data.local.database.dao.CartDao
import com.fd.fakestore.data.model.Cart
import com.fd.fakestore.data.model.CartWithProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : ICartRepository {

    override suspend fun saveCartItem(cartItem: Cart) {
        val existingCartItem = cartDao.getCartItemByProductId(cartItem.productId, cartItem.userId).firstOrNull()

        if (existingCartItem != null) {
            val updatedCartItem = cartItem.copy(cartId = existingCartItem.cartId)
            cartDao.updateCartItem(updatedCartItem)
        } else {
            cartDao.insertCartItem(cartItem)
        }
    }

    override suspend fun deleteCartByProductId(productId: Int, userId: Int) {
        cartDao.deleteCartByProductId(productId, userId)
    }

    override fun getCartItemsWithProducts(userId: Int): Flow<List<CartWithProduct>> {
        return cartDao.getCartItemsWithProducts(userId)
    }
}