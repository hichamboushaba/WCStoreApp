package com.hicham.wcstoreapp.models

import com.hicham.wcstoreapp.data.api.NetworkCheckout

data class CheckoutData(
    val shippingAddress: Address?,
    val billingAddress: Address?,
    val paymentMethod: PaymentMethod?
) {
    companion object {
        val EMPTY by lazy {
            CheckoutData(null, null, null)
        }
    }
}

fun NetworkCheckout.toDomainModel() = CheckoutData(
    shippingAddress = shippingAddress.toDomainModel(),
    billingAddress = billingAddress.toDomainModel(),
    paymentMethod = PaymentMethod.fromNetworkPaymentMethod(paymentMethod)
)