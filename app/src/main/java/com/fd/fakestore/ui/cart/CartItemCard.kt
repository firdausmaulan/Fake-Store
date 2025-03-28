package com.fd.fakestore.ui.cart

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fd.fakestore.data.model.CartWithProduct
import com.fd.fakestore.ui.common.NetworkImage

@Composable
fun CartItemCard(
    cartWithProduct: CartWithProduct,
    onQuantityChanged: (Int) -> Unit,
    onDelete: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    var isChecked by remember { mutableStateOf(false) }
    var quantity by remember { mutableIntStateOf(cartWithProduct.cart.quantity) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NetworkImage(
                imageUrl = cartWithProduct.product.image,
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = cartWithProduct.product.title, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Text(
                            text = "$${cartWithProduct.totalPrice}",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedCard(
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "-",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clickable {
                                        if (quantity > 1) {
                                            quantity--
                                            onQuantityChanged(quantity)
                                        } else {
                                            onDelete()
                                        }
                                    })
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = quantity.toString(),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "+",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clickable {
                                        quantity++
                                        onQuantityChanged(quantity)
                                    })
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }

            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = it
                    onCheckedChange(it)
                },
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}