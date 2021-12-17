package com.hicham.wcstoreapp.ui

import com.hicham.wcstoreapp.models.PaymentMethod

val PaymentMethod.title: String
    get() = when (this) {
        PaymentMethod.WIRE -> "Direct Bank Transfer (BACS)"
        PaymentMethod.CASH -> "Cash on Delivery"
    }