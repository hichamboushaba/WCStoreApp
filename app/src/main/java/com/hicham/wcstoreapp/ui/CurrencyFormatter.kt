package com.hicham.wcstoreapp.ui

import com.hicham.wcstoreapp.models.CurrencyFormatSettings
import com.hicham.wcstoreapp.models.CurrencyPosition
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

class CurrencyFormatter(private val currencyFormatSettings: CurrencyFormatSettings) {
    fun format(price: BigDecimal): String {
        val rawValue = price.abs()
        val decimalFormat = if (currencyFormatSettings.currencyDecimalNumber > 0) {
            DecimalFormat("#,##0.${"0".repeat(currencyFormatSettings.currencyDecimalNumber)}")
        } else {
            DecimalFormat("#,##0")
        }

        decimalFormat.decimalFormatSymbols = decimalFormat.decimalFormatSymbols.apply {
            // If no decimal separator is set, keep whatever the system default is
            currencyFormatSettings.currencyDecimalSeparator.takeIf { it.isNotEmpty() }?.let {
                decimalSeparator = it[0]
            }
            // If no thousands separator is set, assume it's intentional and clear it in the formatter
            currencyFormatSettings.currencyThousandSeparator.takeIf { it.isNotEmpty() }?.let {
                groupingSeparator = it[0]
            } ?: run { decimalFormat.isGroupingUsed = false }
        }

        return decimalFormat.format(rawValue).appendCurrency(isNegative = price < BigDecimal.ZERO)
    }

    private fun String.appendCurrency(isNegative: Boolean): String {
        val currencySymbol = getLocalizedCurrencySymbolForCode(currencyFormatSettings.currencyCode)
        with(StringBuilder()) {
            if (isNegative) {
                append("-")
            }
            append(
                when (currencyFormatSettings.currencyPosition) {
                    CurrencyPosition.LEFT -> "$currencySymbol${this@appendCurrency}"
                    CurrencyPosition.LEFT_SPACE -> "$currencySymbol ${this@appendCurrency}"
                    CurrencyPosition.RIGHT -> "${this@appendCurrency}$currencySymbol"
                    CurrencyPosition.RIGHT_SPACE -> "${this@appendCurrency} $currencySymbol"
                }
            )
            return toString()
        }
    }

    private fun getLocalizedCurrencySymbolForCode(currencyCode: String): String {
        return try {
            Currency.getInstance(currencyCode).getSymbol(Locale.getDefault())
        } catch (e: IllegalArgumentException) {
            currencyCode
        }
    }
}