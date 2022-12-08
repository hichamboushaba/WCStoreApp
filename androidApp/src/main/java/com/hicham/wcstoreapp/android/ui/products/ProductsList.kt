package com.hicham.wcstoreapp.ui.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.hicham.wcstoreapp.android.ui.common.components.ErrorView
import com.hicham.wcstoreapp.android.ui.products.ProductCard
import com.hicham.wcstoreapp.android.ui.theme.WCStoreAppTheme
import com.hicham.wcstoreapp.data.product.fake.FakeProductsRepository
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun ProductsList(
    productsFlow: Flow<PagingData<ProductUiModel>>,
    onProductClicked: (Product) -> Unit,
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier
) {
    val lazyProductList = productsFlow.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()

    val minCardWidth = 160.dp

    BoxWithConstraints(modifier = modifier) {
        val nbColumns = (maxWidth / minCardWidth).toInt()
        val size = (maxWidth / nbColumns) - 16.dp
        val dbSourceLoadState = lazyProductList.loadState.mediator
        val loadState = lazyProductList.loadState
        when {
            loadState.refresh is LoadState.Loading && (
                    dbSourceLoadState == null ||
                            lazyProductList.itemCount == 0) -> {
                // When refreshing and there is no items, or there is no offline cache
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }
            loadState.refresh is LoadState.Error && lazyProductList.itemCount == 0 -> {
                ErrorView(modifier = Modifier.fillMaxSize()) { lazyProductList.retry() }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    renderList(
                        lazyProductList = lazyProductList,
                        onProductClick = onProductClicked,
                        nbColumns = nbColumns,
                        itemsSize = size
                    )

                    handleLoadState(
                        coroutineScope = coroutineScope,
                        loadState = loadState,
                        scaffoldState = scaffoldState
                    ) { lazyProductList.retry() }
                }
            }
        }
    }
}

private fun LazyListScope.handleLoadState(
    coroutineScope: CoroutineScope,
    loadState: CombinedLoadStates,
    scaffoldState: ScaffoldState,
    retry: () -> Unit
) {
    when {
        loadState.refresh is LoadState.Error -> {
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
        loadState.append == LoadState.Loading -> {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                )
            }
        }
        loadState.append is LoadState.Error -> {
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
    lazyProductList: LazyPagingItems<ProductUiModel>,
    onProductClick: (Product) -> Unit,
    nbColumns: Int,
    itemsSize: Dp
) {
    val rowsCount =
        kotlin.math.ceil(lazyProductList.itemCount.toDouble() / nbColumns).toInt()

    // TODO: pass a key that works with the grid implementation
    items(count = rowsCount) { row ->
        val firstIndex = row * nbColumns
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            (firstIndex until firstIndex + nbColumns).forEach { itemIndex ->
                if (itemIndex < lazyProductList.itemCount) {
                    lazyProductList[itemIndex]?.let {
                        ProductCard(
                            uiModel = it,
                            modifier = Modifier
                                .size(itemsSize)
                                .clickable { onProductClick(it.product) }
                        )
                    }
                } else {
                    Spacer(Modifier.size(itemsSize))
                }
            }
        }
    }
}

// TODO check why the preview is not working
@Preview
@Composable
fun DefaultList() {
    val productsFlow = FakeProductsRepository().getProductList()
        .map { data ->
            data.map { ProductUiModel(it, "10 USD", addToCart = {}, deleteFromCart = {}) }
        }

    WCStoreAppTheme {
        ProductsList(
            productsFlow = productsFlow,
            onProductClicked = {},
            scaffoldState = rememberScaffoldState()
        )
    }
}