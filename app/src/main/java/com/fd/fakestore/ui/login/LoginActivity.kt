package com.fd.fakestore.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.fd.fakestore.ui.product.list.ProductListActivity
import com.fd.fakestore.ui.theme.FakeStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FakeStoreTheme {
                LoginScreen(
                    onAuthenticated = {
                        startActivity(Intent(this, ProductListActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}