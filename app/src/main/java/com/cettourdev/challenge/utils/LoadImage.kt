package com.cettourdev.challenge.utils

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun LoadImage(
    model: String,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = model,
        loading = {
            CircularProgressIndicator(modifier = Modifier.requiredSize(40.dp))
        },
        contentDescription = null,
        contentScale = ContentScale.Fit,
    )
}
