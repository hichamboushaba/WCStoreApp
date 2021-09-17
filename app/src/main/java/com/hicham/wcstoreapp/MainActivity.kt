package com.hicham.wcstoreapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ui.BottomNavigation
import com.hicham.wcstoreapp.ui.Screen
import com.hicham.wcstoreapp.ui.home.HomeScreen
import com.hicham.wcstoreapp.ui.theme.WCStoreAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WCStoreAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = { BottomNavigation(navController = navController) }
                    ) { innerPadding ->
                        NavHost(
                            navController,
                            startDestination = Screen.Home.route,
                            Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.Home.route) {
                                HomeScreen(viewModel = hiltViewModel())
                            }
                        }
                    }
                }
            }
        }
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
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        }
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WCStoreAppTheme {
        Greeting("Android")
    }
}