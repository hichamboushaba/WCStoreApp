package com.hicham.wcstoreapp.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
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

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            InsetAwareTopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) {
        ProductsList(productsFlow = productsFlow)
    }
}

@Composable
fun ProductsList(productsFlow: Flow<PagingData<Product>>) {
    val lazyProductList = productsFlow.collectAsLazyPagingItems()

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (lazyProductList.loadState.refresh == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillParentMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
        }

        items(items = lazyProductList) { product ->
            product?.let { ProductCard(product = it) }
        }

        if (lazyProductList.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
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