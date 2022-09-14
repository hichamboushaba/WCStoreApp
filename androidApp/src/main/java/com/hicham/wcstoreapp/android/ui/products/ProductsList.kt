package com.hicham.wcstoreapp.android.ui.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hicham.wcstoreapp.android.ui.common.components.ErrorView
import com.hicham.wcstoreapp.data.product.LoadingState
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.products.ProductsUiListState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProductsList(
    productsUiListState: ProductsUiListState,
    onProductClicked: (Product) -> Unit,
    retry: () -> Unit,
    loadNext: () -> Unit,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    val minCardWidth = 160.dp

    BoxWithConstraints(modifier) {
        val nbColumns = (maxWidth / minCardWidth).toInt()
        val size = (maxWidth / nbColumns) - 16.dp
        val hasOfflineData = productsUiListState.products.isNotEmpty()
        val loadState = productsUiListState.state
        when {
            loadState == LoadingState.Loading && !hasOfflineData -> {
                // When refreshing and there is no items, or there is no offline cache
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
            loadState == LoadingState.Error && !hasOfflineData -> {
                ErrorView(modifier = Modifier.fillMaxSize()) {
                    //TODO lazyProductList.retry()
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    renderList(
                        productsUiListState = productsUiListState,
                        onProductClick = onProductClicked,
                        loadNext = loadNext,
                        nbColumns = nbColumns,
                        itemsSize = size,
                    )

                    handleLoadState(
                        coroutineScope = coroutineScope,
                        loadState = productsUiListState.state,
                        scaffoldState = scaffoldState,
                        retry = { retry() }
                    )
                }
            }
        }
    }
}

private fun LazyListScope.handleLoadState(
    coroutineScope: CoroutineScope,
    loadState: LoadingState,
    scaffoldState: ScaffoldState,
    retry: () -> Unit,
) {
    when {
        loadState == LoadingState.Error -> {
            coroutineScope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = "Fetching products failed",
                    actionLabel = "Retry",
                    duration = SnackbarDuration.Long
                )
                if (result == SnackbarResult.ActionPerformed) {
                    retry()
                }
            }
        }
        loadState == LoadingState.AppendLoading -> {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                )
            }
        }
        loadState == LoadingState.AppendError -> {
            coroutineScope.launch {
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    message = "Loading Products Failed",
                    actionLabel = "Retry",
                    duration = SnackbarDuration.Indefinite
                )
                if (result == SnackbarResult.ActionPerformed) {
                    retry()
                }
            }
        }
    }
}

private fun LazyListScope.renderList(
    productsUiListState: ProductsUiListState,
    onProductClick: (Product) -> Unit,
    loadNext: () -> Unit,
    nbColumns: Int,
    itemsSize: Dp
) {
    val products = productsUiListState.products
    val rowsCount =
        kotlin.math.ceil(products.size.toDouble() / nbColumns).toInt()

    items(count = rowsCount, key = { products[it].product.id }) { row ->
        val firstIndex = row * nbColumns
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            (firstIndex until firstIndex + nbColumns).forEach { itemIndex ->
                if (itemIndex < products.size) {
                    products[itemIndex].let {
                        ProductCard(
                            uiModel = it,
                            modifier = Modifier
                                .size(itemsSize)
                                .clickable { onProductClick(it.product) }
                        )
                    }
                    if (productsUiListState.hasNext && itemIndex == products.size - 1) {
                        LaunchedEffect(key1 = "loadNext", block = { loadNext() })
                    }
                } else {
                    Spacer(Modifier.size(itemsSize))
                }
            }
        }
    }
}

// TODO check why the preview is not working
//@Preview
//@Composable
//fun DefaultList() {
//    val productsFlow = FakeProductsRepository().getProductList()
//        .map { data ->
//            data.map { ProductUiModel(it, "10 USD") }
//        }
//
//    WCStoreAppTheme {
//        ProductsList(
//            productsFlow = productsFlow,
//            addItemToCart = {},
//            removeItemFromCart = {},
//            onProductClicked = {},
//            scaffoldState = rememberScaffoldState()
//        )
//    }
//}