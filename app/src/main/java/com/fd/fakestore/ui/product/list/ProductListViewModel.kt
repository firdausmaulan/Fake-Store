package com.fd.fakestore.ui.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fd.fakestore.data.model.Category
import com.fd.fakestore.data.repository.product.IProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val productRepository: IProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ProductListState>(ProductListState.Loading)
    val state: StateFlow<ProductListState> = _state

    private var allCategories: List<Category> = emptyList()
    private var selectedCategories: MutableList<String> = mutableListOf()
    private var currentQuery: String? = null

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts().map { result ->
                result.fold(
                    onSuccess = { products ->
                        allCategories = products.map { it.category }.distinct().map { Category(it) }
                        ProductListState.Success(products, allCategories)
                    },
                    onFailure = { error ->
                        ProductListState.Error(error.message ?: "Unknown Error")
                    }
                )
            }.catch { error ->
                _state.update { ProductListState.Error(error.message ?: "Unknown Error") }
            }.collect { newState ->
                _state.update { newState }
            }
        }
    }

    private fun getFilteredProducts() {
        viewModelScope.launch {
            productRepository.getFilteredProducts(
                selectedCategories.takeIf { it.isNotEmpty() },
                currentQuery
            ).map { result ->
                result.fold(
                    onSuccess = { products ->
                        val categories = allCategories.map { it.copy(selected = selectedCategories.contains(it.name)) }
                        ProductListState.Success(products, categories)
                    },
                    onFailure = { error ->
                        ProductListState.Error(error.message ?: "Unknown Error")
                    }
                )
            }.catch { error ->
                _state.update { ProductListState.Error(error.message ?: "Unknown Error") }
            }.collect { newState ->
                _state.update { newState }
            }
        }
    }

    fun searchProducts(query: String) {
        currentQuery = query.takeIf { it.isNotEmpty() }
        getFilteredProducts()
    }

    fun toggleCategorySelection(categoryName: String) {
        if (selectedCategories.contains(categoryName)) {
            selectedCategories.remove(categoryName)
        } else {
            selectedCategories.add(categoryName)
        }
        getFilteredProducts()
    }
}