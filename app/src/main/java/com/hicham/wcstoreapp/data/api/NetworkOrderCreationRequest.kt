package com.hicham.wcstoreapp.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkOrderCreationRequest(
    @SerialName("payment_method")
    val paymentMethod: String,

    @SerialName("set_paid")
    val setPaid: Boolean,

    val billing: NetworkAddress,
    val shipping: NetworkAddress,

    @SerialName("line_items")
    val lineItems: List<LineItem>
)

@Serializable
data class LineItem(
    @SerialName("product_id")
    val productID: Long,

    val quantity: Int,

    @SerialName("variation_id")
    val variationID: Long? = null
)

@Serializable
data class ShippingLine(
    @SerialName("method_id")
    val methodID: String,

    @SerialName("method_title")
    val methodTitle: String,

    val total: String
)
