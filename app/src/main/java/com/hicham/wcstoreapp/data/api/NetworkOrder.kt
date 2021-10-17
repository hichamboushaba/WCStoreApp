package com.hicham.wcstoreapp.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// For now, we need only the id
@Serializable
data class NetworkOrder(
    @SerialName("id") val id: Long
)