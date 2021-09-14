package com.hicham.wcstoreapp.ui.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.hicham.wcstoreapp.R
import com.hicham.wcstoreapp.data.source.fake.FakeProductsRepository
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.components.InsetAwareTopAppBar
import com.hicham.wcstoreapp.ui.theme.WCStoreAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.Math.ceil

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
        ProductsList(productsFlow = productsFlow, scaffoldState = scaffoldState)
    }
}

// TODO make it dynamic
private const val COLUMNS_COUNT = 3

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductsList(
    productsFlow: Flow<PagingData<Product>>,
    scaffoldState: ScaffoldState
) {
    val lazyProductList = productsFlow.collectAsLazyPagingItems()

    val coroutineScope = rememberCoroutineScope()

    when (lazyProductList.loadState.refresh) {
        LoadState.Loading -> {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
        is LoadState.Error -> {
            ErrorView(modifier = Modifier.fillMaxSize()) { lazyProductList.retry() }
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                val rowsCount =
                    kotlin.math.ceil(lazyProductList.itemCount.toDouble() / COLUMNS_COUNT).toInt()
                items(count = rowsCount) { row ->
                    val firstIndex = row * COLUMNS_COUNT
                    Row {
                        (firstIndex until firstIndex + COLUMNS_COUNT).forEach { itemIndex ->
                            if (itemIndex < lazyProductList.itemCount) {
                                lazyProductList[itemIndex]?.let { ProductCard(product = it) }
                            }
                        }
                    }
                }
                if (lazyProductList.loadState.append == LoadState.Loading) {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .wrapContentWidth(align = Alignment.CenterHorizontally)
                        )
                    }
                } else if (lazyProductList.loadState.append is LoadState.Error) {
                    coroutineScope.launch {
                        println("show snackbar")
                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            message = "Loading Products Failed",
                            actionLabel = "Retry",
                            duration = SnackbarDuration.Indefinite
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            lazyProductList.retry()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = ImageBitmap.imageResource(id = android.R.drawable.stat_notify_error),
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface)
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
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

@ExperimentalFoundationApi
fun <T : Any> LazyGridScope.items(
    items: LazyPagingItems<T>,
    itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    items(items.itemCount) { index ->
        itemContent(items[index])
    }
}
