package com.hicham.wcstoreapp.models

data class CheckoutData(
    val paymentMethod: PaymentMethod?
) {
    companion object {
        val EMPTY by lazy {
            CheckoutData(null)
        }
    }
}