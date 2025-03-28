package com.fd.fakestore.ui.cart

import EmptyScreen
import ErrorScreen
import LoadingScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.fd.fakestore.R
import com.fd.fakestore.helper.AppColor
import com.fd.fakestore.helper.UiHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(onBack: () -> Unit, onCheckout: (List<Int>) -> Unit) {
    val viewModel: CartViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val stateReadyForSubmit by viewModel.stateReadyForSubmit.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCartItems()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.label_cart), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = UiHelper.topAppBarColors()
            )
        },
        bottomBar = {
            if (stateReadyForSubmit) {
                Button(
                    onClick = {
                        onCheckout(viewModel.selectedCartItemIds.value)
                    },
                    elevation = UiHelper.buttonElevation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Checkout")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.background())
        ) {
            when (state) {
                is CartState.Loading -> {
                    LoadingScreen()
                }

                is CartState.Success -> {
                    val cartItems = (state as CartState.Success).cartItems
                    if (cartItems.isEmpty()) {
                        EmptyScreen(
                            message = stringResource(R.string.empty_cart_message),
                            subMessage = stringResource(R.string.empty_cart_sub_message)
                        )
                    } else {
                        LazyColumn(contentPadding = paddingValues) {
                            items(cartItems, key = { it.cart.cartId }) { cartWithProduct ->
                                CartItemCard(
                                    cartWithProduct = cartWithProduct,
                                    onQuantityChanged = { newQuantity ->
                                        viewModel.updateCartItem(cartWithProduct, newQuantity)
                                    },
                                    onDelete = {
                                        viewModel.deleteCartItem(cartWithProduct)
                                    },
                                    onCheckedChange = {
                                        viewModel.toggleItemSelected(cartWithProduct)
                                    }
                                )
                            }
                        }
                    }
                }

                is CartState.Empty -> {
                    EmptyScreen(
                        message = stringResource(R.string.empty_cart_message),
                        subMessage = stringResource(R.string.empty_cart_sub_message)
                    )
                }

                is CartState.Error -> {
                    ErrorScreen(message = (state as CartState.Error).message)
                }
            }
        }
    }
}