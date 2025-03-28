package com.fd.fakestore.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fd.fakestore.data.model.Cart
import com.fd.fakestore.data.repository.cart.ICartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: ICartRepository
) : ViewModel() {

    private val _state = MutableStateFlow<CartState>(CartState.Loading)
    val state: StateFlow<CartState> = _state

    fun getCartItems(userId: Int) {
        viewModelScope.launch {
            cartRepository.getCartItemsWithProducts(userId).collect { cartItems ->
                _state.update { CartState.Success(cartItems) }
            }
        }
    }

    fun saveCartItem(cartItem: Cart) {
        viewModelScope.launch {
            cartRepository.saveCartItem(cartItem)
            getCartItems(cartItem.userId)
        }
    }

    fun deleteCartItem(productId: Int, userId: Int) {
        viewModelScope.launch {
            cartRepository.deleteCartByProductId(productId, userId)
            getCartItems(userId)
        }
    }
}