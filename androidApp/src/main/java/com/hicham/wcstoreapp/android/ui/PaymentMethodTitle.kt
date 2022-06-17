package com.hicham.wcstoreapp.android.ui

import com.hicham.wcstoreapp.models.PaymentMethod

val PaymentMethod.title: String
    get() = when (this) {
        PaymentMethod.WIRE -> "Direct Bank Transfer (BACS)"
        PaymentMethod.CASH -> "Cash on Delivery"
        is PaymentMethod.WCPayCard -> "Card ending in ${card.number.takeLast(4)} (expires ${card.expiryMonth}/${card.expiryYear})"
    }