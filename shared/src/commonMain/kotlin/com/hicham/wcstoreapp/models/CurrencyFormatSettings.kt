package com.hicham.wcstoreapp.models

data class CurrencyFormatSettings(
    val currencyPrefix: String = "",
    val currencySuffix: String = "",
    val currencyThousandSeparator: String = "",
    val currencyDecimalSeparator: String = ".",
    val currencyDecimalNumber: Int = 2
)
