package com.fd.fakestore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fd.fakestore.data.model.Cart
import com.fd.fakestore.data.model.CartWithProduct
import com.fd.fakestore.data.model.Product
import com.fd.fakestore.data.repository.cart.ICartRepository
import com.fd.fakestore.ui.cart.CartState
import com.fd.fakestore.ui.cart.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CartViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var cartRepository: ICartRepository

    private lateinit var viewModel: CartViewModel

    // Sample test data
    private val product1 = Product(1, "Product 1", 10.0, "Description", "",  "")
    private val product2 = Product(2, "Product 2", 20.0, "Description", "",  "")

    private val cart1 = Cart(1, 1, 2, 1)
    private val cart2 = Cart(2, 2, 3, 1)

    private val cartWithProduct1 = CartWithProduct(cart1, product1, 20.0, false) // 2 * 10.0
    private val cartWithProduct2 = CartWithProduct(cart2, product2, 60.0, false) // 3 * 20.0

    private val cartList = listOf(cartWithProduct1, cartWithProduct2)

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should load cart items and update state to Success when items exist`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsWithProducts()).thenReturn(flowOf(cartList))

        // When
        viewModel = CartViewModel(cartRepository)
        advanceUntilIdle()

        // Then
        assertEquals(CartState.Success(cartList), viewModel.state.value)
        assertEquals(cartList, viewModel.allCartItems.value)
        assertFalse(viewModel.stateReadyForSubmit.value)
    }

    @Test
    fun `init should update state to Empty when no cart items exist`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsWithProducts()).thenReturn(flowOf(emptyList()))

        // When
        viewModel = CartViewModel(cartRepository)
        advanceUntilIdle()

        // Then
        assertEquals(CartState.Empty, viewModel.state.value)
        assertEquals(emptyList<CartWithProduct>(), viewModel.allCartItems.value)
        assertFalse(viewModel.stateReadyForSubmit.value)
    }

    @Test
    fun `init should update state to Error when repository throws exception`() = runTest {
        // Given
        val errorMessage = "Network error"
        `when`(cartRepository.getCartItemsWithProducts()).thenThrow(RuntimeException(errorMessage))

        // When
        viewModel = CartViewModel(cartRepository)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is CartState.Error)
        assertEquals(errorMessage, (viewModel.state.value as CartState.Error).message)
    }

    @Test
    fun `updateCartItem should update cart quantity and total price`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsWithProducts()).thenReturn(flowOf(cartList))
        viewModel = CartViewModel(cartRepository)
        advanceUntilIdle()

        val newQuantity = 5
        val updatedCart = cart1.copy(quantity = newQuantity)
        val updatedCartWithProduct = cartWithProduct1.copy(cart = updatedCart)

        // When
        viewModel.updateCartItem(cartWithProduct1, newQuantity)
        advanceUntilIdle()

        // Then
        verify(cartRepository).updateCart(updatedCartWithProduct)

        // Check that the item was updated in allCartItems
        val updatedItem = viewModel.allCartItems.value.first { it.cart.cartId == cart1.cartId }
        assertEquals(newQuantity, updatedItem.cart.quantity)
        assertEquals(newQuantity * product1.price, updatedItem.totalPrice, 0.01)

        // Verify state is Success
        assertTrue(viewModel.state.value is CartState.Success)
    }

    @Test
    fun `deleteCartItem should remove item from list and update state`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsWithProducts()).thenReturn(flowOf(cartList))
        viewModel = CartViewModel(cartRepository)
        advanceUntilIdle()

        // When
        viewModel.deleteCartItem(cartWithProduct1)
        advanceUntilIdle()

        // Then
        verify(cartRepository).deleteCartById(cartWithProduct1.cart.cartId)

        // Check that the item was removed from allCartItems
        assertEquals(1, viewModel.allCartItems.value.size)
        assertEquals(cartWithProduct2, viewModel.allCartItems.value[0])

        // Verify state is Success with updated list
        assertTrue(viewModel.state.value is CartState.Success)
        assertEquals(listOf(cartWithProduct2), (viewModel.state.value as CartState.Success).cartItems)
    }

    @Test
    fun `deleteCartItem should update state to Empty when last item is deleted`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsWithProducts()).thenReturn(flowOf(listOf(cartWithProduct1)))
        viewModel = CartViewModel(cartRepository)
        advanceUntilIdle()

        // When
        viewModel.deleteCartItem(cartWithProduct1)
        advanceUntilIdle()

        // Then
        verify(cartRepository).deleteCartById(cartWithProduct1.cart.cartId)
        assertEquals(emptyList<CartWithProduct>(), viewModel.allCartItems.value)
        assertEquals(CartState.Empty, viewModel.state.value)
    }

    @Test
    fun `toggleItemSelected should add item ID to selectedCartItemIds when not selected`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsWithProducts()).thenReturn(flowOf(cartList))
        viewModel = CartViewModel(cartRepository)
        advanceUntilIdle()

        // When
        viewModel.toggleItemSelected(cartWithProduct1)

        // Then
        assertEquals(listOf(cart1.cartId), viewModel.selectedCartItemIds.value)
        assertTrue(viewModel.stateReadyForSubmit.value)
    }

    @Test
    fun `toggleItemSelected should remove item ID from selectedCartItemIds when already selected`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsWithProducts()).thenReturn(flowOf(cartList))
        viewModel = CartViewModel(cartRepository)
        advanceUntilIdle()

        // First select the item
        viewModel.toggleItemSelected(cartWithProduct1)
        assertEquals(listOf(cart1.cartId), viewModel.selectedCartItemIds.value)

        // When - toggle again to unselect
        viewModel.toggleItemSelected(cartWithProduct1)

        // Then
        assertEquals(emptyList<Int>(), viewModel.selectedCartItemIds.value)
        assertFalse(viewModel.stateReadyForSubmit.value)
    }

    @Test
    fun `setReadyForSubmit should set value to false when allCartItems is empty`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsWithProducts()).thenReturn(flowOf(emptyList()))

        // When
        viewModel = CartViewModel(cartRepository)
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.stateReadyForSubmit.value)
    }

    @Test
    fun `setReadyForSubmit should set value to false when no items are selected`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsWithProducts()).thenReturn(flowOf(cartList))

        // When
        viewModel = CartViewModel(cartRepository)
        advanceUntilIdle()

        // Then
        assertFalse(viewModel.stateReadyForSubmit.value)
    }

    @Test
    fun `setReadyForSubmit should set value to true when cart has items and at least one is selected`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsWithProducts()).thenReturn(flowOf(cartList))
        viewModel = CartViewModel(cartRepository)
        advanceUntilIdle()

        // When
        viewModel.toggleItemSelected(cartWithProduct1)

        // Then
        assertTrue(viewModel.stateReadyForSubmit.value)
    }
}