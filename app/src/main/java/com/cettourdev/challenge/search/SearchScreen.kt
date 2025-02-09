package com.cettourdev.challenge.search

import android.annotation.SuppressLint
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.cettourdev.challenge.R
import com.cettourdev.challenge.ui.theme.Orange
import com.cettourdev.challenge.utils.NetworkManager
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SearchScreen(context: Context) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val searchResults = remember { mutableStateListOf<String>() } // Placeholder for search results
    searchResults.addAll(listOf("1", "2"))

    val snackbarHostState = remember { SnackbarHostState() } // Snackbar state
    val coroutineScope = rememberCoroutineScope() // Coroutine scope
    val focusManager = LocalFocusManager.current

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
        }, // SnackbarHost to show messages        containerColor = LightGray,
        content = {
            Column(modifier = Modifier.padding(all = 10.dp).fillMaxSize()) {
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions =
                        KeyboardActions(
                            onSearch = {
                                hideKeyboard(context, focusManager)
                                val isConnected = NetworkManager.isConnected(context)

                                coroutineScope.launch {
                                    if (isConnected) {
                                        snackbarHostState.showSnackbar("Searching...", duration = SnackbarDuration.Short)
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
                            focusedBorderColor = Color.DarkGray,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.Gray,
                        ),
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Buscar productos y más...", color = Color.Gray) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large,
                    trailingIcon = {
                        IconButton(onClick = {
                            val isConnected = NetworkManager.isConnected(context)
                            hideKeyboard(context, focusManager)

                            coroutineScope.launch {
                                if (isConnected) {
                                    snackbarHostState.showSnackbar("Searching...", duration = SnackbarDuration.Short)
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
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Scaffold {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                        /*items(searchResults) { result ->
                            Text(result, modifier = Modifier.padding(8.dp))
                        }*/
                }
                // }
            }
        },
    )
}

// Function to hide the keyboard
fun hideKeyboard(
    context: Context,
    focusManager: FocusManager,
) {
    focusManager.clearFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(null, 0)
}
