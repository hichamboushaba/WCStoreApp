package com.hicham.wcstoreapp.ui

import com.hicham.wcstoreapp.models.CurrencyFormatSettings
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.cinterop.convert
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.darwin.NSUInteger

actual class CurrencyFormatter actual constructor(private val currencyFormatSettings: CurrencyFormatSettings) {
    actual fun format(price: BigDecimal): String = with(currencyFormatSettings) {
        val price = NSNumberFormatter().numberFromString(price.toPlainString())!!

        val numberFormatter = NSNumberFormatter().apply {
            decimalSeparator = currencyDecimalSeparator
            groupingSeparator = currencyGroupingSeparator
            usesGroupingSeparator = currencyGroupingSeparator.isNotEmpty()
            this.maximumFractionDigits = currencyDecimalNumber.toUInt().convert()
            this.minimumFractionDigits = currencyDecimalNumber.toUInt().convert()
        }

        return "$currencyPrefix${numberFormatter.stringFromNumber(price)}$currencySuffix"
    }
}