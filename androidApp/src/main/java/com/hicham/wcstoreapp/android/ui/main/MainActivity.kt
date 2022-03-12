package com.hicham.wcstoreapp.android.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ui.BottomNavigation
import com.hicham.wcstoreapp.android.ui.navigation.AndroidNavigationManager
import com.hicham.wcstoreapp.android.ui.navigation.MainNavGraph
import com.hicham.wcstoreapp.android.ui.navigation.Screen
import com.hicham.wcstoreapp.android.ui.theme.WCStoreAppTheme
import compose.icons.TablerIcons
import compose.icons.tablericons.ShoppingCart
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val navigationManager: AndroidNavigationManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WCStoreAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val viewModel by viewModel<MainViewModel>()
                    val uiState by viewModel.uiState.collectAsState()
                    Main(uiState, navigationManager)
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Main(uiState: MainViewModel.UiState, navigationManager: AndroidNavigationManager) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // TODO check if the performance impact is not big here, since it uses reflection
    val currentScreen = Screen::class.sealedSubclasses.firstOrNull {
        currentDestination?.route?.contains(it.objectInstance!!.route) ?: false
    }?.objectInstance

    LaunchedEffect(Unit) {
        navigationManager.handleNavigationCommands(navController)
    }

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            if (currentScreen?.shouldShowBottomNav == true) {
                BottomNavigation(navController = navController)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            if (currentScreen?.shouldShowBottomNav == true) {
                CartButton(uiState.countOfItemsInCart) {
                    navigationManager.navigate(Screen.Cart.route)
                }
            }
        }
    ) { innerPadding ->
        MainNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
            scaffoldState = scaffoldState
        )
    }
}

@Composable
private fun BottomNavigation(navController: NavController) {
    BottomNavigation {
        BottomNavItem(navController = navController, screen = Screen.Home)
        BottomNavItem(navController = navController, screen = Screen.Search)
    }
}

@Composable
private fun RowScope.BottomNavItem(navController: NavController, screen: Screen) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigationItem(
        icon = {
            Icon(
                screen.icon!!,
                modifier = Modifier.size(32.dp),
                contentDescription = null
            )
        },
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}

@Composable
private fun CartButton(countOfItemsInCart: Int, onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy((-16).dp),
        horizontalAlignment = Alignment.End
    ) {
        Surface(
            shape = CircleShape,
            color = Color.Red,
            border = BorderStroke(1.dp, Color.LightGray),
            modifier = Modifier
                .size(24.dp)
                .alpha(if (countOfItemsInCart > 0) 1f else 0f)
                .zIndex(1f)
        ) {
            Text(
                String.format("%02d", countOfItemsInCart),
                color = Color.White,
                style = MaterialTheme.typography.caption,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center)
            )
        }
        FloatingActionButton(
            shape = CircleShape,
            backgroundColor = MaterialTheme.colors.surface,
            onClick = onClick,
            modifier = Modifier.zIndex(0f)
        ) {
            Icon(TablerIcons.ShoppingCart, "")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WCStoreAppTheme {
        Main(MainViewModel.UiState(1), AndroidNavigationManager())
    }
}