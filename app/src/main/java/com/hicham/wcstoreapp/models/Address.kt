package com.hicham.wcstoreapp.models

import android.os.Parcelable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.hicham.wcstoreapp.data.api.NetworkAddress
import kotlinx.parcelize.Parcelize
import java.util.*

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
) : Parcelable {
    fun formatAddress(): AnnotatedString {
        return buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(firstName)
                append(" ")
                append(lastName)
                if (!phone.isNullOrEmpty()) {
                    append("\n")
                    append(phone)
                    append("\n")
                }
            }
            append("\n")
            append(street1)
            append("\n")
            if (!street2.isNullOrEmpty()) {
                append(street2)
                append("\n")
            }
            append(city)
            append(",")
            append(" ")
            append(state)
            append(" ")
            append(postCode)
            append("\n")
            append(Locale("", country).displayCountry)
        }
    }
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