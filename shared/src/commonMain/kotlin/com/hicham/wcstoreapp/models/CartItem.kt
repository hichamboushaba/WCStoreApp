package com.hicham.wcstoreapp.models

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class CartItem(
    val id: String,
    val product: Product,
    val quantity: Int,
    val totals: CartItemTotals
)

data class CartItemTotals(
    val subtotal: BigDecimal,
    val tax: BigDecimal,
    val total: BigDecimal
) {
    companion object {
        val ZERO by lazy { CartItemTotals(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO) }
    }
}