package com.hicham.wcstoreapp.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hicham.wcstoreapp.data.source.fake.PRODUCTS_JSON
import com.hicham.wcstoreapp.data.source.network.NetworkProduct
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.toProduct
import com.hicham.wcstoreapp.ui.components.InsetAwareTopAppBar
import compose.icons.TablerIcons
import compose.icons.tablericons.ShoppingCart
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onBack: () -> Unit
) {
    val items by viewModel.items.collectAsState(initial = emptyList())

    CartScreen(
        items = items,
        onIncreaseQuantity = viewModel::onIncreaseQuantity,
        onDecreaseQuantity = viewModel::onDecreaseQuantity,
        onRemoveProduct = viewModel::onRemoveProduct,
        onBack = onBack
    )
}

@Composable
fun CartScreen(
    items: List<CartViewModel.CartItemUiModel>,
    onIncreaseQuantity: (Product) -> Unit,
    onDecreaseQuantity: (Product) -> Unit,
    onRemoveProduct: (Product) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(onBack)
        }
    ) { innerPadding ->
        if (items.isEmpty()) {
            Text(text = "empty")
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(top = 8.dp)
            ) {
                items(items) {
                    CartListItem(
                        item = it,
                        onIncreaseQuantity = { onIncreaseQuantity(it.product) },
                        onDecreaseQuantity = { onDecreaseQuantity(it.product) },
                        onRemove = { onRemoveProduct(it.product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    InsetAwareTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .padding(end = 48.dp),
                horizontalArrangement = Arrangement.Center
            ) {
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
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "",
                    tint = MaterialTheme.colors.primary
                )
            }
        },
        backgroundColor = MaterialTheme.colors.surface
    )
}

@Preview
@Composable
fun CartPreview() {
    val productsList =
        Json.decodeFromString(ListSerializer(NetworkProduct.serializer()), PRODUCTS_JSON)
            .map { it.toProduct() }
            .take(3)

    val items = productsList.map {
        CartViewModel.CartItemUiModel(
            product = it,
            totalPriceFormatted = "10 $",
            quantity = 1
        )
    }
    CartScreen(
        items = items,
        onIncreaseQuantity = {},
        onDecreaseQuantity = {},
        onRemoveProduct = {},
        onBack = {}
    )
}