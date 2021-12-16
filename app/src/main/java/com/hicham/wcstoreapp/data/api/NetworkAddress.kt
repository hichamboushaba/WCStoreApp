package com.hicham.wcstoreapp.data.api

import com.hicham.wcstoreapp.models.Address
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

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
) {
    // TODO check if this override is enough for Retrofit's use
    override fun toString(): String {
        return Json.encodeToJsonElement(this).jsonObject.toMap().toString()
    }
}

fun Address.toNetworkAddress(): NetworkAddress {
    return NetworkAddress(
        firstName = firstName,
        lastName = lastName,
        address1 = street1,
        address2 = street2,
        city = city,
        state = state,
        postcode = postCode,
        country = country
    )
}