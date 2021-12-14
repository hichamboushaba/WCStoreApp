@file:UseSerializers(BigDecimalSerializer::class)

package com.hicham.wcstoreapp.data.api

import com.hicham.wcstoreapp.util.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.math.BigDecimal

@Serializable
data class NetworkPrices(
    @SerialName("currency_code")
    val currencyCode: String,

    @SerialName("currency_symbol")
    val currencySymbol: String,

    @SerialName("currency_minor_unit")
    val currencyMinorUnit: Int,

    @SerialName("currency_decimal_separator")
    val currencyDecimalSeparator: String,

    @SerialName("currency_thousand_separator")
    val currencyThousandSeparator: String,

    @SerialName("currency_prefix")
    val currencyPrefix: String,

    @SerialName("currency_suffix")
    val currencySuffix: String,

    val price: BigDecimal,

    @SerialName("regular_price")
    val regularPrice: BigDecimal,

    @SerialName("sale_price")
    val salePrice: BigDecimal
)
