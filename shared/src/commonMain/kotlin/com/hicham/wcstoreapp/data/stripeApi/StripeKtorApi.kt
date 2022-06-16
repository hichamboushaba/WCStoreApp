package com.hicham.wcstoreapp.data.stripeApi

import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

class StripeKtorApi(private val httpClient: HttpClient) : StripeApi {
    override suspend fun createPaymentMethod(paymentRequest: NetworkPaymentMethodRequest): String {
        return httpClient.submitForm<NetworkPaymentMethodResponse>(
            url = "/v1/payment_methods",
            formParameters = Parameters.build {
                append("type", "card")
                append("card[number]", paymentRequest.card.number)
                append("card[exp_month]", paymentRequest.card.expiryMonth.toString())
                append("card[exp_year]", paymentRequest.card.expiryYear.toString())
                append("card[cvc]", paymentRequest.card.cvc)
                append(
                    "billing_details[name]",
                    "${paymentRequest.billingAddress.firstName} ${paymentRequest.billingAddress.lastName}"
                )
                append("billing_details[email]", paymentRequest.billingAddress.email!!)
                append(
                    "billing_details[address][line1]",
                    paymentRequest.billingAddress.street1
                )
                paymentRequest.billingAddress.street2?.let {
                    append("billing_details[address][line2]", it)
                }
                append("billing_details[address][city]", paymentRequest.billingAddress.city)
                append(
                    "billing_details[address][postal_code]",
                    paymentRequest.billingAddress.postCode
                )
                append("billing_details[address][state]", paymentRequest.billingAddress.state)
                append("billing_details[address][country]", paymentRequest.billingAddress.country)
            }
        ).id
    }
}