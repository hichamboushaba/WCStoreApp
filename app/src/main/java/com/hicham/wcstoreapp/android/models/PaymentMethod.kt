package com.hicham.wcstoreapp.android.models

enum class PaymentMethod(val value: String) {
    WIRE("bacs"), CASH("cod");

    companion object {
        fun fromNetworkPaymentMethod(value: String): PaymentMethod? {
            if (value == "") return null
            return PaymentMethod.values().firstOrNull { it.value == value }
                ?: error("Unsupported payment method")
        }
    }
}