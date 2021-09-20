package com.hicham.wcstoreapp.ui.product

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.Scale
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.hicham.wcstoreapp.R
import com.hicham.wcstoreapp.data.source.fake.FakeProductsRepository
import com.hicham.wcstoreapp.ui.components.ErrorView
import com.hicham.wcstoreapp.ui.components.WCTopAppBar
import compose.icons.TablerIcons
import compose.icons.tablericons.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun ProductScreen(viewModel: ProductViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    ProductScreen(uiState = uiState)
}

@Composable
private fun ProductScreen(uiState: ProductViewModel.UiState) {
    val title =
        if (uiState is ProductViewModel.UiState.SuccessState) uiState.product.name else ""
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
    Column {
        val pagesCount = state.product.images.size.takeIf { it > 0 } ?: 1
        val pagerState = rememberPagerState(pageCount = pagesCount)

        val coroutineScope = rememberCoroutineScope()

        val product = state.product

        Box(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
        ) {
            HorizontalPager(
                state = pagerState,

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
                        .padding(16.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentDescription = ""
                )
            }

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }, modifier = Modifier.align(Alignment.CenterStart),
                enabled = pagerState.currentPage > 0
            ) {
                Icon(imageVector = TablerIcons.ChevronLeft, contentDescription = "")
            }

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }, modifier = Modifier.align(Alignment.CenterEnd),
                enabled = pagerState.currentPage < pagerState.pageCount - 1
            ) {
                Icon(imageVector = TablerIcons.ChevronRight, contentDescription = "")
            }

            PagerIndicator(pagerState, modifier = Modifier.align(Alignment.BottomCenter))
        }
        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = product.name,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = state.priceFormatted,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

/**
 * A pretty simple pager indicator implementation
 */
@ExperimentalPagerApi
@Composable
private fun PagerIndicator(state: PagerState, modifier: Modifier) {
    if (state.pageCount == 1) return

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        (0 until state.pageCount).forEach {
            if (it == state.currentPage) {
                Canvas(modifier = Modifier.size(24.dp, 4.dp)) {
                    drawRoundRect(Color.Black, cornerRadius = CornerRadius(2.dp.toPx()))
                }
            } else {
                Canvas(modifier = Modifier.size(8.dp, 4.dp)) {
                    drawRoundRect(Color.Gray, cornerRadius = CornerRadius(2.dp.toPx()))
                }
            }
        }
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