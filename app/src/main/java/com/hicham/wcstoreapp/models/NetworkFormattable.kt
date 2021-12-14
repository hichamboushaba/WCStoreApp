package com.hicham.wcstoreapp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.text.DecimalFormat

@Serializable
open class NetworkFormattable {
    @SerialName("currency_code")
    val currencyCode: String = ""

    @SerialName("currency_symbol")
    val currencySymbol: String = ""

    @SerialName("currency_minor_unit")
    val currencyMinorUnit: Int = 0

    @SerialName("currency_decimal_separator")
    val currencyDecimalSeparator: String = ""

    @SerialName("currency_thousand_separator")
    val currencyThousandSeparator: String = ""

    @SerialName("currency_prefix")
    val currencyPrefix: String = ""

    @SerialName("currency_suffix")
    val currencySuffix: String = ""

    fun calculatePrice(price: BigDecimal): BigDecimal = price.divide(BigDecimal.TEN.pow(currencyMinorUnit))

    fun format(price: BigDecimal): String {
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

        return "$currencyPrefix${decimalFormat.format(price)}$currencySuffix"
    }
}