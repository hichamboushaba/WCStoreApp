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
import com.hicham.wcstoreapp.data.product.fake.FakeProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.common.components.ErrorView
import com.hicham.wcstoreapp.ui.home.ProductUiModel
import com.hicham.wcstoreapp.ui.theme.WCStoreAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun ProductsList(
    productsFlow: Flow<PagingData<ProductUiModel>>,
    addItemToCart: (Product) -> Unit,
    removeItemFromCart: (Product) -> Unit,
    openProduct: (Long) -> Unit,
    scaffoldState: ScaffoldState
) {
    val lazyProductList = productsFlow.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()

    val minCardWidth = 160.dp

    BoxWithConstraints {
        val nbColumns = (maxWidth / minCardWidth).toInt()
        val size = (maxWidth / nbColumns) - 16.dp
        val loadState = lazyProductList.loadState
        when {
            loadState.refresh is LoadState.Loading && lazyProductList.itemCount == 0 -> {
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
                        addItemToCart = addItemToCart,
                        removeItemFromCart = removeItemFromCart,
                        onProductClick = openProduct,
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
    addItemToCart: (Product) -> Unit,
    removeItemFromCart: (Product) -> Unit,
    onProductClick: (Long) -> Unit,
    nbColumns: Int,
    itemsSize: Dp
) {
    val rowsCount =
        kotlin.math.ceil(lazyProductList.itemCount.toDouble() / nbColumns).toInt()

    items(count = rowsCount, key = { lazyProductList[it]?.product?.id ?: 0L }) { row ->
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
                            addItemToCart = addItemToCart,
                            removeItemFromCart = removeItemFromCart,
                            modifier = Modifier
                                .size(itemsSize)
                                .clickable { onProductClick(it.product.id) }
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
            data.map { ProductUiModel(it, "10 USD") }
        }

    WCStoreAppTheme {
        ProductsList(
            productsFlow = productsFlow,
            addItemToCart = {},
            removeItemFromCart = {},
            openProduct = {},
            scaffoldState = rememberScaffoldState()
        )
    }
}