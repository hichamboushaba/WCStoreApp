package com.hicham.wcstoreapp.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAddress (
    @SerialName("first_name")
    val firstName: String,

    @SerialName("last_name")
    val lastName: String,

    @SerialName("address_1")
    val address1: String,

    @SerialName("address_2")
    val address2: String?,

    val city: String,
    val state: String?,
    val postcode: String,
    val country: String,
    val email: String? = null,
    val phone: String? = null
)