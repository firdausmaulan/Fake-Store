package com.fd.fakestore.ui.product.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fd.fakestore.data.model.Category

@Composable
fun CategoryItem(category: Category, onCategoryClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable { onCategoryClick(category.name ?: "") },
        colors = CardDefaults.cardColors(if (category.selected) MaterialTheme.colorScheme.secondary else Color.White)
    ) {
        Text(
            text = category.name ?: "Unknown",
            color = Color.Black,
            modifier = Modifier.padding(8.dp)
        )
    }
}