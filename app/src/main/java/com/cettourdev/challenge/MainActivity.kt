package com.cettourdev.challenge

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cettourdev.challenge.favourites.FavouritesScreen
import com.cettourdev.challenge.search.ui.SearchScreen
import com.cettourdev.challenge.search.ui.SearchViewModel
import com.cettourdev.challenge.ui.theme.ChallengeTheme
import com.cettourdev.challenge.ui.theme.LightYellow
import com.cettourdev.challenge.ui.theme.YellowPrimary
import com.cettourdev.challenge.ui.theme.customColorScheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val searchViewModel: SearchViewModel =
                ViewModelProvider(
                    this,
                    SavedStateViewModelFactory(application, this),
                )[SearchViewModel::class.java]
            ChallengeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(innerPadding, searchViewModel)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    innerPadding: PaddingValues,
    searchViewModel: SearchViewModel,
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    MaterialTheme(colorScheme = customColorScheme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.width(240.dp)) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(30.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            tint = Color.Gray,
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = stringResource(R.string.foto_de_perfil),
                            modifier = Modifier.size(64.dp),
                        )
                    }
                    NavigationDrawerItem(
                        colors =
                            NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = LightYellow,
                                selectedTextColor = Color.Black,
                                selectedIconColor = Color.Black,
                            ),
                        icon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "Buscar") },
                        label = { Text(stringResource(R.string.buscar)) },
                        selected = currentRoute == "search",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate("search")
                        },
                    )
                    NavigationDrawerItem(
                        colors =
                            NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = LightYellow,
                                selectedTextColor = Color.Black,
                                selectedIconColor = Color.Black,
                            ),
                        icon = { Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Favoritos") },
                        label = { Text(stringResource(R.string.favoritos)) },
                        selected = currentRoute == "favourites",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate("favourites")
                        },
                    )
                }
            },
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("", fontSize = MaterialTheme.typography.titleLarge.fontSize) },
                        colors =
                            TopAppBarDefaults.topAppBarColors(
                                containerColor = YellowPrimary,
                                titleContentColor = Color.Black,
                            ),
                        navigationIcon = {
                            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            IconButton(onClick = { showDialog = true }) {
                                Icon(imageVector = Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "Ayuda")
                            }
                        },
                    )
                },
            ) {
                Surface(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(it),
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "search",
                    ) {
                        composable("search") { SearchScreen(context = context, searchViewModel, navController) }
                        composable("favourites") { FavouritesScreen(context = context) }
                    }
                }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        confirmButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Aceptar", color = Color.DarkGray, fontSize = 16.sp)
                            }
                        },
                        title = { Text("¿Cómo funciona?") },
                        text = {
                            Text(
                                "Buscá los artículos desde la barra de búsqueda. Pulsá sobre un ítem para ver más detalles. Podés agregar tus productos a favoritos!",
                                fontSize = 16.sp,
                            )
                        },
                    )
                }
            }
        }
    }
}
