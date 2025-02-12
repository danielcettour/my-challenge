package com.cettourdev.challenge.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cettourdev.challenge.R
import com.cettourdev.challenge.ui.theme.LightGray
import com.cettourdev.challenge.ui.theme.Orange
import com.cettourdev.challenge.utils.LoadImage
import com.cettourdev.challenge.utils.NetworkManager
import com.cettourdev.challenge.utils.Utils.getPriceWithCurrency
import com.cettourdev.challenge.utils.Utils.hideKeyboard
import com.google.gson.Gson
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    context: Context,
    searchViewModel: SearchViewModel,
    navController: NavController,
) {
    // var searchQuery by rememberSaveable { mutableStateOf("") }
    // val searchQuery by searchViewModel.query.observeAsState("")
    var textState by rememberSaveable { mutableStateOf("") }
    var isError by rememberSaveable { mutableStateOf(false) }
    val searchQuery by searchViewModel.query.observeAsState("")
    val searchResults by searchViewModel.items.observeAsState(emptyList())

    val snackbarHostState = remember { SnackbarHostState() } // Snackbar state
    val coroutineScope = rememberCoroutineScope() // Coroutine scope
    val focusManager = LocalFocusManager.current

    val isLoading: Boolean by searchViewModel.isLoading.observeAsState(initial = false)
    val resultsNotmpty: Boolean by searchViewModel.resultsNotmpty.observeAsState(initial = true)

    LaunchedEffect(searchQuery) {
        textState = searchQuery
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                val backgroundColor =
                    when (data.visuals.message) {
                        context.getString(R.string.revisa_tu_conexion_a_internet) -> Orange
                        "Searching..." -> Color.Blue
                        "Error occurred" -> Color.Red
                        else -> Color.DarkGray
                    }
                Snackbar(
                    snackbarData = data,
                    containerColor = backgroundColor,
                    contentColor = Color.White,
                )
            }
        },
        content = {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(LightGray)) {
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions =
                    KeyboardActions(
                        onSearch = {
                            if (textState.isBlank()) {
                                isError = true
                                return@KeyboardActions
                            }
                            isError = false
                            hideKeyboard(context, focusManager)
                            val isConnected = NetworkManager.isConnected(context)

                            coroutineScope.launch {
                                if (isConnected) {
                                    searchViewModel.onSearch()
                                } else {
                                    snackbarHostState.showSnackbar(
                                        context.getString(R.string.revisa_tu_conexion_a_internet),
                                        duration = SnackbarDuration.Short,
                                    )
                                }
                            }
                        },
                    ),
                    colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedLabelColor = Color.DarkGray,
                        unfocusedLabelColor = Color.Gray,
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.Gray,
                    ),
                    value = textState,
                    onValueChange = {
                        textState = it
                        searchViewModel.onQueryChanged(it)
                    },
                    label = {
                        Text(
                            stringResource(
                                R.string.buscar_productos_y_mas,
                            ),
                            color = Color.Gray,
                            modifier = Modifier.background(Color.Transparent),
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp),
                    singleLine = true,
                    isError = isError,
                    shape = MaterialTheme.shapes.large,
                    trailingIcon = {
                        IconButton(onClick = {
                            if (textState.isBlank()) {
                                isError = true
                                return@IconButton
                            }
                            isError = false
                            val isConnected = NetworkManager.isConnected(context)
                            hideKeyboard(context, focusManager)

                            coroutineScope.launch {
                                if (isConnected) {
                                    searchViewModel.onSearch()
                                } else {
                                    snackbarHostState.showSnackbar(
                                        context.getString(R.string.revisa_tu_conexion_a_internet),
                                        duration = SnackbarDuration.Short,
                                    )
                                }
                            }
                        }) {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                        }
                    },
                    supportingText = {
                        if (isError) {
                            Text(
                                text = "Campo requerido",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    },
                )

                if (isLoading) {
                    Box(Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.DarkGray
                        )
                    }
                } else {
                    if (resultsNotmpty) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(searchResults) { item ->
                                Card(
                                    modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 12.dp,
                                            end = 12.dp,
                                            top = 6.dp,
                                            bottom = 6.dp,
                                        )
                                        .clickable {
                                            val itemJson = Gson().toJson(item)
                                            val encodedJson =
                                                Base64.encodeToString(
                                                    itemJson.toByteArray(Charsets.UTF_8),
                                                    Base64.URL_SAFE or Base64.NO_WRAP,
                                                )
                                            navController.navigate("details/$encodedJson")
                                        },
                                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                                    shape = MaterialTheme.shapes.medium,
                                    border = BorderStroke(width = 1.dp, Color.LightGray),
                                ) {
                                    Row(modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.White)) {
                                        LoadImage(
                                            item.thumbnail.replace("http://", "https://"),
                                            Modifier
                                                .size(100.dp, 100.dp)
                                                .padding(top = 8.dp, bottom = 8.dp),
                                        )
                                        Column {
                                            Text(
                                                text = item.title.trim().replace(
                                                    "\\s+".toRegex(),
                                                    " "
                                                ), // algunos nombres vienen con múltiples espacios
                                                modifier = Modifier.padding(8.dp),
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 3,
                                                overflow = TextOverflow.Ellipsis,
                                                lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                                            )
                                            Row(modifier = Modifier) {
                                                Text(
                                                    text =
                                                    getPriceWithCurrency(
                                                        item,
                                                        originalPrice = false,
                                                    ),
                                                    fontSize = 16.sp,
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
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Spacer(modifier = Modifier.height(40.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.not_found),
                                    contentDescription = "No Results Found",
                                    modifier = Modifier
                                        .size(150.dp)
                                        .align(Alignment.CenterHorizontally),
                                )
                                Spacer(modifier = Modifier.height(40.dp))
                                Text(
                                    text = "No hay publicaciones que coincidan con tu búsqueda",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center,
                                )
                                Spacer(modifier = Modifier.height(40.dp))
                                Text(
                                    text = "-  Revisá la ortografía de la palabra",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray,
                                    fontSize = 16.sp,
                                )
                                Text(
                                    text = "-  Utilizá palabras más genéricas o menos palabras.",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray,
                                    fontSize = 16.sp,
                                )
                            }
                        }
                    }
                }
            }
        },
    )
}
