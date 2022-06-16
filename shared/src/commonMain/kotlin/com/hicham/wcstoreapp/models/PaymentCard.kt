package com.hicham.wcstoreapp.models

data class Card(
    val number: String,
    val expiryMonth: Int,
    val expiryYear: Int,
    val cvc: String
)