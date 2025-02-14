package com.cettourdev.challenge.screens.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(context: Context, searchViewModel: SearchViewModel, navController: NavController) {
    val searchQuery by searchViewModel.query.observeAsState("")
    val searchResults by searchViewModel.items.observeAsState(emptyList())

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val isLoading: Boolean by searchViewModel.isLoading.observeAsState(initial = false)
    val resultsNotEmpty: Boolean by searchViewModel.resultsNotEmpty.observeAsState(initial = true)
    val resultError: Boolean by searchViewModel.resultsError.observeAsState(initial = false)

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                val backgroundColor =
                    when (data.visuals.message) {
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
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(LightGray)
            ) {
                BusquedaOutlinedTextField(
                    searchQuery,
                    onQueryChanged = { q -> searchViewModel.setearQuery(q) },
                    onSearch = { searchViewModel.onSearch() },
                    snackbarHostState
                )
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
                    if (resultError) {
                        showSnackbarError(context, coroutineScope, snackbarHostState)
                    }
                }
            }
        },
    )
}

fun showSnackbarError(context: Context, coroutineScope: CoroutineScope, snackbarHostState: SnackbarHostState) {
    coroutineScope.launch {
        snackbarHostState.showSnackbar(
            context.getString(R.string.error_inesperado),
            duration = SnackbarDuration.Long,
        )
    }
}

@Composable
fun BusquedaOutlinedTextField(
    searchQuery: String,
    onQueryChanged: (String) -> Unit,
    onSearch: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    var isError by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        // al inicio, desplegar teclado para permitir escribir
        if (searchQuery.isEmpty()) {
            delay(300) // Delay para asegurar el dibujado de la pantalla
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    OutlinedTextField(
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions =
        KeyboardActions(
            onSearch = {
                if (searchQuery.isBlank()) {
                    isError = true
                    return@KeyboardActions
                }
                isError = false
                hideKeyboard(context, focusManager)
                val isConnected = NetworkManager.isConnected(context)

                coroutineScope.launch {
                    if (isConnected) {
                        onSearch()
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
        value = searchQuery,
        onValueChange = { q -> onQueryChanged(q) },
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
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            .focusRequester(focusRequester)
            .testTag("campoBusquedaTag"),
        singleLine = true,
        isError = isError,
        shape = MaterialTheme.shapes.large,
        trailingIcon = {
            IconButton(onClick = {
                if (searchQuery.isBlank()) {
                    isError = true
                    return@IconButton
                }
                isError = false
                val isConnected = NetworkManager.isConnected(context)
                hideKeyboard(context, focusManager)

                coroutineScope.launch {
                    if (isConnected) {
                        onSearch()
                    } else {
                        snackbarHostState.showSnackbar(
                            context.getString(R.string.revisa_tu_conexion_a_internet),
                            duration = SnackbarDuration.Short,
                        )
                    }
                }
            }, modifier = Modifier.testTag("performSearchIcon")) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            }
        },
        supportingText = {
            if (isError) {
                Text(
                    text = "Dato requerido",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.testTag("supportingTextTag")
                )
            }
        },
    )
}
