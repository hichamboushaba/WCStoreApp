package com.hicham.wcstoreapp.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.PagingData
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.products.ProductUiModel
import com.hicham.wcstoreapp.ui.products.ProductsList
import kotlinx.coroutines.flow.Flow

@Composable
fun SearchScreen(viewModel: SearchViewModel, scaffoldState: ScaffoldState) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    SearchScreen(
        productsList = viewModel.products,
        searchQuery = searchQuery,
        scaffoldState = scaffoldState,
        onQueryChanged = viewModel::onQueryChanged
    )
}

@Composable
fun SearchScreen(
    productsList: Flow<PagingData<ProductUiModel>>,
    searchQuery: String,
    scaffoldState: ScaffoldState,
    onQueryChanged: (String) -> Unit = {},
    addItemToCart: (Product) -> Unit = {},
    removeItemFromCart: (Product) -> Unit = {},
    openProduct: (Long) -> Unit = {}
) {
    Column {
        TextField(value = searchQuery, onValueChange = onQueryChanged)

        ProductsList(
            productsFlow = productsList,
            addItemToCart = addItemToCart,
            removeItemFromCart = removeItemFromCart,
            openProduct = openProduct,
            scaffoldState = scaffoldState
        )
    }
}