package com.hicham.wcstoreapp.models

import com.hicham.wcstoreapp.data.api.NetworkProduct
import com.hicham.wcstoreapp.data.db.entities.ProductEntity
import java.math.BigDecimal

data class Product(
    val id: Long,
    val name: String,
    val images: List<String>,
    val price: BigDecimal,
    val shortDescription: String,
    val description: String
)

fun ProductEntity.toProduct(): Product {
    return Product(
        id = id,
        name = name,
        images = images,
        price = BigDecimal(price),
        shortDescription = shortDescription,
        description = description
    )
}

fun NetworkProduct.toProduct() = Product(
    id = id,
    name = name,
    shortDescription = shortDescription,
    description = description,
    images = images.map { it.src },
    price = price
)