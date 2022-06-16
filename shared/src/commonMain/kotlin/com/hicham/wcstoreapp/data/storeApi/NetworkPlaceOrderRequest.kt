package com.hicham.wcstoreapp.data.storeApi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class NetworkPlaceOrderRequest(
    @SerialName("shipping_address")
    val shippingAddress: NetworkAddress? = null,
    @SerialName("billing_address")
    val billingAddress: NetworkAddress? = null,
    @SerialName("payment_method")
    val paymentMethod: String,
    @SerialName("payment_data")
    val paymentData: JsonElement
)
