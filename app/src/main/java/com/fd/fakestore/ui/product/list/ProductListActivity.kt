package com.fd.fakestore.ui.product.list

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fd.fakestore.helper.AppConstant
import com.fd.fakestore.ui.login.LoginScreen
import com.fd.fakestore.ui.product.detail.ProductDetailActivity
import com.fd.fakestore.ui.theme.FakeStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FakeStoreTheme {
                ProductListScreen(
                    onProductClick = { product ->
                        val intent = Intent(this, ProductDetailActivity::class.java)
                        intent.putExtra(AppConstant.KEY_PRODUCT_ID, product.id)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}