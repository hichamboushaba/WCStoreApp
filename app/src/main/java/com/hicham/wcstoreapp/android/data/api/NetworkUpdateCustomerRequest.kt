package com.hicham.wcstoreapp.android.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkUpdateCustomerRequest(
    @SerialName("shipping_address")
    val shippingAddress: NetworkAddress? = null,
    @SerialName("billing_address")
    val billingAddress: NetworkAddress? = null
)
