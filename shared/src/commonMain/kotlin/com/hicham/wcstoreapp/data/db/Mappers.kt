package com.hicham.wcstoreapp.data.db

import com.hicham.wcstoreapp.models.*

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

fun CartWithItemsEntity.toDomainModel() = Cart(
    totals = CartTotals(
        subtotal = cartEntity.subtotal,
        tax = cartEntity.tax,
        shippingEstimate = cartEntity.shippingEstimate,
        total = cartEntity.total
    ),
    items = items.mapNotNull { item ->
        CartItem(
            id = item.cartItem.key,
            product = item.product.toDomainModel(),
            quantity = item.cartItem.quantity,
            totals = CartItemTotals(
                subtotal = item.cartItem.subtotal,
                tax = item.cartItem.subtotal,
                total = item.cartItem.total
            )
        )
    }
)