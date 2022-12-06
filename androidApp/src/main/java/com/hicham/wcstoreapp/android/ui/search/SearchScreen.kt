package com.hicham.wcstoreapp.android.ui.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.ShowSnackbar
import com.hicham.wcstoreapp.ui.products.ProductUiModel
import com.hicham.wcstoreapp.ui.products.ProductsList
import com.hicham.wcstoreapp.ui.search.SearchViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.Search
import compose.icons.tablericons.X
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun SearchScreen(viewModel: SearchViewModel, scaffoldState: ScaffoldState) {
    val searchQuery by viewModel.searchQuery.collectAsState()

    LaunchedEffect("effects") {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ShowSnackbar -> scaffoldState.snackbarHostState.showSnackbar(effect.message)
            }
        }
    }

    SearchScreen(
        products = viewModel.products,
        searchQuery = searchQuery,
        scaffoldState = scaffoldState,
        onQueryChanged = viewModel::onQueryChanged,
        openProduct = viewModel::onProductClicked
    )
}

@Composable
fun SearchScreen(
    products: Flow<PagingData<ProductUiModel>>,
    searchQuery: String,
    scaffoldState: ScaffoldState,
    onQueryChanged: (String) -> Unit = {},
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
            productsFlow = products,
            onProductClicked = openProduct,
            scaffoldState = scaffoldState,
            modifier = Modifier.padding(it)
        )
    }
}

@Composable
@Preview
private fun SearchPreview() {
    SearchScreen(
        products = flowOf(PagingData.empty()),
        searchQuery = "test",
        scaffoldState = rememberScaffoldState()
    )
}