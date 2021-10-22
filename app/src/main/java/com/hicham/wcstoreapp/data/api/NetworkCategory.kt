package com.hicham.wcstoreapp.data.api

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCategory(
    val id: Long,
    val name: String,
    val slug: String
)
