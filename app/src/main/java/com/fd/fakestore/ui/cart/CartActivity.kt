package com.fd.fakestore.ui.cart

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.fd.fakestore.helper.AppConstant
import com.fd.fakestore.ui.checkout.CheckOutActivity
import com.fd.fakestore.ui.login.LoginScreen
import com.fd.fakestore.ui.theme.FakeStoreTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : ComponentActivity() {

    private lateinit var checkoutResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkoutResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Checkout was successful, recreate the activity
                recreate()
            }
        }

        setContent {
            FakeStoreTheme {
                CartScreen(
                    onBack = { finish() },
                    onCheckout = { selectedCartIds ->
                        val intent = Intent(this, CheckOutActivity::class.java)
                        intent.putIntegerArrayListExtra(AppConstant.KEY_SELECTED_CART_IDS, ArrayList(selectedCartIds))
                        checkoutResultLauncher.launch(intent)
                    },
                )
            }
        }
    }
}