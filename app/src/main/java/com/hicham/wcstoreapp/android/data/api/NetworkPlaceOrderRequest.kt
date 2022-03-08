package com.hicham.wcstoreapp.android.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkPlaceOrderRequest(
    @SerialName("shipping_address")
    val shippingAddress: NetworkAddress? = null,
    @SerialName("billing_address")
    val billingAddress: NetworkAddress? = null,
    @SerialName("payment_method")
    val paymentMethod: String
)
