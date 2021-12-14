package com.hicham.wcstoreapp.models

import com.hicham.wcstoreapp.data.api.NetworkProductPrices
import java.math.BigDecimal

data class ProductPrices(
    val price: BigDecimal,
    val regularPrice: BigDecimal,
    val salePrice: BigDecimal,
    val formattedPrice: String,
    val formattedRegularPrice: String,
    val formattedSalePrice: String
)

fun NetworkProductPrices.toDomainModel(): ProductPrices {
    val price = calculatePrice(price)
    val regularPrice = calculatePrice(regularPrice)
    val salePrice = calculatePrice(salePrice)

    return ProductPrices(
        price = price,
        regularPrice = regularPrice,
        salePrice = salePrice,
        formattedPrice = format(price),
        formattedRegularPrice = format(regularPrice),
        formattedSalePrice = format(salePrice)
    )
}