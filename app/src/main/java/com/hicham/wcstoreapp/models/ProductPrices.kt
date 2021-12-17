package com.hicham.wcstoreapp.models

import com.hicham.wcstoreapp.data.api.NetworkProductPrices
import java.math.BigDecimal

data class ProductPrices(
    val price: BigDecimal,
    val regularPrice: BigDecimal,
    val salePrice: BigDecimal
)

fun NetworkProductPrices.toDomainModel(): ProductPrices {
    return ProductPrices(
        price = calculatePrice(price),
        regularPrice = calculatePrice(regularPrice),
        salePrice = calculatePrice(salePrice),
    )
}