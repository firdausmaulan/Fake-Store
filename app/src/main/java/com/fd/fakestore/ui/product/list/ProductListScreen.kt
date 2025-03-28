package com.fd.fakestore.ui.product.list

import EmptyScreen
import ErrorScreen
import LoadingScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fd.fakestore.R
import com.fd.fakestore.data.model.Category
import com.fd.fakestore.data.model.Product
import com.fd.fakestore.helper.AppColor
import com.fd.fakestore.ui.common.DebounceTextField
import com.fd.fakestore.ui.profile.ProfileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = hiltViewModel(),
    onCartClick: () -> Unit = {},
    onProductClick: (Product) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColor.background())
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            if (isSheetOpen) {
                ModalBottomSheet(
                    sheetState = sheetState,
                    onDismissRequest = { isSheetOpen = false },
                    containerColor = Color.White
                ) {
                    ProfileScreen(
                        onDismiss = { isSheetOpen = false },
                        onLogout = {
                            isSheetOpen = false
                            onLogout()
                        }
                    )
                }
            }

            // Top Section: Search, Cart, Avatar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DebounceTextField {
                    searchQuery = it
                    viewModel.searchProducts(it)
                }
                IconButton(onClick = onCartClick ) {
                    Icon(
                        Icons.Filled.ShoppingCart,
                        contentDescription = "Cart",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
                IconButton(onClick = {
                    isSheetOpen = true
                }) {
                    Icon(
                        Icons.Filled.AccountCircle,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            // Category List
            if (state is ProductListState.Success) {
                val categories = (state as ProductListState.Success).categories
                LazyRow(contentPadding = PaddingValues(8.dp)) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            onCategoryClick = { viewModel.toggleCategorySelection(it) }
                        )
                    }
                }
            }

            // Product Grid
            when (state) {
                is ProductListState.Loading -> {
                    LoadingScreen()
                }

                is ProductListState.Success -> {
                    val products = (state as ProductListState.Success).products
                    if (products.isEmpty()) {
                        EmptyScreen(
                            message = stringResource(R.string.empty_product_message),
                            subMessage = stringResource(R.string.empty_product_sub_message)
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(products) { product ->
                                ProductItem(product = product, onProductClick)
                            }
                        }
                    }
                }

                is ProductListState.Error -> {
                    ErrorScreen(
                        message = (state as ProductListState.Error).message,
                        onRetry = { viewModel.getProducts() }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    val dummyProducts = listOf(
        Product(
            1,
            "Product 1",
            19.99,
            "Description 1",
            "Category 1",
            "https://via.placeholder.com/150"
        ),
        Product(
            2,
            "Product 2",
            29.99,
            "Description 2",
            "Category 2",
            "https://via.placeholder.com/150"
        ),
        Product(
            3,
            "Product 3",
            39.99,
            "Description 3",
            "Category 1",
            "https://via.placeholder.com/150"
        )
    )

    val dummyCategories = listOf(
        Category("Category 1", true),
        Category("Category 2", false)
    )

    Column {
        LazyRow(contentPadding = PaddingValues(8.dp)) {
            items(dummyCategories) { category ->
                CategoryItem(category = category, onCategoryClick = {})
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dummyProducts) { product ->
                ProductItem(product = product, {})
            }
        }
    }
}