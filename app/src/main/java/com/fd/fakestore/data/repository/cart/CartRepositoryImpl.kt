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

    override suspend fun addToCart(product: Product) {
        val userId = appPreference.userId.first() ?: 0
        val cartItem = Cart(productId = product.id, userId = userId, quantity = 1)
        cartDao.insertCartItem(cartItem)
    }

    override suspend fun updateCart(cartWithProduct: CartWithProduct) {
        cartDao.updateCartItem(cartWithProduct.cart)
    }

    override suspend fun isProductInCart(productId: Int): Boolean {
        val userId = appPreference.userId.first() ?: 0
        return cartDao.getCartItemByProductId(productId, userId).firstOrNull() != null
    }

    override suspend fun deleteCartById(cartId: Int) {
        cartDao.deleteCartById(cartId)
    }

    override suspend fun deleteCarts(cartIds : List<Int>) {
        cartDao.deleteCarts(cartIds)
    }

    override suspend fun getCartItemsWithProducts(): Flow<List<CartWithProduct>> {
        val userId = appPreference.userId.first() ?: 0
        return cartDao.getCartItemsWithProducts(userId)
    }

    override suspend fun getCartItemsByIds(cartIds: List<Int>): Flow<List<CartWithProduct>> {
        val userId = appPreference.userId.first() ?: 0
        return cartDao.getCartItemsByIds(cartIds, userId)
    }
}