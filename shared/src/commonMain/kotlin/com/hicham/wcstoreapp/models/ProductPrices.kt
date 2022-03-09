package com.hicham.wcstoreapp.models

import com.ionspin.kotlin.bignum.decimal.BigDecimal

data class ProductPrices(
    val price: BigDecimal,
    val regularPrice: BigDecimal,
    val salePrice: BigDecimal
)
