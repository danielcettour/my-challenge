package com.cettourdev.challenge.screens.details

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Base64
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cettourdev.challenge.R
import com.cettourdev.challenge.model.ItemResponse
import com.cettourdev.challenge.ui.theme.FavouriteBlue
import com.cettourdev.challenge.ui.theme.LightGray
import com.cettourdev.challenge.ui.theme.Orange
import com.cettourdev.challenge.ui.theme.SnackbarSuccess
import com.cettourdev.challenge.ui.theme.YellowPrimary
import com.cettourdev.challenge.utils.LoadImage
import com.cettourdev.challenge.utils.NetworkManager
import com.cettourdev.challenge.utils.Utils.getPriceWithCurrency
import com.google.gson.Gson
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScreen(
    itemJson: String,
    detailsViewModel: DetailsViewModel,
    navController: NavController,
) {
    val decodedJson =
        String(Base64.decode(itemJson, Base64.URL_SAFE or Base64.NO_WRAP), Charsets.UTF_8)
    val item = Gson().fromJson(decodedJson, ItemResponse::class.java)
    detailsViewModel.isItemFavourite(item)

    val isLoading: Boolean by detailsViewModel.isLoading.observeAsState(initial = false)
    val items by detailsViewModel.items.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                val backgroundColor =
                    when (data.visuals.message) {
                        context.getString(R.string.se_agrego_a_tu_lista_de_favoritos) -> SnackbarSuccess
                        context.getString(R.string.revisa_tu_conexion_a_internet) -> Orange
                        context.getString(R.string.error_inesperado) -> Color.Red
                        else -> Color.DarkGray
                    }
                Snackbar(
                    snackbarData = data,
                    containerColor = backgroundColor,
                    contentColor = Color.White,
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = YellowPrimary,
                onClick = {
                    val permalink = Uri.encode(item.permalink)
                    val isConnected = NetworkManager.isConnected(context)
                    coroutineScope.launch {
                        if (isConnected) {
                            navController.navigate("webview/$permalink")
                        } else {
                            snackbarHostState.showSnackbar(
                                context.getString(R.string.revisa_tu_conexion_a_internet),
                                duration = SnackbarDuration.Short,
                            )
                        }
                    }

                }) {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = "Más detalles")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            if (items == null || isLoading) {
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
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(
                        modifier = Modifier.padding(all = 12.dp),
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    LoadImage(
                        model = item.thumbnail.replace("http://", "https://"),
                        modifier = Modifier
                            .size(150.dp)
                            .clickable(enabled = false) {},
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 18.dp)
                    ) {
                        FavouriteIcon(item, detailsViewModel, snackbarHostState)
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text =
                        getPriceWithCurrency(
                            item,
                            originalPrice = false,
                        ),
                        fontSize = 20.sp,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                    if (item.original_price > 0) {
                        Text(
                            text =
                            getPriceWithCurrency(
                                item,
                                originalPrice = true,
                            ),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(start = 10.dp),
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough,
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    if (item.attributes.isNotEmpty()) {
                        Column {
                            item.attributes.forEachIndexed { index, attribute ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(if (index % 2 == 0) LightGray else Color.Transparent),
                                    verticalAlignment = Alignment.CenterVertically

                                ) {
                                    Text(
                                        text = attribute.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(4.dp),
                                        lineHeight = MaterialTheme.typography.bodySmall.lineHeight,

                                        )
                                    Text(
                                        text = if (attribute.value_name.isNullOrEmpty()) " N/A" else " ${attribute.value_name}".trim(),
                                        fontSize = 14.sp,
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(4.dp),
                                        lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavouriteIcon(
    item: ItemResponse,
    detailsViewModel: DetailsViewModel,
    snackbarHostState: SnackbarHostState
) {
    val isFavourite by detailsViewModel.isFavourite.observeAsState(false)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Icon(
        imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
        contentDescription = "Favorite",
        tint = if (isFavourite) FavouriteBlue else Color.Gray,
        modifier = Modifier
            .size(30.dp)
            .clickable {
                detailsViewModel.toggleFavourite(item)
                coroutineScope.launch {
                    val message = if (isFavourite) {
                        context.getString(R.string.se_agrego_a_tu_lista_de_favoritos)
                    } else {
                        context.getString(R.string.se_elimino_de_tu_lista_de_favoritos)
                    }
                    snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
                }
            }
    )
}

