package com.hicham.wcstoreapp.android.models

import java.math.BigDecimal

data class Cart(
    val items: List<CartItem>,
    val totals: CartTotals
)

data class CartTotals(
    val subtotal: BigDecimal,
    val tax: BigDecimal,
    val shippingEstimate: BigDecimal?,
    val total: BigDecimal
) {
    companion object {
        val ZERO by lazy {
            CartTotals(
                subtotal = BigDecimal.ZERO,
                tax = BigDecimal.ZERO,
                shippingEstimate = null,
                total = BigDecimal.ZERO
            )
        }
    }
}
