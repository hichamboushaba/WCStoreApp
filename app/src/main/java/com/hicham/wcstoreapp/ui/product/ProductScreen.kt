package com.hicham.wcstoreapp.ui.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hicham.wcstoreapp.R
import com.hicham.wcstoreapp.data.source.fake.FakeProductsRepository
import com.hicham.wcstoreapp.ui.components.ErrorView
import com.hicham.wcstoreapp.ui.components.WCTopAppBar
import kotlinx.coroutines.runBlocking

@Composable
fun ProductScreen(viewModel: ProductViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    ProductScreen(uiState = uiState)
}

@Composable
private fun ProductScreen(uiState: ProductViewModel.UiState) {
    val title =
        if (uiState is ProductViewModel.UiState.SuccessState) uiState.product.name else "Product"
    Scaffold(
        topBar = {
            WCTopAppBar(
                title = title,
                onNavigationClick = {/*TODO*/ }
            )
        }
    ) { innerPadding ->
        when (uiState) {
            ProductViewModel.UiState.ErrorState -> ErrorView() {
                // TODO
            }
            ProductViewModel.UiState.LoadingState -> LoadingView()
            is ProductViewModel.UiState.SuccessState -> ProductDetails(uiState)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ProductDetails(state: ProductViewModel.UiState.SuccessState) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        val pagesCount = state.product.images.size.takeIf { it > 0 } ?: 1
        val pagerState = rememberPagerState(pageCount = pagesCount)

        val product = state.product

        HorizontalPager(
            state = pagerState,
            Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
        ) {
            Image(
                painter = rememberImagePainter(
                    data = state.product.images.getOrNull(it),
                    builder = {
                        crossfade(true)
                        scale(Scale.FIT)
                        placeholder(R.drawable.ic_product_placeholder)
                        error(R.drawable.ic_product_placeholder)
                    }
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium),
                contentDescription = ""
            )
        }
        Spacer(modifier = Modifier.size(32.dp))
        Text(text = product.name, style = MaterialTheme.typography.subtitle1)
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = state.priceFormatted, style = MaterialTheme.typography.subtitle2)
    }
}

@Composable
private fun LoadingView() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
    )
}

@Preview
@Composable
private fun ProductPreview() {
    val product = runBlocking { FakeProductsRepository().getProduct(0L) }
    val state = ProductViewModel.UiState.SuccessState(
        product = product,
        priceFormatted = "10 $",
        quantityInCart = 1
    )

    ProductScreen(uiState = state)
}