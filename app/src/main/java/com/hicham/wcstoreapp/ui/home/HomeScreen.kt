package com.hicham.wcstoreapp.ui.home

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import com.hicham.wcstoreapp.ui.products.ProductsList

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    ProductsList(
        productsFlow = viewModel.products,
        addItemToCart = viewModel::addItemToCart,
        removeItemFromCart = viewModel::deleteItemFromCart,
        openProduct = viewModel::onProductClicked,
        scaffoldState = scaffoldState
    )
}
