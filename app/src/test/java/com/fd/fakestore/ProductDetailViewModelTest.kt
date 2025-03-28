package com.fd.fakestore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fd.fakestore.data.model.Product
import com.fd.fakestore.data.repository.cart.ICartRepository
import com.fd.fakestore.data.repository.product.IProductRepository
import com.fd.fakestore.ui.product.detail.ProductDetailState
import com.fd.fakestore.ui.product.detail.ProductDetailViewModel
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
class ProductDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var productRepository: IProductRepository

    @Mock
    private lateinit var cartRepository: ICartRepository

    private lateinit var viewModel: ProductDetailViewModel

    private val testProduct = Product(1, "Test Product", 10.0, "Description", "", "")

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ProductDetailViewModel(productRepository, cartRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getProductDetail should update state to Success on successful product retrieval`() =
        runTest {
            // Given
            `when`(productRepository.getProductById(1)).thenReturn(flowOf(Result.success(testProduct)))

            // When
            viewModel.getProductDetail(1)
            advanceUntilIdle()

            // Then
            assertTrue(viewModel.state.value is ProductDetailState.Success)
            assertEquals(testProduct, (viewModel.state.value as ProductDetailState.Success).product)
        }

    @Test
    fun `getProductDetail should update state to Error on failed product retrieval`() = runTest {
        // Given
        val errorMessage = "Product not found"
        `when`(productRepository.getProductById(1)).thenReturn(
            flowOf(
                Result.failure(
                    RuntimeException(errorMessage)
                )
            )
        )

        // When
        viewModel.getProductDetail(1)
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value is ProductDetailState.Error)
        assertEquals(errorMessage, (viewModel.state.value as ProductDetailState.Error).message)
    }

    @Test
    fun `isProductInCart should update isProductInCart state to true when product is in cart`() =
        runTest {
            // Given
            `when`(cartRepository.isProductInCart(1)).thenReturn(true)

            // When
            viewModel.isProductInCart(1)
            advanceUntilIdle()

            // Then
            assertTrue(viewModel.isProductInCart.value)
        }

    @Test
    fun `isProductInCart should update isProductInCart state to false when product is not in cart`() =
        runTest {
            // Given
            `when`(cartRepository.isProductInCart(1)).thenReturn(false)

            // When
            viewModel.isProductInCart(1)
            advanceUntilIdle()

            // Then
            assertFalse(viewModel.isProductInCart.value)
        }

    @Test
    fun `addToCart should add product to cart and update isProductInCart state to true`() =
        runTest {
            // Given
            `when`(cartRepository.isProductInCart(1)).thenReturn(true)

            // When
            viewModel.addToCart(testProduct)
            advanceUntilIdle()

            // Then
            verify(cartRepository).addToCart(testProduct)
            assertTrue(viewModel.isProductInCart.value)
        }
}