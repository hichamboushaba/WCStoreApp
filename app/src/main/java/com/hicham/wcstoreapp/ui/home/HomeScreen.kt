package com.hicham.wcstoreapp.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.hicham.wcstoreapp.R
import com.hicham.wcstoreapp.data.source.fake.FakeProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.components.InsetAwareTopAppBar
import com.hicham.wcstoreapp.ui.theme.WCStoreAppTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    HomeScreen(productsFlow = viewModel.products, scaffoldState = scaffoldState)
}

@Composable
fun HomeScreen(
    productsFlow: Flow<PagingData<Product>>,
    scaffoldState: ScaffoldState
) {
    val lazyProductList = productsFlow.collectAsLazyPagingItems()

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
            items(items = lazyProductList) { product ->
                product?.let { ProductCard(product = it) }
            }
        }
    }
}

@Preview
@Composable
fun DefaultHome() {
    val productsFlow = FakeProductsRepository().getProductList()

    WCStoreAppTheme {
        HomeScreen(
            productsFlow = productsFlow,
            scaffoldState = rememberScaffoldState()
        )
    }
}