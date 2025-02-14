package com.cettourdev.challenge.screens.favourites

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cettourdev.challenge.ui.theme.LightGray
import com.cettourdev.challenge.utils.LoadImage
import com.cettourdev.challenge.utils.Utils.getPriceWithCurrency
import com.google.gson.Gson

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavouritesScreen(context: Context, favouritesViewModel: FavouritesViewModel, navController: NavController) {
    val searchResults by favouritesViewModel.items.observeAsState(emptyList())
    val isLoading: Boolean by favouritesViewModel.isLoading.observeAsState(initial = false)
    val resultsNotEmpty: Boolean by favouritesViewModel.resultsNotEmpty.observeAsState(initial = true)

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightGray)
            ) {
                if (isLoading) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterHorizontally)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.DarkGray
                        )
                    }
                } else {
                    if (resultsNotEmpty) {
                        // para portrait se muestra un ítem por fila
                        // para landscape se muestran 2 ítems por fila
                        val configuration = LocalConfiguration.current
                        val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
                        val columns = if (isLandscape) 2 else 1

                        Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            Text(
                                modifier = Modifier.padding(top = 18.dp, bottom = 8.dp),
                                text = "Tus favoritos",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                            )
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(columns),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                        ) {
                            items(searchResults) { item ->
                                Card(
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clickable {
                                            val itemJson = Gson().toJson(item)
                                            val encodedJson = Base64.encodeToString(
                                                itemJson.toByteArray(Charsets.UTF_8),
                                                Base64.URL_SAFE or Base64.NO_WRAP,
                                            )
                                            navController.navigate("details/$encodedJson")
                                        },
                                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                                    shape = MaterialTheme.shapes.medium,
                                    border = BorderStroke(width = 1.dp, Color.LightGray),
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.White)
                                    ) {
                                        LoadImage(
                                            item.thumbnail.replace("http://", "https://"),
                                            Modifier
                                                .size(100.dp, 100.dp)
                                                .padding(vertical = 8.dp),
                                        )
                                        Column {
                                            Text(
                                                text = item.title.trim().replace("\\s+".toRegex(), " "),
                                                modifier = Modifier.padding(8.dp),
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 3,
                                                overflow = TextOverflow.Ellipsis,
                                                lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                                            )
                                            Row {
                                                Text(
                                                    text = getPriceWithCurrency(item, originalPrice = false),
                                                    fontSize = 16.sp,
                                                    modifier = Modifier.padding(start = 8.dp),
                                                )
                                                if (item.original_price > 0) {
                                                    Text(
                                                        text = getPriceWithCurrency(item, originalPrice = true),
                                                        fontSize = 14.sp,
                                                        modifier = Modifier.padding(start = 10.dp),
                                                        color = Color.Gray,
                                                        textDecoration = TextDecoration.LineThrough,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!resultsNotEmpty) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.padding(all = 12.dp),
                                text = "Tu lista de favoritos está vacía",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        },
    )
}
