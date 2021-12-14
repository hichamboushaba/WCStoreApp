package com.hicham.wcstoreapp.models

import com.hicham.wcstoreapp.data.api.NetworkPrices
import java.math.BigDecimal
import java.text.DecimalFormat

data class Prices(
    val price: BigDecimal,
    val regularPrice: BigDecimal,
    val salePrice: BigDecimal,
    val formattedPrice: String,
    val formattedRegularPrice: String,
    val formattedSalePrice: String
)

fun NetworkPrices.toDomainModel(): Prices {
    val decimalFormat = if (currencyMinorUnit > 0) {
        DecimalFormat("#,##0.${"0".repeat(currencyMinorUnit)}")
    } else {
        DecimalFormat("#,##0")
    }

    decimalFormat.decimalFormatSymbols = decimalFormat.decimalFormatSymbols.apply {
        // If no decimal separator is set, keep whatever the system default is
        currencyDecimalSeparator.takeIf { it.isNotEmpty() }?.let {
            decimalSeparator = it[0]
        }
        // If no thousands separator is set, assume it's intentional and clear it in the formatter
        currencyThousandSeparator.takeIf { it.isNotEmpty() }?.let {
            groupingSeparator = it[0]
        } ?: run { decimalFormat.isGroupingUsed = false }
    }

    fun convert(price: BigDecimal): BigDecimal = price.divide(BigDecimal.TEN.pow(currencyMinorUnit))
    fun format(price: BigDecimal): String {
        return "$currencyPrefix${decimalFormat.format(price)}$currencySuffix"
    }

    val price = convert(price)
    val regularPrice = convert(regularPrice)
    val salePrice = convert(salePrice)

    return Prices(
        price = price,
        regularPrice = regularPrice,
        salePrice = salePrice,
        formattedPrice = format(price),
        formattedRegularPrice = format(regularPrice),
        formattedSalePrice = format(salePrice)
    )
}