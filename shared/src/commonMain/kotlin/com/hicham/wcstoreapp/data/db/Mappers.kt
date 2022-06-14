package com.hicham.wcstoreapp.data.db

import com.hicham.wcstoreapp.models.Product
import com.hicham.wcstoreapp.models.ProductPrices

fun ProductEntity.toDomainModel() = Product(
    id = id,
    name = name,
    images = images,
    prices = ProductPrices(
        price = price,
        regularPrice = regularPrice,
        salePrice = salePrice
    ),
    shortDescription = shortDescription,
    description = description
)

fun Product.toEntity() = ProductEntity(
    id = id,
    name = name,
    images = images,
    price = prices.price,
    regularPrice = prices.regularPrice,
    salePrice = prices.salePrice,
    shortDescription = shortDescription,
    description = description
)