package com.hicham.wcstoreapp.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.navigationBarsHeight
import com.hicham.wcstoreapp.R
import com.hicham.wcstoreapp.data.Status
import com.hicham.wcstoreapp.data.source.ProductsRepository
import com.hicham.wcstoreapp.data.source.network.FakeWooCommerceApi
import com.hicham.wcstoreapp.ui.components.InsetAwareTopAppBar
import com.hicham.wcstoreapp.ui.theme.WCStoreAppTheme
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val state by viewModel.uiState.collectAsState()

    HomeScreen(uiState = state, scaffoldState = scaffoldState)
}

@Composable
fun HomeScreen(
    uiState: UiState,
    scaffoldState: ScaffoldState
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            InsetAwareTopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) {
        LazyColumn {
            items(uiState.products) { product ->
                ProductCard(product = product)
            }
        }
    }
}

@Preview
@Composable
fun DefaultHome() {
    val products = runBlocking {
            ProductsRepository(FakeWooCommerceApi(Json)).getProductList()
                .first().getOrThrow()
        }

    WCStoreAppTheme {
        HomeScreen(
            UiState(
                isLoading = false,
                products = products
            ),
            scaffoldState = rememberScaffoldState()
        )
    }
}