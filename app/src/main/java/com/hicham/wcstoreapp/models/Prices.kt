package com.hicham.wcstoreapp.models

import com.hicham.wcstoreapp.data.api.NetworkPrices
import java.math.BigDecimal

data class Prices(
    val price: BigDecimal,
    val regularPrice: BigDecimal,
    val salePrice: BigDecimal,
    val formattedPrice: String,
    val formattedRegularPrice: String,
    val formattedSalePrice: String
)

fun NetworkPrices.toDomainModel(): Prices {
    fun format(price: BigDecimal): String {
        return "$currencyPrefix${price.toPlainString()}$currencySuffix"
    }

    val price = price.divide(BigDecimal.TEN.pow(currencyMinorUnit))
    val regularPrice = regularPrice.divide(BigDecimal.TEN.pow(currencyMinorUnit))
    val salePrice = salePrice.divide(BigDecimal.TEN.pow(currencyMinorUnit))
    return Prices(
        price = price,
        regularPrice = regularPrice,
        salePrice = salePrice,
        formattedPrice = format(price),
        formattedRegularPrice = format(regularPrice),
        formattedSalePrice = format(salePrice)
    )
}