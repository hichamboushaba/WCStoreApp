package com.hicham.wcstoreapp.android.data.api

import com.hicham.wcstoreapp.models.CheckoutData
import com.hicham.wcstoreapp.models.PaymentMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray

@Serializable
data class NetworkCheckout(
    @SerialName("order_id")
    val orderID: Long,

    val status: String,

    @SerialName("order_key")
    val orderKey: String,

    @SerialName("customer_note")
    val customerNote: String,

    @SerialName("customer_id")
    val customerID: Long,

    @SerialName("billing_address")
    val billingAddress: NetworkAddress,

    @SerialName("shipping_address")
    val shippingAddress: NetworkAddress,

    @SerialName("payment_method")
    val paymentMethod: String,

    @SerialName("payment_result")
    val paymentResult: NetworkPaymentResult
)

@Serializable
data class IngAddress(
    @SerialName("first_name")
    val firstName: String,

    @SerialName("last_name")
    val lastName: String,

    val company: String,

    @SerialName("address_1")
    val address1: String,

    @SerialName("address_2")
    val address2: String,

    val city: String,
    val state: String,
    val postcode: String,
    val country: String,
    val email: String? = null,
    val phone: String? = null
)

@Serializable
data class NetworkPaymentResult(
    @SerialName("payment_status")
    val paymentStatus: String,

    @SerialName("payment_details")
    val paymentDetails: JsonArray,

    @SerialName("redirect_url")
    val redirectURL: String
)

fun NetworkCheckout.toDomainModel() = CheckoutData(
    paymentMethod = PaymentMethod.fromNetworkPaymentMethod(paymentMethod)
)