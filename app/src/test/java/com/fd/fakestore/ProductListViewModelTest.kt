package com.fd.fakestore

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.fd.fakestore.data.model.Category
import com.fd.fakestore.data.model.Product
import com.fd.fakestore.data.repository.product.IProductRepository
import com.fd.fakestore.ui.product.list.ProductListState
import com.fd.fakestore.ui.product.list.ProductListViewModel
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
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ProductListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var productRepository: IProductRepository

    private lateinit var viewModel: ProductListViewModel

    private val product1 = Product(1, "Product 1", 10.0, "Description", "Category A", "")
    private val product2 = Product(2, "Product 2", 20.0, "Description", "Category B", "")
    private val product3 = Product(3, "Product 3", 30.0, "Description", "Category A", "")
    private val products = listOf(product1, product2, product3)
    private val categories = listOf(Category("Category A"), Category("Category B"))

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        `when`(productRepository.getProducts()).thenReturn(flowOf(Result.success(products)))
        viewModel = ProductListViewModel(productRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getProducts should update state to Success on successful product retrieval`() = runTest {
        advanceUntilIdle()
        assertTrue(viewModel.state.value is ProductListState.Success)
        val successState = viewModel.state.value as ProductListState.Success
        assertEquals(products, successState.products)
        assertEquals(categories, successState.categories)
    }

    @Test
    fun `getProducts should update state to Error on failed product retrieval`() = runTest {
        val errorMessage = "Failed to load products"
        `when`(productRepository.getProducts()).thenReturn(flowOf(Result.failure(RuntimeException(errorMessage))))
        viewModel = ProductListViewModel(productRepository)
        advanceUntilIdle()
        assertTrue(viewModel.state.value is ProductListState.Error)
        assertEquals(errorMessage, (viewModel.state.value as ProductListState.Error).message)
    }

    @Test
    fun `searchProducts should filter products based on query`() = runTest {
        advanceUntilIdle()
        viewModel.searchProducts("Product 1")
        advanceUntilIdle()
        assertTrue(viewModel.state.value is ProductListState.Success)
        val successState = viewModel.state.value as ProductListState.Success
        assertEquals(listOf(product1), successState.products)
    }

    @Test
    fun `toggleCategorySelection should filter products based on selected categories`() = runTest {
        advanceUntilIdle()
        viewModel.toggleCategorySelection("Category A")
        advanceUntilIdle()
        assertTrue(viewModel.state.value is ProductListState.Success)
        val successState = viewModel.state.value as ProductListState.Success
        assertEquals(listOf(product1, product3), successState.products)
    }

    @Test
    fun `toggleCategorySelection should toggle category selection and update category list`() = runTest {
        advanceUntilIdle()
        viewModel.toggleCategorySelection("Category A")
        advanceUntilIdle()
        assertTrue(viewModel.state.value is ProductListState.Success)
        val successState = viewModel.state.value as ProductListState.Success
        assertEquals(listOf(Category("Category A", true), Category("Category B")), successState.categories)

        viewModel.toggleCategorySelection("Category A")
        advanceUntilIdle()
        assertTrue(viewModel.state.value is ProductListState.Success)
        val successState2 = viewModel.state.value as ProductListState.Success
        assertEquals(listOf(Category("Category A", false), Category("Category B")), successState2.categories)
    }
}