package com.hicham.wcstoreapp.models

enum class PaymentMethod(val value: String) {
    WIRE("bacs"), CASH("cod");

    companion object {
        fun fromNetworkPaymentMethod(value: String): PaymentMethod {
            return PaymentMethod.values().firstOrNull { it.value == value }
                ?: error("Unsupported payment method")
        }
    }
}