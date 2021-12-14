package com.hicham.wcstoreapp.models

data class CartItem(
    val id: String,
    val product: Product,
    val quantity: Int
)