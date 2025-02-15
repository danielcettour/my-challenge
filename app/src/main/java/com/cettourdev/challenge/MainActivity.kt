package com.cettourdev.challenge

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DrawerState
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cettourdev.challenge.screens.about.AboutScreen
import com.cettourdev.challenge.screens.details.WebViewScreen
import com.cettourdev.challenge.screens.favourites.FavouritesScreen
import com.cettourdev.challenge.screens.details.DetailsScreen
import com.cettourdev.challenge.screens.details.DetailsViewModel
import com.cettourdev.challenge.screens.favourites.FavouritesViewModel
import com.cettourdev.challenge.screens.search.SearchScreen
import com.cettourdev.challenge.screens.search.SearchViewModel
import com.cettourdev.challenge.ui.theme.ChallengeTheme
import com.cettourdev.challenge.ui.theme.LightYellow
import com.cettourdev.challenge.ui.theme.YellowPrimary
import com.cettourdev.challenge.ui.theme.customColorScheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val searchViewModel: SearchViewModel by viewModels()
    private val detailsViewModel: DetailsViewModel by viewModels()
    private val favouritesViewModel: FavouritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChallengeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(innerPadding, searchViewModel, detailsViewModel, favouritesViewModel)
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    innerPadding: PaddingValues,
    searchViewModel: SearchViewModel,
    detailsViewModel: DetailsViewModel,
    favouritesViewModel: FavouritesViewModel
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val showDialog: Boolean by searchViewModel.dialogVisible.observeAsState(initial = false)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current

    // en la webview de detalles, se deshabilitan los gestos del menú para que no interfieran con el scroll
    val gesturesEnabled = remember { mutableStateOf(true) }
    gesturesEnabled.value = currentRoute != "webview/{permalink}"

    MaterialTheme(colorScheme = customColorScheme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = gesturesEnabled.value,
            drawerContent = {
                ModalDrawerSheet(modifier = Modifier.width(240.dp)) {
                    Box(
                        modifier = Modifier
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
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = LightYellow,
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black,
                        ),
                        icon = {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = "Buscar")
                        },
                        label = { Text(stringResource(R.string.buscar)) },
                        selected = currentRoute == "search",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate("search")
                        },
                    )
                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = LightYellow,
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black,
                        ),
                        icon = {
                            Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Favoritos")
                        },
                        label = { Text(stringResource(R.string.favoritos)) },
                        selected = currentRoute == "favourites",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate("favourites")
                        },
                    )
                    NavigationDrawerItem(
                        colors = NavigationDrawerItemDefaults.colors(
                            selectedContainerColor = LightYellow,
                            selectedTextColor = Color.Black,
                            selectedIconColor = Color.Black,
                        ),
                        icon = {
                            Icon(imageVector = Icons.Filled.Info, contentDescription = "Acerca de")
                        },
                        label = { Text("Acerca de") },
                        selected = currentRoute == "about",
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navController.navigate("about")
                        },
                    )
                }
            },
        ) {
            Scaffold(
                topBar = {
                    MyTopBar(
                        navController,
                        coroutineScope,
                        drawerState,
                        onVisibilityChanged = { visible -> searchViewModel.setearDialogVisibility(visible) }
                    )
                },
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "search",
                    ) {
                        composable("search") {
                            SearchScreen(context = context, searchViewModel = searchViewModel, navController = navController)
                        }
                        composable("favourites") {
                            FavouritesScreen(context = context, favouritesViewModel = favouritesViewModel, navController = navController)
                        }
                        composable(
                            "details/{itemJson}",
                            arguments = listOf(navArgument("itemJson") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            backStackEntry.arguments?.getString("itemJson")?.let { itemJson ->
                                DetailsScreen(itemJson, detailsViewModel, navController)
                            }
                        }
                        composable(
                            "webview/{permalink}",
                            arguments = listOf(navArgument("permalink") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val permalink = backStackEntry.arguments?.getString("permalink")?.let { Uri.decode(it) } ?: ""
                            WebViewScreen(permalink)
                        }
                        composable("about") {
                            AboutScreen()
                        }
                    }
                }
                AlertDialogAyuda(
                    showDialog,
                    onVisibilityChanged = { visible ->
                        searchViewModel.setearDialogVisibility(visible)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopBar(
    navController: NavController,
    coroutineScope: CoroutineScope?,
    drawerState: DrawerState?,
    onVisibilityChanged: (Boolean) -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    TopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.meli_logo),
                    contentDescription = "Logo empresa",
                    modifier = Modifier
                        .size(50.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = YellowPrimary, titleContentColor = Color.Black),
        navigationIcon = {
            if (currentRoute == "webview/{permalink}" || currentRoute == "details/{itemJson}") {
                // Se muestra la flecha hacia atrás en pantalla de detalle y webview
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            } else {
                // Se muestra el ícono de menú hamburguesa en el resto de pantallas
                IconButton(onClick = { coroutineScope?.launch { drawerState?.open() } }) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                }
            }
        },
        actions = {
            IconButton(
                onClick = { onVisibilityChanged(true) },
                modifier = Modifier.testTag("iconoAyudaTag")
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "Ayuda")
            }
        },
    )
}

@Composable
fun AlertDialogAyuda(showDialog: Boolean, onVisibilityChanged: (Boolean) -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onVisibilityChanged(false) },
            confirmButton = {
                TextButton(onClick = { onVisibilityChanged(false) }, modifier = Modifier.testTag("aceptarButtonTag")) {
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
            modifier = Modifier.testTag("dialogAyudaTag")
        )
    }
}
