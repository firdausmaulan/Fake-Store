package com.fd.fakestore.ui.product.detail

import ErrorScreen
import LoadingScreen
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fd.fakestore.R
import com.fd.fakestore.data.model.Product
import com.fd.fakestore.helper.UiHelper
import com.fd.fakestore.ui.common.NetworkImage
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(
    productId: Int,
    viewModel: ProductDetailViewModel,
    onClose: () -> Unit,
    onCartClick: () -> Unit
) {

    LaunchedEffect(Unit) {
        println("ProductDetailScreen LaunchedEffect")
        viewModel.isProductInCart(productId)
        viewModel.getProductDetail(productId)
    }

    when (val state = viewModel.state.collectAsState().value) {
        is ProductDetailState.Loading -> {
            LoadingScreen()
        }

        is ProductDetailState.Success -> {
            ProductDetailContent(
                viewModel,
                state.product,
                onClose,
                onCartClick
            )
        }

        is ProductDetailState.Error -> {
            ErrorScreen(
                message = state.message,
                onRetry = { viewModel.getProductDetail(productId) }
            )
        }
    }
}

@Composable
fun ProductDetailContent(
    viewModel: ProductDetailViewModel,
    product: Product,
    onClose: () -> Unit,
    onCartClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                NetworkImage(
                    imageUrl = product.image,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.FillHeight
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = product.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray
                    )
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    ) {
                        Text(
                            text = "$${product.price}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = product.description,
                    fontSize = 16.sp
                )
            }
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color.LightGray.copy(alpha = 0.5f), CircleShape)
                .clip(CircleShape)
                .size(36.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close")
        }

        IconButton(
            onClick = onCartClick,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .background(Color.LightGray.copy(alpha = 0.5f), CircleShape)
                .clip(CircleShape)
                .size(36.dp)
        ) {
            Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
        }

        if (!viewModel.isProductInCart.collectAsState().value) {
            Button(
                onClick = {
                    scope.launch {
                        viewModel.addToCart(product)
                        Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show()
                        onCartClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                elevation = UiHelper.buttonElevation()
            ) {
                Text(text = stringResource(R.string.label_add_to_cart))
            }
        }
    }
}