package com.hicham.wcstoreapp.android.ui.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import com.hicham.wcstoreapp.android.ui.products.ProductsList
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.ShowSnackbar
import com.hicham.wcstoreapp.ui.products.ProductsUiListState
import com.hicham.wcstoreapp.ui.search.SearchViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Search
import compose.icons.tablericons.X

@Composable
fun SearchScreen(viewModel: SearchViewModel, scaffoldState: ScaffoldState) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val products by viewModel.products.collectAsState(
        initial = ProductsUiListState()
    )

    LaunchedEffect("effects") {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    SearchScreen(
        products = products,
        searchQuery = searchQuery,
        scaffoldState = scaffoldState,
        onQueryChanged = viewModel::onQueryChanged,
        addItemToCart = viewModel::addItemToCart,
        removeItemFromCart = viewModel::deleteItemFromCart,
        loadNext = viewModel::loadNext,
        openProduct = viewModel::onProductClicked
    )
}

@Composable
fun SearchScreen(
    products: ProductsUiListState,
    searchQuery: String,
    scaffoldState: ScaffoldState,
    onQueryChanged: (String) -> Unit = {},
    addItemToCart: (Product) -> Unit = {},
    removeItemFromCart: (Product) -> Unit = {},
    openProduct: (Product) -> Unit = {},
    retry: () -> Unit = {},
    loadNext: () -> Unit = {},
) {
    Scaffold(scaffoldState = scaffoldState, topBar = {
        TopAppBar {
            TextField(
                value = searchQuery,
                onValueChange = onQueryChanged,
                leadingIcon = {
                    Icon(
                        imageVector = TablerIcons.Search,
                        contentDescription = "",
                        tint = MaterialTheme.colors.onPrimary
                    )
                },
                placeholder = {
                    Text(text = "Search")
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                onQueryChanged("")
                            }
                        ) {
                            Icon(
                                TablerIcons.X,
                                contentDescription = "",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RectangleShape,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    cursorColor = MaterialTheme.colors.onPrimary,
                    placeholderColor = MaterialTheme.colors.onPrimary.copy(alpha = 0.6f)
                )
            )
        }
    }) {

        ProductsList(
            productsUiListState = products,
            addItemToCart = addItemToCart,
            removeItemFromCart = removeItemFromCart,
            onProductClicked = openProduct,
            retry = retry,
            loadNext = loadNext,
            scaffoldState = scaffoldState
        )
    }
}

@Composable
@Preview
private fun SearchPreview() {
    SearchScreen(
        products = ProductsUiListState(),
        searchQuery = "test",
        scaffoldState = rememberScaffoldState()
    )
}