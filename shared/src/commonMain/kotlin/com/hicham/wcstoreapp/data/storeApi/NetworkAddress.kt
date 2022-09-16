package com.hicham.wcstoreapp.data.storeApi

import com.hicham.wcstoreapp.models.Address
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkAddress(
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

fun Address.toNetworkAddress(): NetworkAddress {
    return NetworkAddress(
        firstName = firstName,
        lastName = lastName,
        address1 = street1,
        address2 = street2,
        phone = phone,
        email = email,
        city = city,
        state = state,
        postcode = postCode,
        country = country
    )
}

fun NetworkAddress.toDomainModel() = Address(
    firstName = firstName,
    lastName = lastName,
    street1 = address1,
    street2 = address2,
    email = email,
    city = city,
    state = state.orEmpty(),
    postCode = postcode,
    country = country,
    phone = phone,
)
