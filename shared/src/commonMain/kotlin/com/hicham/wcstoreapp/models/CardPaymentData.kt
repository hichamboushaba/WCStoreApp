package com.hicham.wcstoreapp.models

data class CardPaymentData(
    val card: Card
)

data class Card(
    val number: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val cvc: String
)