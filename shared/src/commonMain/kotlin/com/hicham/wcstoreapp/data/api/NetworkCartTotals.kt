@file:UseSerializers(BigDecimalSerializer::class)

package com.hicham.wcstoreapp.data.api

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.JsonArray

@Serializable
data class NetworkCartTotals(
    @SerialName("total_items")
    val totalItems: BigDecimal,

    @SerialName("total_items_tax")
    val totalItemsTax: BigDecimal,

    @SerialName("total_fees")
    val totalFees: BigDecimal,

    @SerialName("total_fees_tax")
    val totalFeesTax: BigDecimal,

    @SerialName("total_discount")
    val totalDiscount: BigDecimal,

    @SerialName("total_discount_tax")
    val totalDiscountTax: BigDecimal,

    @SerialName("total_shipping")
    val totalShipping: BigDecimal?,

    @SerialName("total_shipping_tax")
    val totalShippingTax: BigDecimal?,

    @SerialName("total_price")
    val totalPrice: BigDecimal,

    @SerialName("total_tax")
    val totalTax: BigDecimal,

    @SerialName("tax_lines")
    val taxLines: JsonArray
) : NetworkFormattable()
