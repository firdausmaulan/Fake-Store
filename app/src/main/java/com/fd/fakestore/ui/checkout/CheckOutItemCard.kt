package com.fd.fakestore.ui.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.fd.fakestore.R
import com.fd.fakestore.data.model.CartWithProduct

@Composable
fun CheckoutItemCard(cartItem: CartWithProduct) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = cartItem.product.image),
                contentDescription = "Product Image",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(cartItem.product.title, fontWeight = FontWeight.Bold)
                Text("${stringResource(R.string.label_quantity)}: ${cartItem.cart.quantity}")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${stringResource(R.string.label_price)}: $${cartItem.product.price}")
                    Text(
                        "$${cartItem.cart.quantity * cartItem.product.price}",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}