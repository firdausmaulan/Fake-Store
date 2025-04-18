package com.fd.fakestore.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fd.fakestore.data.model.CartWithProduct
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.fd.fakestore.data.repository.cart.ICartRepository
import com.fd.fakestore.data.repository.user.IUserRepository

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: ICartRepository,
    private val userRepo: IUserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<CheckoutState>(CheckoutState.Loading)
    val state: StateFlow<CheckoutState> = _state

    fun getCheckoutDetails(cartIds : List<Int>) {
        viewModelScope.launch {
            cartRepository.getCartItemsByIds(cartIds).collect { cartItems ->
                if (cartItems.isNotEmpty()) {
                    val totalPrice = cartItems.sumOf { it.product.price * it.cart.quantity }
                    val userResult = userRepo.getUserData()
                    if (userResult.isSuccess) {
                        val user = userResult.getOrNull()
                        if (user != null) {
                            _state.update { CheckoutState.Success(cartItems, user, totalPrice) }
                        } else {
                            _state.update { CheckoutState.Error("User not found") }
                        }
                    } else {
                        _state.update { CheckoutState.Error(userResult.exceptionOrNull()?.message ?: "Unknown error") }
                    }
                } else {
                    _state.update { CheckoutState.Error("Cart is empty") }
                }
            }
        }
    }

    fun placeOrder(cartItems: List<CartWithProduct>) {
        viewModelScope.launch {
            val cartIds = cartItems.map { it.cart.cartId }
            cartRepository.deleteCarts(cartIds)
        }
    }
}