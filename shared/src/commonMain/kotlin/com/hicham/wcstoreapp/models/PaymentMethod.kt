package com.hicham.wcstoreapp.models

sealed class PaymentMethod(val value: String) {
    object WIRE : PaymentMethod("bacs")
    object CASH : PaymentMethod("cod")
    data class WCPayCard(val card: PaymentCard) : PaymentMethod("woocommerce_payments")
}