package com.fd.fakestore.ui.product.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.fd.fakestore.helper.AppConstant
import com.fd.fakestore.ui.cart.CartActivity
import com.fd.fakestore.ui.theme.FakeStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailActivity : ComponentActivity() {

    private val viewModel : ProductDetailViewModel by viewModels()
    private var productId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productId = intent.getIntExtra(AppConstant.KEY_PRODUCT_ID, 0)
        setContent {
            FakeStoreTheme {
                ProductDetailScreen(
                    productId = productId,
                    viewModel = viewModel,
                    onClose = {
                        finish()
                    },
                    onCartClick = {
                        startActivity(Intent(this, CartActivity::class.java))
                    }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.isProductInCart(productId)
    }
}