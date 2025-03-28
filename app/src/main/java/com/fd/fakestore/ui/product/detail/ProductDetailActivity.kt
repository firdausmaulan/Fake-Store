package com.fd.fakestore.ui.product.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fd.fakestore.helper.AppConstant
import com.fd.fakestore.ui.theme.FakeStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FakeStoreTheme {
                ProductDetailScreen(
                    productId = intent.getIntExtra(AppConstant.KEY_PRODUCT_ID, 0),
                    onClose = {
                        finish()
                    },
                    onCartClick = {
                        // Handle cart click
                    }
                )
            }
        }
    }
}