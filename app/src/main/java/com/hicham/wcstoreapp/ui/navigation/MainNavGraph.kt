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
import com.hicham.wcstoreapp.ui.checkout.CheckoutScreen
import com.hicham.wcstoreapp.ui.checkout.address.AddAddressScreen
import com.hicham.wcstoreapp.ui.checkout.address.AddressListScreen
import com.hicham.wcstoreapp.ui.checkout.after.OrderPlacedScreen
import com.hicham.wcstoreapp.ui.home.HomeScreen
import com.hicham.wcstoreapp.ui.product.ProductScreen
import com.hicham.wcstoreapp.ui.search.SearchScreen

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
            HomeScreen(viewModel = hiltViewModel(), scaffoldState = scaffoldState)
        }
        composable(Screen.Search.route) {
            SearchScreen(viewModel = hiltViewModel(), scaffoldState = scaffoldState)
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
        composable(
            Screen.Checkout.route
        ) {
            CheckoutScreen(viewModel = hiltViewModel())
        }
        composable(
            Screen.AddressList.route
        ) {
            AddressListScreen(viewModel = hiltViewModel())
        }
        composable(
            Screen.AddAddress.route
        ) {
            AddAddressScreen(viewModel = hiltViewModel())
        }
        composable(
            Screen.OrderPlaced.route
        ) { backStackEntry ->
            OrderPlacedScreen(
                orderId = backStackEntry
                    .arguments
                    ?.getString(Screen.OrderPlaced.navArguments.first().name)!!
                    .toLong(),
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
