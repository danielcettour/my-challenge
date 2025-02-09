package com.cettourdev.challenge.favourites

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FavouritesScreen(context: Context) {
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Text("Favoritos")
    }
}
