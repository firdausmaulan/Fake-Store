package com.fd.fakestore.ui.cart

import com.fd.fakestore.data.model.CartWithProduct

sealed class CartState {
    data object Loading : CartState()
    data class Success(val cartItems: List<CartWithProduct>) : CartState()
    data class Error(val message: String) : CartState()
}