package com.hicham.wcstoreapp.data.payment

import com.hicham.wcstoreapp.BuildKonfig
import com.hicham.wcstoreapp.models.Address
import com.hicham.wcstoreapp.models.PaymentMethod
import com.stripe.android.Stripe
import com.stripe.android.model.Address as StripeAddress
import com.stripe.android.model.PaymentMethod as StripePaymentMethod
import com.stripe.android.model.PaymentMethodCreateParams
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*

class NetworkPaymentHandler(private val stripe: Stripe) : PaymentHandler {
    override suspend fun processPayment(
        paymentMethod: PaymentMethod,
        billingAddress: Address
    ): JsonElement {
        return withContext(Dispatchers.Default) {
            return@withContext when (paymentMethod) {
                PaymentMethod.CASH, PaymentMethod.WIRE -> JsonNull
                is PaymentMethod.WCPayCard -> {
                    val paymentData = paymentMethod.data
                    val paymentId = stripe.createPaymentMethodSynchronous(
                        paymentMethodCreateParams = PaymentMethodCreateParams.create(
                            card = PaymentMethodCreateParams.Card(
                                number = paymentData.card.number,
                                expiryMonth = paymentData.card.expiryMonth,
                                expiryYear = paymentData.card.expiryYear,
                                cvc = paymentData.card.cvc
                            ),
                            billingDetails = StripePaymentMethod.BillingDetails(
                                address = StripeAddress(
                                    line1 = billingAddress.street1,
                                    line2 = billingAddress.street2,
                                    city = billingAddress.city,
                                    state = billingAddress.state,
                                    postalCode = billingAddress.postCode,
                                    country = billingAddress.country
                                )
                            )
                        ),
                        stripeAccountId = BuildKonfig.WC_PAY_STRIPE_ACCOUNT_ID
                    )!!.id!!
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