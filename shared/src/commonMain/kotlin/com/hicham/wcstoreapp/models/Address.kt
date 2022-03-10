package com.hicham.wcstoreapp.models

import com.hicham.wcstoreapp.util.Parcelable
import com.hicham.wcstoreapp.util.Parcelize

@Parcelize
data class Address(
    val label: String? = null,
    val firstName: String,
    val lastName: String,
    val street1: String,
    val street2: String?,
    val phone: String?,
    val email: String?,
    val city: String,
    val state: String,
    val postCode: String,
    val country: String,
) : Parcelable
