package com.hicham.wcstoreapp.data.db

data class CartWithItemsEntity(
    val cartEntity: CartEntity,
    val items: List<CartItemWithProduct>
)