package com.fd.fakestore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fd.fakestore.data.model.Cart
import com.fd.fakestore.data.model.CartWithProduct
import com.fd.fakestore.data.model.Product
import com.fd.fakestore.data.model.User
import com.fd.fakestore.data.repository.cart.ICartRepository
import com.fd.fakestore.data.repository.user.IUserRepository
import com.fd.fakestore.ui.checkout.CheckoutState
import com.fd.fakestore.ui.checkout.CheckoutViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CheckoutViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var cartRepository: ICartRepository

    @Mock
    private lateinit var userRepository: IUserRepository

    private lateinit var viewModel: CheckoutViewModel

    // Sample test data
    private val product1 = Product(1, "Product 1", 10.0, "Description", "", "")
    private val product2 = Product(2, "Product 2", 20.0, "Description", "", "")

    private val cart1 = Cart(1, 1, 2, 1)
    private val cart2 = Cart(2, 2, 3, 1)

    private val cartWithProduct1 = CartWithProduct(cart1, product1, 20.0, false) // 2 * 10.0
    private val cartWithProduct2 = CartWithProduct(cart2, product2, 60.0, false) // 3 * 20.0

    private val cartList = listOf(cartWithProduct1, cartWithProduct2)
    private val cartIds = cartList.map { it.cart.cartId }

    private val user = User(1, "John Doe", "john@example.com", "123 Main St")

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
    fun `getCheckoutDetails should update state to Success with cart items, user, and total price`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsByIds(cartIds)).thenReturn(flowOf(cartList))
        `when`(userRepository.getUserData()).thenReturn(Result.success(user))

        // When
        viewModel = CheckoutViewModel(cartRepository, userRepository)
        viewModel.getCheckoutDetails(cartIds)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is CheckoutState.Success)
        val successState = viewModel.state.value as CheckoutState.Success
        assertEquals(cartList, successState.cartItems)
        assertEquals(user, successState.user)
        assertEquals(80.0, successState.totalPrice, 0.01)
    }

    @Test
    fun `getCheckoutDetails should update state to Error when cart is empty`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsByIds(cartIds)).thenReturn(flowOf(emptyList()))

        // When
        viewModel = CheckoutViewModel(cartRepository, userRepository)
        viewModel.getCheckoutDetails(cartIds)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is CheckoutState.Error)
        assertEquals("Cart is empty", (viewModel.state.value as CheckoutState.Error).message)
    }

    @Test
    fun `getCheckoutDetails should update state to Error when user is null`() = runTest {
        // Given
        `when`(cartRepository.getCartItemsByIds(cartIds)).thenReturn(flowOf(cartList))
        `when`(userRepository.getUserData()).thenReturn(Result.failure(RuntimeException("User not found")))

        // When
        viewModel = CheckoutViewModel(cartRepository, userRepository)
        viewModel.getCheckoutDetails(cartIds)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is CheckoutState.Error)
        assertEquals("User not found", (viewModel.state.value as CheckoutState.Error).message)
    }

    @Test
    fun `getCheckoutDetails should update state to Error when user repository returns error`() = runTest {
        // Given
        val errorMessage = "Network error"
        `when`(cartRepository.getCartItemsByIds(cartIds)).thenReturn(flowOf(cartList))
        `when`(userRepository.getUserData()).thenReturn(Result.failure(RuntimeException(errorMessage)))

        // When
        viewModel = CheckoutViewModel(cartRepository, userRepository)
        viewModel.getCheckoutDetails(cartIds)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is CheckoutState.Error)
        assertEquals(errorMessage, (viewModel.state.value as CheckoutState.Error).message)
    }

    @Test
    fun `placeOrder should delete carts by ids`() = runTest {
        // Given
        viewModel = CheckoutViewModel(cartRepository, userRepository)

        // When
        viewModel.placeOrder(cartList)
        advanceUntilIdle()

        // Then
        verify(cartRepository).deleteCarts(cartIds)
    }
}