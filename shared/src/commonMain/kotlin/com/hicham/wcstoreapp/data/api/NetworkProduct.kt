package com.hicham.wcstoreapp.data.api

import com.hicham.wcstoreapp.models.Product
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkProduct(
    val id: Long,
    val name: String,
    val variation: String,
    val permalink: String,
    val sku: String,
    val summary: String? = null,

    @SerialName("short_description")
    val shortDescription: String,

    val description: String,

    @SerialName("on_sale")
    val onSale: Boolean,

    val prices: NetworkProductPrices,

    @SerialName("average_rating")
    val averageRating: String,

    @SerialName("review_count")
    val reviewCount: Long,

    val images: List<Image>,

    @SerialName("has_options")
    val hasOptions: Boolean,

    @SerialName("is_purchasable")
    val isPurchasable: Boolean,

    @SerialName("is_in_stock")
    val isInStock: Boolean,

    @SerialName("low_stock_remaining")
    val lowStockRemaining: Double? = null,

    @SerialName("add_to_cart")
    val addToCart: AddToCart
)

@Serializable
data class AddToCart(
    val text: String,
    val description: String
)

@Serializable
data class Image(
    val id: Long,
    val src: String,
    val thumbnail: String,
    val srcset: String,
    val sizes: String,
    val name: String,
    val alt: String
)

fun NetworkProduct.toDomainModel() = Product(
    id = id,
    name = name,
    shortDescription = shortDescription,
    description = description,
    images = images.map { it.src },
    prices = prices.toDomainModel()
)