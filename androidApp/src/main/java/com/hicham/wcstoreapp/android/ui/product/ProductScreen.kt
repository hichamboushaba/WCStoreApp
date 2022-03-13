package com.hicham.wcstoreapp.android.ui.product

import android.util.TypedValue
import android.widget.TextView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.hicham.wcstoreapp.android.R
import com.hicham.wcstoreapp.android.data.product.fake.FakeProductsRepository
import com.hicham.wcstoreapp.android.ui.common.components.ErrorView
import com.hicham.wcstoreapp.android.ui.common.components.WCTopAppBar
import com.hicham.wcstoreapp.ui.ShowActionSnackbar
import com.hicham.wcstoreapp.ui.ShowSnackbar
import com.hicham.wcstoreapp.ui.product.ProductViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.ChevronLeft
import compose.icons.tablericons.ChevronRight
import compose.icons.tablericons.ShoppingCartPlus
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun ProductScreen(viewModel: ProductViewModel, scaffoldState: ScaffoldState) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = scaffoldState.snackbarHostState

    LaunchedEffect("effect") {
        viewModel.effects.collect {
            when (it) {
                is ShowActionSnackbar -> {
                    val result =
                        snackbarHostState.showSnackbar(it.message, actionLabel = it.actionText)
                    if (result == SnackbarResult.ActionPerformed) {
                        it.action()
                    }
                }
                is ShowSnackbar -> {
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }
    ProductScreen(
        uiState = uiState,
        onAddToCart = viewModel::onAddToCartClicked,
        onBack = viewModel::onBackClicked
    )
}

@Composable
private fun ProductScreen(
    uiState: ProductViewModel.UiState,
    onAddToCart: () -> Unit,
    onBack: () -> Unit
) {
    val title =
        if (uiState is ProductViewModel.UiState.SuccessState) uiState.product.name else ""

    Scaffold(
        topBar = {
            WCTopAppBar(
                title = title,
                onNavigationClick = onBack
            )
        }
    ) { innerPadding ->
        when (uiState) {
            ProductViewModel.UiState.ErrorState -> ErrorView() {
                // TODO
            }
            ProductViewModel.UiState.LoadingState -> LoadingView()
            is ProductViewModel.UiState.SuccessState -> ProductDetails(uiState, onAddToCart)
        }
    }
}

@Composable
private fun ProductDetails(state: ProductViewModel.UiState.SuccessState, onAddToCart: () -> Unit) {
    val product = state.product
    val scrollState = rememberScrollState()
    Column {
        ProductImagesPager(
            state.product.images,
            elevation = if (scrollState.value > 0) 8.dp else 0.dp
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            Spacer(modifier = Modifier.size(32.dp))
            Text(
                text = state.priceFormatted,
                style = MaterialTheme.typography.subtitle1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            HtmlText(
                text = product.shortDescription,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            HtmlText(
                text = product.description,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = TablerIcons.ShoppingCartPlus, contentDescription = null)
                    Text(text = "Add to Cart")
                }
            }
        }
    }
}

@Composable
private fun HtmlText(
    text: String,
    style: TextStyle = LocalTextStyle.current,
    modifier: Modifier = Modifier
) {
    // We are using just a subset of the passed [style]

    val defaultColor = LocalContentColor.current
    with(LocalDensity.current) {
        AndroidView(modifier = modifier, factory = { context ->
            TextView(context).apply {
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, style.fontSize.toDp().value)
                setTextColor(style.color.takeOrElse { defaultColor }.toArgb())
                if (style.letterSpacing.isSpecified) {
                    // Convert to em, TODO check if this is correct actually
                    letterSpacing = style.letterSpacing.toPx() / style.fontSize.toPx()
                }
                setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY).trim('\n'))
            }
        })
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ProductImagesPager(images: List<String>, elevation: Dp, modifier: Modifier = Modifier) {
    val pagesCount = images.size.takeIf { it > 0 } ?: 1
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Surface(elevation = elevation) {
        Box(
            modifier = modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                count = pagesCount,
                modifier = Modifier.fillMaxSize()
            ) {
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

    ProductScreen(uiState = state, {}, {})
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