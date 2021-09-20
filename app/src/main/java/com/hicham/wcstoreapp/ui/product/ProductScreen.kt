package com.hicham.wcstoreapp.ui.product

import android.widget.TextView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
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

@Composable
private fun ProductDetails(state: ProductViewModel.UiState.SuccessState) {
    Column {

        val product = state.product

        ProductImagesPager(state.product.images)

        Spacer(modifier = Modifier.size(32.dp))
        Text(
            text = state.priceFormatted,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        HtmlText(text = product.shortDescription, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.size(8.dp))
        HtmlText(text = product.description, modifier = Modifier.padding(horizontal = 16.dp))
    }
}

@Composable
private fun HtmlText(text: String, modifier: Modifier = Modifier) {
    AndroidView(modifier = modifier, factory = { context ->
        TextView(context).apply {
            setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
        }
    })
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ProductImagesPager(images: List<String>) {
    val pagesCount = images.size.takeIf { it > 0 } ?: 1
    val pagerState = rememberPagerState(pageCount = pagesCount)
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth()
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = rememberImagePainter(
                    data = images.getOrNull(it),
                    builder = {
                        crossfade(true)
                        scale(Scale.FIT)
                        placeholder(R.drawable.ic_product_placeholder)
                        error(R.drawable.ic_product_placeholder)
                        size(OriginalSize)
                        transformations(MaterialTheme.shapes.medium.toCoilTransformation())
                    }
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
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

@Composable
fun CornerBasedShape.toCoilTransformation(): RoundedCornersTransformation {
    return with(LocalDensity.current) {
        RoundedCornersTransformation(
            topLeft = topStart.toPx(Size.Unspecified, this),
            topRight = topEnd.toPx(Size.Unspecified, this),
            bottomLeft = bottomStart.toPx(Size.Unspecified, this),
            bottomRight = bottomEnd.toPx(Size.Unspecified, this)
        )
    }
}