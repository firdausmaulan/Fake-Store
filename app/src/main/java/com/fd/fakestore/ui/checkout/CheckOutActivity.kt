package com.fd.fakestore.ui.checkout

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
import com.fd.fakestore.ui.theme.FakeStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckOutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FakeStoreTheme {
                CheckoutScreen(
                    onBack = {
                        finish()
                    },
                    onSuccessfulOrder = {
                        setResult(RESULT_OK)
                        finish()
                    },
                    cartIds = intent.getIntegerArrayListExtra(AppConstant.KEY_SELECTED_CART_IDS) ?: emptyList()
                )
            }
        }
    }
}