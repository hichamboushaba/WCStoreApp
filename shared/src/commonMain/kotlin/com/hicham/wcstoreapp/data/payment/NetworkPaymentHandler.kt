package com.hicham.wcstoreapp.data.payment

import com.hicham.wcstoreapp.data.stripeApi.NetworkPaymentMethodRequest
import com.hicham.wcstoreapp.data.stripeApi.StripeApi
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.PaymentMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*

class NetworkPaymentHandler(private val stripeApi: StripeApi) : PaymentHandler {
    override suspend fun processPayment(
        paymentMethod: PaymentMethod,
        billingAddress: Address
    ): JsonElement {
        return withContext(Dispatchers.Default) {
            return@withContext when (paymentMethod) {
                PaymentMethod.CASH, PaymentMethod.WIRE -> JsonNull
                is PaymentMethod.WCPayCard -> {
                    val paymentCard = paymentMethod.card
                    val paymentId = stripeApi.createPaymentMethod(
                        paymentRequest = NetworkPaymentMethodRequest(
                            billingAddress = billingAddress,
                            card = paymentCard
                        )
                    )
                    JsonArray(
                        listOf(
                            JsonObject(
                                mapOf(
                                    "key" to JsonPrimitive("paymentMethod"),
                                    "value" to JsonPrimitive("woocommerce_payments")
                                )
                            ),
                            JsonObject(
                                mapOf(
                                    "key" to JsonPrimitive("wcpay-payment-method"),
                                    "value" to JsonPrimitive(paymentId)
                                )
                            ),
                            JsonObject(
                                mapOf(
                                    "key" to JsonPrimitive("wc-woocommerce_payments-new-payment-method"),
                                    "value" to JsonPrimitive(false)
                                )
                            )
                        )
                    )
                }
            }
        }
    }
}