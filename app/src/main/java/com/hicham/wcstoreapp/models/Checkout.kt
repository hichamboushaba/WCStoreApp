package com.hicham.wcstoreapp.models

import com.hicham.wcstoreapp.data.api.NetworkCheckout

data class CheckoutData(
    val paymentMethod: PaymentMethod?
) {
    companion object {
        val EMPTY by lazy {
            CheckoutData(null)
        }
    }
}

fun NetworkCheckout.toDomainModel() = CheckoutData(
    paymentMethod = PaymentMethod.fromNetworkPaymentMethod(paymentMethod)
)