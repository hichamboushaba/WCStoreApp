package com.hicham.wcstoreapp.models

data class Product(
    val id: Long,
    val name: String,
    val images: List<String>,
    val prices: ProductPrices,
    val shortDescription: String,
    val description: String
)
