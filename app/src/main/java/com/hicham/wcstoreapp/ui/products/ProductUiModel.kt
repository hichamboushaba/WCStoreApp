package com.hicham.wcstoreapp.ui.products

import com.hicham.wcstoreapp.models.CartItem
import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.ui.CurrencyFormatter

data class ProductUiModel(
    val product: Product,
    val priceFormatted: String,
    val quantityInCart: Int = 0
)

fun Product.toUiModel(
    currencyFormatter: CurrencyFormatter,
    cartItems: List<CartItem>
): ProductUiModel {
    return ProductUiModel(
        product = this,
        priceFormatted = currencyFormatter.format(price),
        quantityInCart = cartItems.firstOrNull { it.product == this }?.quantity ?: 0
    )
}