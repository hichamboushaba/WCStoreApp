package com.hicham.wcstoreapp.models

import com.hicham.wcstoreapp.data.source.network.NetworkProduct

data class Product(
    val id: Long,
    val name: String,
    val images: List<String>
)

fun NetworkProduct.toProduct() = Product(
    id = id,
    name = name,
    images = images.map { it.src }
)