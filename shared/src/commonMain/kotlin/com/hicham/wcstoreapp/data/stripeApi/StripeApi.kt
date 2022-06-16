package com.hicham.wcstoreapp.data.stripeApi

interface StripeApi {
    suspend fun createPaymentMethod(
        paymentRequest: NetworkPaymentMethodRequest
    ): String
}