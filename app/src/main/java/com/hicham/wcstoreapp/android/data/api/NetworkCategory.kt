package com.hicham.wcstoreapp.android.data.api

import kotlinx.serialization.Serializable

@Serializable
data class NetworkCategory(
    val id: Long,
    val name: String,
    val slug: String
)
