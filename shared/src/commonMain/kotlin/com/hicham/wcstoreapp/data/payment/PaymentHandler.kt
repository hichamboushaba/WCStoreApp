package com.hicham.wcstoreapp.data.payment

import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.PaymentMethod
import kotlinx.serialization.json.JsonElement

interface PaymentHandler {
    suspend fun processPayment(paymentMethod: PaymentMethod, billingAddress: Address): JsonElement
}