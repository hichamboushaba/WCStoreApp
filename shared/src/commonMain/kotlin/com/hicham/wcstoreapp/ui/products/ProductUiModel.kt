package com.hicham.wcstoreapp.ui.products

import com.hicham.wcstoreapp.data.product.LoadingState
import com.hicham.wcstoreapp.models.Product
import kotlinx.coroutines.Job

data class ProductUiModel(
    val product: Product,
    val priceFormatted: String,
    val quantityInCart: Int = 0,
    val addToCart: () -> Unit,
    val deleteFromCart: () -> Unit
)

data class ProductsUiListState(
    val products: List<ProductUiModel> = emptyList(),
    val hasNext: Boolean = false,
    val state: LoadingState = LoadingState.Loading
)