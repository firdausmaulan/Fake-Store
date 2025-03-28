package com.fd.fakestore.helper

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColor {

    @Composable
    fun background(): Brush {
        return Brush.verticalGradient(
            colors = listOf(MaterialTheme.colorScheme.primary, Color.White)
        )
    }
    @Composable
    fun backgroundReverse(): Brush {
        return Brush.verticalGradient(
            colors = listOf(Color.White, MaterialTheme.colorScheme.secondary)
        )
    }

    @Composable
    fun buttonColor() : ButtonColors {
        return ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Black
        )
    }

}