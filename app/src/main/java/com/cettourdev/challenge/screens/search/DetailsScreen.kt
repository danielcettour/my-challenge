package com.cettourdev.challenge.screens.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cettourdev.challenge.model.ItemResponse
import com.cettourdev.challenge.ui.theme.LightGray
import com.cettourdev.challenge.ui.theme.YellowPrimary
import com.cettourdev.challenge.utils.LoadImage
import com.cettourdev.challenge.utils.Utils.getPriceWithCurrency
import com.google.gson.Gson

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScreen(
    itemJson: String,
    context: Context,
) {
    val decodedJson =
        String(Base64.decode(itemJson, Base64.URL_SAFE or Base64.NO_WRAP), Charsets.UTF_8)
    val item = Gson().fromJson(decodedJson, ItemResponse::class.java)

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(containerColor = YellowPrimary, onClick = {
                val uri = Uri.parse(item.permalink)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }) {
                Icon(imageVector = Icons.Outlined.Info, contentDescription = "Más detalles")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) {
        Column {
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
                    .padding(12.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                LoadImage(
                    model = item.thumbnail.replace("http://", "https://"),
                    modifier = Modifier
                        .size(150.dp)
                        .clickable(enabled = false) {},
                )
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
                    .verticalScroll(rememberScrollState()),
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
                                    text = "${attribute.name}",
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
