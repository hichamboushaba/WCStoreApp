@file:UseSerializers(BigDecimalSerializer::class)

package com.hicham.wcstoreapp.data.api

import com.hicham.wcstoreapp.models.NetworkFormattable
import com.hicham.wcstoreapp.util.BigDecimalSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.json.JsonArray
import java.math.BigDecimal

@Serializable
data class NetworkCartItem(
    val key: String,
    val id: Long,
    val quantity: Int,

    @SerialName("quantity_limit")
    val quantityLimit: Long,

    val name: String,
    val summary: String? = null,

    @SerialName("short_description")
    val shortDescription: String,

    val description: String,
    val sku: String,

    @SerialName("low_stock_remaining")
    val lowStockRemaining: Float? = null,

    @SerialName("backorders_allowed")
    val backordersAllowed: Boolean,

    @SerialName("show_backorder_badge")
    val showBackorderBadge: Boolean,

    @SerialName("sold_individually")
    val soldIndividually: Boolean,

    val permalink: String,
    val images: List<Image>,
    val variation: JsonArray,
    val prices: Prices,
    val totals: NetworkItemTotals
)

@Serializable
data class NetworkItemTotals(
    @SerialName("line_subtotal")
    val lineSubtotal: BigDecimal,

    @SerialName("line_subtotal_tax")
    val lineSubtotalTax: BigDecimal,

    @SerialName("line_total")
    val lineTotal: BigDecimal,

    @SerialName("line_total_tax")
    val lineTotalTax: BigDecimal
) : NetworkFormattable()