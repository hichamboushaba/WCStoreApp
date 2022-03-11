package com.hicham.wcstoreapp.android.ui.products

import com.hicham.wcstoreapp.models.Product

data class ProductUiModel(
    val product: Product,
    val priceFormatted: String,
    val quantityInCart: Int = 0
)