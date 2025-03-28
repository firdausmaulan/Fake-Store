package com.fd.fakestore.ui.checkout

import com.fd.fakestore.data.model.CartWithProduct
import com.fd.fakestore.data.model.User

sealed class CheckoutState {
    data object Loading : CheckoutState()
    data class Success(
        val cartItems: List<CartWithProduct>,
        val user : User,
        val totalPrice: Double
    ) : CheckoutState()
    data class Error(val message: String) : CheckoutState()
}