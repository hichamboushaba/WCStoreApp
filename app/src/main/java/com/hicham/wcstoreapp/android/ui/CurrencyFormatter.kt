package com.hicham.wcstoreapp.android.ui

import com.hicham.wcstoreapp.models.CurrencyFormatSettings
import java.math.BigDecimal
import java.text.DecimalFormat

class CurrencyFormatter(private val currencyFormatSettings: CurrencyFormatSettings) {
    fun format(price: BigDecimal): String = with(currencyFormatSettings) {
        val decimalFormat = if (currencyDecimalNumber > 0) {
            DecimalFormat("#,##0.${"0".repeat(currencyDecimalNumber)}")
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