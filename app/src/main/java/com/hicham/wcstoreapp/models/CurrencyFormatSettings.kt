package com.hicham.wcstoreapp.models

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

data class CurrencyFormatSettings(
    val currencyCode: String,
    val currencyPosition: CurrencyPosition,
    val currencyThousandSeparator: String = "",
    val currencyDecimalSeparator: String = ".",
    val currencyDecimalNumber: Int = 2
)

enum class CurrencyPosition {
    LEFT, RIGHT, LEFT_SPACE, RIGHT_SPACE
}
