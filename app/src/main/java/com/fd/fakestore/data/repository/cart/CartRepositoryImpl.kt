package com.fd.fakestore.data.repository.cart

import com.fd.fakestore.data.local.database.dao.CartDao
import com.fd.fakestore.data.local.preference.AppPreference
import com.fd.fakestore.data.model.Cart
import com.fd.fakestore.data.model.CartWithProduct
import com.fd.fakestore.data.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao,
    private val appPreference: AppPreference
) : ICartRepository {

    override suspend fun saveToCart(product: Product) {
        val userId = appPreference.userId.first() ?: 0
        val existingCartItem = cartDao.getCartItemByProductId(product.id, userId).firstOrNull()
        if (existingCartItem != null) {
            val updatedCartItem = existingCartItem.copy(quantity = existingCartItem.quantity + 1)
            cartDao.updateCartItem(updatedCartItem)
        } else {
            val cartItem = Cart(productId = product.id, userId = userId, quantity = 1)
            cartDao.insertCartItem(cartItem)
        }
    }

    override suspend fun isProductInCart(productId: Int): Boolean {
        val userId = appPreference.userId.first() ?: 0
        return cartDao.getCartItemByProductId(productId, userId).firstOrNull() != null
    }

    override suspend fun deleteCartByProductId(productId: Int, userId: Int) {
        cartDao.deleteCartByProductId(productId, userId)
    }

    override fun getCartItemsWithProducts(userId: Int): Flow<List<CartWithProduct>> {
        return cartDao.getCartItemsWithProducts(userId)
    }
}