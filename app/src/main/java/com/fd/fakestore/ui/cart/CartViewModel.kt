package com.fd.fakestore.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fd.fakestore.data.model.CartWithProduct
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

    val allCartItems = MutableStateFlow<List<CartWithProduct>>(emptyList())

    private val _selectedCartItemIds = MutableStateFlow<List<Int>>(emptyList())
    val selectedCartItemIds: StateFlow<List<Int>> = _selectedCartItemIds

    private val _stateReadyForSubmit = MutableStateFlow(false)
    val stateReadyForSubmit: StateFlow<Boolean> = _stateReadyForSubmit

    init {
        getCartItems()
    }

    fun getCartItems() {
        viewModelScope.launch {
            try {
                cartRepository.getCartItemsWithProducts().collect { cartItems ->
                    allCartItems.value =
                        cartItems.map { it.copy(totalPrice = it.cart.quantity * it.product.price) }
                    _state.update {
                        if (cartItems.isEmpty()) CartState.Empty else CartState.Success(allCartItems.value)
                    }
                    setReadyForSubmit()
                }
            } catch (e: Exception) {
                _state.update { CartState.Error(e.message ?: "An unexpected error occurred.") }
            }
        }
    }

    fun updateCartItem(cartWithProduct: CartWithProduct, newQuantity: Int) {
        viewModelScope.launch {
            try {
                cartWithProduct.cart.quantity = newQuantity
                cartRepository.updateCart(cartWithProduct)

                allCartItems.update { currentItems ->
                    currentItems.map { item ->
                        if (item.cart.cartId == cartWithProduct.cart.cartId) {
                            item.copy(
                                cart = cartWithProduct.cart,
                                totalPrice = cartWithProduct.cart.quantity * item.product.price
                            )
                        } else {
                            item
                        }
                    }
                }
                _state.update { CartState.Success(allCartItems.value) }

            } catch (e: Exception) {
                _state.update { CartState.Error(e.message ?: "Failed to update cart item.") }
            }
        }
    }

    fun deleteCartItem(cartWithProduct: CartWithProduct) {
        viewModelScope.launch {
            try {

                toggleItemSelected(cartWithProduct)

                cartRepository.deleteCartById(cartWithProduct.cart.cartId)

                allCartItems.update { currentItems ->
                    currentItems.filter { it.cart.cartId != cartWithProduct.cart.cartId }
                }
                _state.update {
                    if (allCartItems.value.isEmpty()) CartState.Empty else CartState.Success(
                        allCartItems.value
                    )
                }

                setReadyForSubmit()

            } catch (e: Exception) {
                _state.update { CartState.Error(e.message ?: "Failed to delete cart item.") }
            }
        }
    }

    fun toggleItemSelected(cartWithProduct: CartWithProduct) {
        _selectedCartItemIds.update { currentIds ->
            if (currentIds.contains(cartWithProduct.cart.cartId)) {
                currentIds.filter { it != cartWithProduct.cart.cartId }
            } else {
                currentIds + cartWithProduct.cart.cartId
            }
        }
        setReadyForSubmit()
    }

    private fun setReadyForSubmit() {
        if (allCartItems.value.isEmpty()) {
            _stateReadyForSubmit.value = false
            return
        }
        _stateReadyForSubmit.value = _selectedCartItemIds.value.isNotEmpty()
    }
}