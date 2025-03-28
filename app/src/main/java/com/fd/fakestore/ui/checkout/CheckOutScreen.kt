package com.fd.fakestore.ui.checkout

import ErrorScreen
import LoadingScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fd.fakestore.R
import com.fd.fakestore.helper.AppColor
import com.fd.fakestore.helper.UiHelper
import com.fd.fakestore.ui.common.SuccessBottomSheetDialog
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    onSuccessfulOrder: () -> Unit,
    cartIds: List<Int>,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val checkoutState by viewModel.state.collectAsState()
    var isOrderSuccessful by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getCheckoutDetails(cartIds)
    }

    if (isOrderSuccessful) {
        SuccessBottomSheetDialog(
            onDismissRequest = {
                viewModel.placeOrder((checkoutState as CheckoutState.Success).cartItems)
                onSuccessfulOrder()
            },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.label_check_out), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = UiHelper.topAppBarColors()
            )
        },
        bottomBar = {
            if (checkoutState is CheckoutState.Success) {
                CheckoutFooter(
                    totalPrice = (checkoutState as CheckoutState.Success).totalPrice,
                    onOrderClick = {
                        isOrderSuccessful = true
                    }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppColor.background())
        ) {
            when (checkoutState) {
                is CheckoutState.Loading -> {
                    LoadingScreen()
                }

                is CheckoutState.Success -> {
                    val successState = checkoutState as CheckoutState.Success
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp)
                    ) {
                        item {
                            UserInformation(successState.user)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        items(successState.cartItems) { cartItem ->
                            CheckoutItemCard(cartItem = cartItem)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                is CheckoutState.Error -> {
                    ErrorScreen(message = (checkoutState as CheckoutState.Error).message)
                }
            }
        }
    }
}

@Composable
fun CheckoutFooter(totalPrice: Double, onOrderClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onOrderClick,
                elevation = UiHelper.buttonElevation(),
                modifier = Modifier.defaultMinSize(minWidth = 150.dp)
            ) {
                Text(stringResource(R.string.label_order))
            }
            Text(
                "Total: $${String.format(Locale.US, "%.2f", totalPrice)}",
                fontWeight = FontWeight.Bold
            )
        }
    }
}