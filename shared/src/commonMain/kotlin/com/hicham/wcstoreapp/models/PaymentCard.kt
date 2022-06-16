package com.hicham.wcstoreapp.models

data class PaymentCard(
    val number: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val cvc: String
)