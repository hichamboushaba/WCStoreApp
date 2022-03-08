package com.hicham.wcstoreapp.android.ui.common

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.hicham.wcstoreapp.models.Address
import java.util.*

fun Address.formatAddress(): AnnotatedString {
    return buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(firstName)
            append(" ")
            append(lastName)
            if (!phone.isNullOrEmpty()) {
                append("\n")
                append(phone!!)
                append("\n")
            }
        }
        append("\n")
        append(street1)
        append("\n")
        if (!street2.isNullOrEmpty()) {
            append(street2!!)
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