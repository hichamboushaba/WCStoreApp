package com.hicham.wcstoreapp.models

import java.math.BigDecimal

data class ProductPrices(
    val price: BigDecimal,
    val regularPrice: BigDecimal,
    val salePrice: BigDecimal
)
