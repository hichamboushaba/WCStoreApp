package com.hicham.wcstoreapp.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hicham.wcstoreapp.models.Address

@Entity
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String?,
    val firstName: String,
    val lastName: String,
    val street1: String,
    val street2: String?,
    val phone: String?,
    val city: String,
    val state: String?,
    val postCode: String,
    val country: String
) {
    fun toAddress() = Address(
        label = label,
        firstName = firstName,
        lastName = lastName,
        street1 = street1,
        street2 = street2,
        phone = phone,
        city = city,
        state = state,
        postCode = postCode,
        country = country
    )

    fun isSameAsAddress(address: Address): Boolean {
        return toAddress() == address
    }
}