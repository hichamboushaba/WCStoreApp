package com.hicham.wcstoreapp.ui.navigation

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hicham.wcstoreapp.ui.cart.CartScreen
import com.hicham.wcstoreapp.ui.home.HomeScreen
import com.hicham.wcstoreapp.ui.product.ProductScreen

@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    NavHost(
        navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(viewModel = hiltViewModel())
        }
        composable(Screen.Cart.route) {
            CartScreen(viewModel = hiltViewModel())
        }
        composable(
            Screen.Product.route,
            arguments = Screen.Product.navArguments
        ) {
            ProductScreen(viewModel = hiltViewModel(), scaffoldState)
        }
    }
}