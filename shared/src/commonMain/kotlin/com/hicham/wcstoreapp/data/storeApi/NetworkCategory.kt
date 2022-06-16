package com.hicham.wcstoreapp.data.storeApi

import com.hicham.wcstoreapp.models.Category
import kotlinx.serialization.Serializable

@Serializable
data class NetworkCategory(
    val id: Long,
    val name: String,
    val slug: String
)

fun NetworkCategory.toDomainModel() = Category(
    id = id,
    name = name
)
