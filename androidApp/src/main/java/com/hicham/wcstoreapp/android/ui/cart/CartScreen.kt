package com.hicham.wcstoreapp.android.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hicham.wcstoreapp.android.ui.common.components.CartTotals
import com.hicham.wcstoreapp.android.ui.common.components.WCTopAppBar
import com.hicham.wcstoreapp.data.product.fake.PRODUCTS_JSON
import com.hicham.wcstoreapp.data.storeApi.NetworkProduct
import com.hicham.wcstoreapp.data.storeApi.toDomainModel
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.cart.CartViewModel
import compose.icons.TablerIcons
import compose.icons.tablericons.ShoppingCart
import compose.icons.tablericons.ShoppingCartPlus
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Composable
fun CartScreen(
    viewModel: CartViewModel
) {
    val uiState by viewModel.uiState.collectAsState(CartViewModel.CartUiState())

    CartScreen(
        state = uiState,
        onIncreaseQuantity = viewModel::onIncreaseQuantity,
        onDecreaseQuantity = viewModel::onDecreaseQuantity,
        onRemoveProduct = viewModel::onRemoveProduct,
        onCheckout = viewModel::onCheckoutClicked,
        onBack = viewModel::onBackClicked,
        onGoToProducts = viewModel::onGoToProductsClicked
    )
}

@Composable
fun CartScreen(
    state: CartViewModel.CartUiState,
    onIncreaseQuantity: (Product) -> Unit,
    onDecreaseQuantity: (Product) -> Unit,
    onRemoveProduct: (Product) -> Unit,
    onCheckout: () -> Unit,
    onBack: () -> Unit,
    onGoToProducts: () -> Unit
) {
    Scaffold(
        topBar = {
            WCTopAppBar(
                title = {
                    Image(
                        imageVector = TablerIcons.ShoppingCart,
                        contentDescription = null
                    )
                    Text(
                        text = "Shopping Cart",
                        maxLines = 1,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                },
                onNavigationClick = onBack
            )
        }
    ) { innerPadding ->
        if (state.cartItems.isEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    16.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Text(
                    text = "Your shopping cart is empty",
                    style = MaterialTheme.typography.subtitle1
                )
                Icon(
                    imageVector = TablerIcons.ShoppingCartPlus,
                    modifier = Modifier.size(84.dp),
                    contentDescription = ""
                )
                Button(onClick = onGoToProducts) {
                    Text(text = "Check available products")
                }
            }

        } else {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(innerPadding)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(top = 8.dp)
                ) {
                    items(state.cartItems) {
                        CartListItem(
                            item = it,
                            onIncreaseQuantity = { onIncreaseQuantity(it.product) },
                            onDecreaseQuantity = { onDecreaseQuantity(it.product) },
                            onRemove = { onRemoveProduct(it.product) }
                        )
                    }
                }

                CartTotals(
                    subtotal = state.subtotalFormatted,
                    tax = state.taxFormatted,
                    total = state.totalFormatted,
                    buttonLabel = "Checkout",
                    onButtonClick = onCheckout
                )
            }
        }
        if (state.isUpdatingCart) {
            Box(modifier = Modifier
                .pointerInput(Unit) {
                    // Consume all events to prevent clicking on other views
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent(pass = PointerEventPass.Initial)
                                .changes
                                .forEach(PointerInputChange::consumeAllChanges)
                        }
                    }
                }
                .fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview
@Composable
private fun EmptyCartPreview() {
    CartScreen(
        state = CartViewModel.CartUiState(),
        onIncreaseQuantity = {},
        onDecreaseQuantity = {},
        onRemoveProduct = {},
        onCheckout = {},
        onBack = {},
        onGoToProducts = {}
    )
}

@Preview
@Composable
private fun CartPreview() {
    val productsList =
        Json.decodeFromString(ListSerializer(NetworkProduct.serializer()), PRODUCTS_JSON)
            .map { it.toDomainModel() }
            .take(3)

    val items = productsList.map {
        CartViewModel.CartItemUiModel(
            product = it,
            totalPriceFormatted = "10 $",
            quantity = 1
        )
    }
    val state = CartViewModel.CartUiState(
        cartItems = items + items,
        subtotalFormatted = "10$",
        totalFormatted = "10$"
    )
    CartScreen(
        state = state,
        onIncreaseQuantity = {},
        onDecreaseQuantity = {},
        onRemoveProduct = {},
        onCheckout = {},
        onBack = {},
        onGoToProducts = {}
    )
}

@Suppress("JSON_FORMAT_REDUNDANT")
@Preview
@Composable
private fun CartUpdatingPreview() {
    val productsList = Json {
        ignoreUnknownKeys = true
    }.decodeFromString(ListSerializer(NetworkProduct.serializer()), PRODUCTS_JSON)
        .map { it.toDomainModel() }
        .take(3)

    val items = productsList.map {
        CartViewModel.CartItemUiModel(
            product = it,
            totalPriceFormatted = "10 $",
            quantity = 1
        )
    }
    val state = CartViewModel.CartUiState(
        isUpdatingCart = true,
        cartItems = items + items,
        subtotalFormatted = "10$",
        totalFormatted = "10$"
    )
    CartScreen(
        state = state,
        onIncreaseQuantity = {},
        onDecreaseQuantity = {},
        onRemoveProduct = {},
        onCheckout = {},
        onBack = {},
        onGoToProducts = {}
    )
}