package com.fd.fakestore.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.fd.fakestore.R

@Composable
fun NetworkImage(
    imageUrl: String,
    contentDescription: String? = "",
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    alignment: Alignment = Alignment.Center,
    colorFilter: ColorFilter? = null,
    alpha: Float = 1f,
    filterQuality: FilterQuality = FilterQuality.Low,
) {
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        alignment = alignment,
        colorFilter = colorFilter,
        alpha = alpha,
        filterQuality = filterQuality,
        placeholder = painterResource(R.drawable.ic_store),
        error = painterResource(R.drawable.ic_store)
    )
}