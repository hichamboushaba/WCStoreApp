package com.hicham.wcstoreapp.ui

import com.hicham.wcstoreapp.models.CurrencyFormatSettings
import com.ionspin.kotlin.bignum.decimal.BigDecimal

expect class CurrencyFormatter constructor(currencyFormatSettings: CurrencyFormatSettings) {
    fun format(price: BigDecimal): String
}