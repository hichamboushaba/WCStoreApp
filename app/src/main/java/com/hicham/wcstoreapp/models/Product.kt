package com.hicham.wcstoreapp.models

import com.hicham.wcstoreapp.data.source.network.NetworkProduct
import java.math.BigDecimal

data class Product(
    val id: Long,
    val name: String,
    val images: List<String>,
    val price: BigDecimal,
    val shortDescription: String,
    val description: String
)

fun NetworkProduct.toProduct() = Product(
    id = id,
    name = name,
    shortDescription = shortDescription,
    description = description,
    images = images.map { it.src },
    price = price
)