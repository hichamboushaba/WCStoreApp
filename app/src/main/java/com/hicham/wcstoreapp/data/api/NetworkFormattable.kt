package com.hicham.wcstoreapp.data.api

import com.hicham.wcstoreapp.models.CurrencyFormatSettings
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal

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

    fun calculatePrice(price: BigDecimal): BigDecimal =
        price.divide(BigDecimal.TEN.pow(currencyMinorUnit))
}

fun NetworkFormattable.toDomainModel(): CurrencyFormatSettings {
    return CurrencyFormatSettings(
        currencyPrefix = currencyPrefix,
        currencySuffix = currencySuffix,
        currencyDecimalSeparator = currencyDecimalSeparator,
        currencyThousandSeparator = currencyThousandSeparator,
        currencyDecimalNumber = currencyMinorUnit
    )
}