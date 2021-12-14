package com.hicham.wcstoreapp.models

import com.hicham.wcstoreapp.data.api.NetworkProduct
import com.hicham.wcstoreapp.data.db.entities.ProductEntity

data class Product(
    val id: Long,
    val name: String,
    val images: List<String>,
    val prices: Prices,
    val shortDescription: String,
    val description: String
)

fun ProductEntity.toDomainModel(): Product {
    return Product(
        id = id,
        name = name,
        images = images,
        prices = prices,
        shortDescription = shortDescription,
        description = description
    )
}

fun NetworkProduct.toDomainModel() = Product(
    id = id,
    name = name,
    shortDescription = shortDescription,
    description = description,
    images = images.map { it.src },
    prices = prices.toDomainModel()
)