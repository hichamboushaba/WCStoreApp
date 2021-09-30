package com.hicham.wcstoreapp.models

import android.os.Parcelable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val label: String? = null,
    val firstName: String,
    val lastName: String,
    val street1: String,
    val street2: String?,
    val phone: String?,
    val city: String,
    val state: String?,
    val postCode: String,
    val country: String,
): Parcelable {
    fun formatAddress(): AnnotatedString {
        return buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                append(firstName)
                append(" ")
                append(lastName)
                append("\n")
                if (phone != null) {
                    append(phone)
                    append("\n")
                }
            }
            append("\n")
            append(street1)
            append("\n")
            street2?.let {
                append(it)
                append("\n")
            }
            append(city)
            append(",")
            state?.let {
                append(" ")
                append(it)
            }
            append(" ")
            append(postCode)
            append("\n")
            append(country)
        }
    }
}