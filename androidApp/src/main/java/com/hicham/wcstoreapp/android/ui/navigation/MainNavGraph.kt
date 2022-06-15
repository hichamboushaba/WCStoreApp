package com.hicham.wcstoreapp.android.ui.navigation

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hicham.wcstoreapp.android.ui.cart.CartScreen
import com.hicham.wcstoreapp.android.ui.checkout.CheckoutScreen
import com.hicham.wcstoreapp.android.ui.checkout.address.AddAddressScreen
import com.hicham.wcstoreapp.android.ui.checkout.address.AddressListScreen
import com.hicham.wcstoreapp.android.ui.checkout.after.OrderPlacedScreen
import com.hicham.wcstoreapp.android.ui.home.HomeScreen
import com.hicham.wcstoreapp.android.ui.product.ProductScreen
import com.hicham.wcstoreapp.android.ui.search.SearchScreen
import com.hicham.wcstoreapp.ui.navigation.Screen
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

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
            HomeScreen(viewModel = getViewModel(), scaffoldState = scaffoldState)
        }
        composable(Screen.Search.route) {
            SearchScreen(viewModel = getViewModel(), scaffoldState = scaffoldState)
        }
        composable(Screen.Cart.route) {
            CartScreen(viewModel = getViewModel())
        }
        composable(
            Screen.Product.route,
            arguments = Screen.Product.navArguments.toNamedNavArguments()
        ) { backStackEntry ->
            ProductScreen(
                viewModel = getViewModel {
                    parametersOf(
                        backStackEntry
                            .arguments
                            ?.getLong(Screen.Product.navArguments.first().name)
                    )
                }, scaffoldState
            )
        }
        composable(
            Screen.Checkout.route
        ) {
            CheckoutScreen(viewModel = getViewModel(), scaffoldState = scaffoldState)
        }
        composable(
            Screen.AddressList.route
        ) {
            AddressListScreen(viewModel = getViewModel(), scaffoldState = scaffoldState)
        }
        composable(
            Screen.AddAddress.route
        ) {
            AddAddressScreen(viewModel = getViewModel())
        }
        composable(
            Screen.OrderPlaced.route
        ) { backStackEntry ->
            OrderPlacedScreen(
                orderId = backStackEntry
                    .arguments
                    ?.getString(Screen.OrderPlaced.navArguments.first().name)!!
                    .toLong(),
                onNavigateToHome = { navController.navigate(Screen.Home.route) }
            )
        }
    }
}
