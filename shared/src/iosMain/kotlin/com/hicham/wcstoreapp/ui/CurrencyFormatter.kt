package com.hicham.wcstoreapp.ui

import com.hicham.wcstoreapp.models.CurrencyFormatSettings
import com.ionspin.kotlin.bignum.decimal.BigDecimal

actual class CurrencyFormatter actual constructor(private val currencyFormatSettings: CurrencyFormatSettings) {
    actual fun format(price: BigDecimal): String = with(currencyFormatSettings) {
//        val price = java.math.BigDecimal(price.toString())
//        val decimalFormat = if (currencyDecimalNumber > 0) {
//            DecimalFormat("#,##0.${"0".repeat(currencyDecimalNumber)}")
//        } else {
//            DecimalFormat("#,##0")
//        }
//
//        decimalFormat.decimalFormatSymbols = decimalFormat.decimalFormatSymbols.apply {
//            // If no decimal separator is set, keep whatever the system default is
//            currencyDecimalSeparator.takeIf { it.isNotEmpty() }?.let {
//                decimalSeparator = it[0]
//            }
//            // If no thousands separator is set, assume it's intentional and clear it in the formatter
//            currencyThousandSeparator.takeIf { it.isNotEmpty() }?.let {
//                groupingSeparator = it[0]
//            } ?: run { decimalFormat.isGroupingUsed = false }
//        }
//
//        return "$currencyPrefix${decimalFormat.format(price)}$currencySuffix"
        // TODO
        return@with price.toPlainString()
    }
}